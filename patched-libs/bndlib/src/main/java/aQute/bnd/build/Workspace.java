package aQute.bnd.build;

import aQute.bnd.maven.support.Maven;
import aQute.bnd.service.BndListener;
import aQute.bnd.service.RepositoryPlugin;
import aQute.bnd.service.action.Action;
import aQute.lib.deployer.FileRepo;
import aQute.lib.io.IO;
import aQute.lib.osgi.Processor;
import aQute.libg.reporter.Reporter;

import javax.naming.TimeLimitExceededException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class Workspace extends Processor {
  public static final String BUILDFILE = "build.bnd";
  public static final String CNFDIR = "cnf";
  public static final String BNDDIR = "bnd";
  public static final String CACHEDIR = "cache";

  static Map<File, WeakReference<Workspace>> cache = newHashMap();
  final Map<String, Project> models = newHashMap();
  final Map<String, Action> commands = newMap();
  final CachedFileRepo cachedRepo;
  final File buildDir;
  final Maven maven = new Maven(Processor.getExecutor());
  private boolean postpone;

  /**
   * This static method finds the workspace and creates a project (or returns
   * an existing project)
   *
   * @param projectDir
   * @return
   */
  public static Project getProject(File projectDir) throws Exception {
    projectDir = projectDir.getAbsoluteFile();
    assert projectDir.isDirectory();

    Workspace ws = getWorkspace(projectDir.getParentFile());
    return ws.getProject(projectDir.getName());
  }

  public static Workspace getWorkspace(File parent) throws Exception {
    File workspaceDir = parent.getAbsoluteFile();

    // the cnf directory can actually be a
    // file that redirects
    while (workspaceDir.isDirectory()) {
      File test = new File(workspaceDir, CNFDIR);

      if (!test.exists()) test = new File(workspaceDir, BNDDIR);

      if (test.isDirectory()) break;

      if (test.isFile()) {
        String redirect = IO.collect(test).trim();
        test = getFile(test.getParentFile(), redirect).getAbsoluteFile();
        workspaceDir = test;
      }
      if (!test.exists()) throw new IllegalArgumentException("No Workspace found from: " + parent);
    }

    synchronized (cache) {
      WeakReference<Workspace> wsr = cache.get(workspaceDir);
      Workspace ws;
      if (wsr == null || (ws = wsr.get()) == null) {
        ws = new Workspace(workspaceDir);
        cache.put(workspaceDir, new WeakReference<Workspace>(ws));
      }
      return ws;
    }
  }

  public Workspace(File dir) throws Exception {
    dir = dir.getAbsoluteFile();
    dir.mkdirs();
    assert dir.isDirectory();

    File buildDir = new File(dir, BNDDIR).getAbsoluteFile();
    if (!buildDir.isDirectory()) buildDir = new File(dir, CNFDIR).getAbsoluteFile();

    this.buildDir = buildDir;

    File buildFile = new File(buildDir, BUILDFILE).getAbsoluteFile();
    if (!buildFile.isFile()) warning("No Build File in " + dir);

    setProperties(buildFile, dir);
    propertiesChanged();

    cachedRepo = new CachedFileRepo();
  }

  public Project getProject(String bsn) throws Exception {
    synchronized (models) {
      Project project = models.get(bsn);
      if (project != null) return project;

      File projectDir = getFile(bsn);
      project = new Project(this, projectDir);
      if (!project.isValid()) return null;

      models.put(bsn, project);
      return project;
    }
  }

  public boolean isPresent(String name) {
    return models.containsKey(name);
  }

  public Collection<Project> getCurrentProjects() {
    return models.values();
  }

  public boolean refresh() {
    if (super.refresh()) {
      for (Project project : getCurrentProjects()) {
        project.propertiesChanged();
      }
      return true;
    }
    return false;
  }

  @Override
  public void propertiesChanged() {
    super.propertiesChanged();
    File extDir = new File(this.buildDir, "ext");
    File[] extensions = extDir.listFiles();
    if (extensions != null) {
      for (File extension : extensions) {
        if (extension.getName().endsWith(".bnd")) {
          try {
            doIncludeFile(extension, false, getProperties());
          }
          catch (Exception e) {
            error("PropertiesChanged: " + e.getMessage());
          }
        }
      }
    }
  }

  public String _workspace(String args[]) {
    return getBase().getAbsolutePath();
  }

  public void addCommand(String menu, Action action) {
    commands.put(menu, action);
  }

  public void removeCommand(String menu) {
    commands.remove(menu);
  }

  public void fillActions(Map<String, Action> all) {
    all.putAll(commands);
  }

  public Collection<Project> getAllProjects() throws Exception {
    List<Project> projects = new ArrayList<Project>();
    for (File file : getBase().listFiles()) {
      if (new File(file, Project.BNDFILE).isFile()) projects.add(getProject(file));
    }
    return projects;
  }

  /**
   * Inform any listeners that we changed a file (created/deleted/changed).
   *
   * @param f The changed file
   */
  public void changedFile(File f) {
    List<BndListener> listeners = getPlugins(BndListener.class);
    for (BndListener l : listeners) {
      try {
        l.changed(f);
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public void bracket(boolean begin) {
    List<BndListener> listeners = getPlugins(BndListener.class);
    for (BndListener l : listeners) {
      try {
        if (begin) {
          l.begin();
        }
        else {
          l.end();
        }
      }
      catch (Exception e) {
        // who cares?
      }
    }
  }


  /**
   * Signal a BndListener plugin.
   * We ran an infinite bug loop :-(
   */
  final ThreadLocal<Reporter> signalBusy = new ThreadLocal<Reporter>();

  public void signal(Reporter reporter) {
    if (signalBusy.get() != null) return;

    signalBusy.set(reporter);
    try {
      List<BndListener> listeners = getPlugins(BndListener.class);
      for (BndListener l : listeners) {
        try {
          l.signal(this);
        }
        catch (Exception e) {
          // who cares?
        }
      }
    }
    catch (Exception e) {
      // Ignore
    }
    finally {
      signalBusy.set(null);
    }
  }

  @Override
  public void signal() {
    signal(this);
  }

  private void copy(InputStream in, OutputStream out) throws Exception {
    byte data[] = new byte[10000];
    int size = in.read(data);
    while (size > 0) {
      out.write(data, 0, size);
      size = in.read(data);
    }
  }

  class CachedFileRepo extends FileRepo {
    final Lock lock = new ReentrantLock();
    boolean inited;

    CachedFileRepo() {
      super("cache", getFile(buildDir, CACHEDIR), false);
    }

    protected void init() throws Exception {
      if (lock.tryLock(50, TimeUnit.SECONDS) == false) {
        throw new TimeLimitExceededException("Cached File Repo is locked and can't acquire it");
      }
      try {
        if (!inited) {
          inited = true;
          root.mkdirs();
          if (!root.isDirectory()) throw new IllegalArgumentException("Cannot create cache dir " + root);

          InputStream in = getClass().getResourceAsStream(EMBEDDED_REPO);
          if (in != null) {
            unzip(in, root);
          }
          else {
            System.out.println("!!!! Couldn't find embedded-repo.jar in bundle ");
            error("Couldn't find embedded-repo.jar in bundle ");
          }
        }
      }
      finally {
        lock.unlock();
      }
    }

    void unzip(InputStream in, File dir) throws Exception {
      try {
        JarInputStream jin = new JarInputStream(in);
        JarEntry jentry = jin.getNextJarEntry();
        while (jentry != null) {
          if (!jentry.isDirectory()) {
            File dest = Processor.getFile(dir, jentry.getName());
            if (!dest.isFile() || dest.lastModified() < jentry.getTime() || jentry.getTime() == 0) {
              dest.getParentFile().mkdirs();
              FileOutputStream out = new FileOutputStream(dest);
              try {
                copy(jin, out);
              }
              finally {
                out.close();
              }
            }
          }
          jentry = jin.getNextJarEntry();
        }
      }
      finally {
        in.close();
      }
    }
  }

  public List<RepositoryPlugin> getRepositories() {
    return getPlugins(RepositoryPlugin.class);
  }

  public static Workspace getWorkspace(String path) throws Exception {
    File file = IO.getFile(new File(""), path);
    return getWorkspace(file);
  }

  public Maven getMaven() {
    return maven;
  }

  @Override
  protected void setTypeSpecificPlugins(Set<Object> list) {
    super.setTypeSpecificPlugins(list);
    list.add(maven);
    list.add(cachedRepo);
  }

}
