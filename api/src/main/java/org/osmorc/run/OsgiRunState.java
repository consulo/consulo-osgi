/*
 * Copyright (c) 2007-2009, Osmorc Development Team
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright notice, this list
 *       of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright notice, this
 *       list of conditions and the following disclaimer in the documentation and/or other
 *       materials provided with the distribution.
 *     * Neither the name of 'Osmorc Development Team' nor the names of its contributors may be
 *       used to endorse or promote products derived from this software without specific
 *       prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.osmorc.run;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.SwingUtilities;

import org.osmorc.frameworkintegration.CachingBundleInfoProvider;
import org.osmorc.frameworkintegration.FrameworkInstanceDefinition;
import org.osmorc.frameworkintegration.FrameworkIntegrator;
import org.osmorc.frameworkintegration.FrameworkIntegratorUtil;
import org.osmorc.frameworkintegration.FrameworkRunner;
import org.osmorc.run.ui.BundleType;
import org.osmorc.run.ui.SelectedBundle;
import com.intellij.execution.CantRunException;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.JavaCommandLineState;
import com.intellij.execution.configurations.ParametersList;
import com.intellij.execution.filters.TextConsoleBuilderImpl;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessAdapter;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.JavaSdk;
import com.intellij.openapi.projectRoots.JdkUtil;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.packaging.artifacts.Artifact;
import com.intellij.packaging.artifacts.ArtifactManager;
import com.intellij.util.PathsList;
import consulo.java.execution.configurations.OwnJavaParameters;

/**
 * RunState for launching the OSGI framework.
 *
 * @author <a href="mailto:janthomae@janthomae.de">Jan Thom&auml;</a>
 * @author Robert F. Beeger (robert@beeger.net)
 * @version $Id$
 */
public class OsgiRunState extends JavaCommandLineState {
  private final OsgiRunConfiguration runConfiguration;
  private final Project project;
  private final Sdk jdkForRun;
  private SelectedBundle[] mySelectedBundles;
  private final FrameworkRunner runner;
  private static final String FILE_URL_PREFIX = "file:///";

  public OsgiRunState(@Nonnull Executor executor,
                      @Nonnull ExecutionEnvironment env,
                      OsgiRunConfiguration configuration,
                      Project project,
                      Sdk projectJdk) {
    super(env);
    this.runConfiguration = configuration;
    this.project = project;

    if (configuration.isUseAlternativeJre()) {
      String path = configuration.getAlternativeJrePath();
      if (path == null || "".equals(path) || !JdkUtil.checkForJre(path)) {
        this.jdkForRun = null;
      }
      else {
        this.jdkForRun = JavaSdk.getInstance().createJdk("", configuration.getAlternativeJrePath());
      }
    }
    else {
      this.jdkForRun = projectJdk;
    }
    setConsoleBuilder(new TextConsoleBuilderImpl(project));
    FrameworkInstanceDefinition definition = runConfiguration.getInstanceToUse();
    FrameworkIntegrator integrator = FrameworkIntegratorUtil.findIntegratorByInstanceDefinition(definition);
    runner = integrator.createFrameworkRunner();
    runner.init(project, runConfiguration, getRunnerSettings());
  }


  public boolean requiresRemoteDebugger() {
    return runner instanceof ExternalVMFrameworkRunner;
  }

  protected OwnJavaParameters createJavaParameters() throws ExecutionException {
    if (jdkForRun == null) {
      throw CantRunException.noJdkConfigured();
    }
    final OwnJavaParameters params = new OwnJavaParameters();

    params.setWorkingDirectory(runner.getWorkingDir());

    // only add JDK classes to the classpath
    // the rest is is to be provided by bundles
    params.configureByProject(project, OwnJavaParameters.JDK_ONLY, jdkForRun);
    PathsList classpath = params.getClassPath();
    for (VirtualFile libraryFile : runner.getFrameworkStarterLibraries()) {
      classpath.add(libraryFile);
    }


    // get the bundles to be run.
    SelectedBundle[] bundles = getSelectedBundles();
    if (bundles == null) {
      throw new CantRunException(
        "One or more modules seem to be missing their OSGi facets or you have modules in your run configuration that no longer exist. Please re-add the OSGi facets or clean the run configuration and try again.");
    }

    if (runConfiguration.isIncludeAllBundlesInClassPath()) {
      for (SelectedBundle bundle : bundles) {
        String bundlePath = bundle.getBundlePath();

        assert bundlePath != null;

        classpath.add(FileUtil.toSystemIndependentName(bundlePath));
      }
    }

    // setup  the main class
    params.setMainClass(runner.getMainClass());

    // setup the commandline parameters
    final ParametersList programParameters = params.getProgramParametersList();
    runner.fillCommandLineParameters(programParameters, bundles);

    // and the vm parameters
    final ParametersList vmParameters = params.getVMParametersList();
    runner.fillVmParameters(vmParameters, bundles);

    //hide vm options from command line in order to make length predictable
    params.setUseDynamicVMOptions(bundles.length > 0);

    return params;
  }

  /**
   * Here we got the magic. All libs are turned into bundles sorted and returned.
   *
   * @return the sorted list of all bundles to start or null if the selected bundles cannot be collected for some reason.
   */
  @Nullable
  private SelectedBundle[] getSelectedBundles() {

    if (mySelectedBundles == null) {
      ProgressManager.getInstance().run(new Task.Modal(project, "Preparing bundles...", false) {

        public void run(@Nonnull ProgressIndicator progressIndicator) {
          progressIndicator.setIndeterminate(false);
          final HashSet<SelectedBundle> selectedBundles = new HashSet<SelectedBundle>();

          int bundleCount = runConfiguration.getBundlesToDeploy().size();
          for (int i = 0; i < bundleCount; i++) {
            final SelectedBundle selectedBundle = runConfiguration.getBundlesToDeploy().get(i);
            progressIndicator.setFraction(i / bundleCount);

            if(selectedBundle.getBundleType() == BundleType.Artifact) {
              final Artifact artifact = ArtifactManager.getInstance(project).findArtifact(selectedBundle.getName());
              if(artifact == null) {
                showErrorMessage("Artifact '" + selectedBundle.getName() + "' is not found");
                OsgiRunState.this.mySelectedBundles = null;
                return;
              }
              final String outputFilePath = artifact.getOutputFilePath();
              if(outputFilePath == null) {
                showErrorMessage("Artifact '" + selectedBundle.getName() + "' is not builded");
                OsgiRunState.this.mySelectedBundles = null;
                return;
              }

              selectedBundle.setBundlePath(outputFilePath);
              selectedBundles.add(selectedBundle);
            }
            else {
              if (selectedBundles.contains(selectedBundle)) {
                // if the user selected a dependency as runnable library, we need to replace the dependency with
                // the runnable library part
                selectedBundles.remove(selectedBundle);
              }
              selectedBundles.add(selectedBundle);
            }
          }
          HashMap<String, SelectedBundle> finalList = new HashMap<String, SelectedBundle>();

          // filter out bundles which have the same symbolic name
          for (SelectedBundle selectedBundle : selectedBundles) {
            String name = CachingBundleInfoProvider.getBundleSymbolicName(selectedBundle.getBundlePath());
            String version = CachingBundleInfoProvider.getBundleVersions(selectedBundle.getBundlePath());
            String key = name + version;
            if (!finalList.containsKey(key)) {
              finalList.put(key, selectedBundle);
            }
          }

          Collection<SelectedBundle> selectedBundleCollection = finalList.values();
          OsgiRunState.this.mySelectedBundles = selectedBundleCollection.toArray(new SelectedBundle[selectedBundleCollection.size()]);
          Arrays.sort(OsgiRunState.this.mySelectedBundles, new StartLevelComparator());
        }
      });
    }
    return mySelectedBundles;
  }

  private static void showErrorMessage(final String message) {
    try {

      SwingUtilities.invokeAndWait(new Runnable() {
        @Override
        public void run() {
          Messages.showErrorDialog(message, "Error");
        }
      });
    }
    catch (Exception ignore) {
      //ok
    }
  }

  @Nonnull
  protected OSProcessHandler startProcess() throws ExecutionException {
    // run any final configuration steps
    SelectedBundle[] bundles = getSelectedBundles();
    runner.runCustomInstallationSteps(bundles);

    OSProcessHandler handler = super.startProcess();
    handler.addProcessListener(new ProcessAdapter() {
      public void processTerminated(ProcessEvent event) {
        // make sure the runner is disposed when the process exits (so we get rid of the temp folders)
        Disposer.dispose(runner);
      }
    });
    return handler;
  }

  /**
   * Comparator for sorting bundles by their start level.
   *
   * @author <a href="mailto:janthomae@janthomae.de">Jan Thom&auml;</a>
   * @version $Id:$
   */
  public static class StartLevelComparator implements Comparator<SelectedBundle> {
    public int compare(SelectedBundle selectedBundle, SelectedBundle selectedBundle2) {
      return selectedBundle.getStartLevel() - selectedBundle2.getStartLevel();
    }
  }
}
