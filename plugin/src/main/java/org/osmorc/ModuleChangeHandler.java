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

package org.osmorc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import org.osmorc.run.OsgiConfigurationType;
import org.osmorc.run.OsgiRunConfiguration;
import org.osmorc.run.ui.SelectedBundle;
import com.intellij.ProjectTopics;
import com.intellij.execution.RunManager;
import com.intellij.execution.configurations.ConfigurationTypeUtil;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.ModuleAdapter;
import com.intellij.openapi.project.Project;
import com.intellij.util.messages.MessageBusConnection;

/**
 * @author Robert F. Beeger (robert@beeger.net)
 */
public class ModuleChangeHandler implements ProjectComponent {
  @Nonnull
  private final Project project;
  @Nonnull
  private final Map<Module, String> moduleNames;

  public ModuleChangeHandler(@Nonnull Project project) {
    this.project = project;
    moduleNames = new HashMap<Module, String>();
  }

  public void projectOpened() {
  }

  public void projectClosed() {
  }

  @Nonnull
  public String getComponentName() {
    return "ModuleChangeHandler";
  }

  public void initComponent() {
    MessageBusConnection connection = project.getMessageBus().connect(project);
    connection.subscribe(ProjectTopics.MODULES, new ModuleAdapter() {
      public void moduleAdded(Project project, Module module) {
        if (ModuleChangeHandler.this.project == project) {
          moduleNames.put(module, module.getName());
        }
      }

      public void moduleRemoved(Project project, Module module) {
        if (ModuleChangeHandler.this.project == project) {
          moduleNames.remove(module);
        }
      }

      public void modulesRenamed(Project project, List<Module> modules) {
        assert modules != null;
        if (ModuleChangeHandler.this.project == project) {
          for (Module module : modules) {
            String oldName = moduleNames.get(module);
            if (oldName != null) {
              fireModuleRenamed(module, oldName);
            }
            else if (project.isInitialized()) {
              throw new RuntimeException("Unknown module renamed " + module.getName());
            }
            moduleNames.put(module, module.getName());
          }
        }
      }
    });
  }

  private void fireModuleRenamed(@Nonnull final Module module, @Nonnull String oldName) {
    Project project = module.getProject();
    RunConfiguration[] runConfigurations = RunManager.getInstance(project).getConfigurations(ConfigurationTypeUtil.findConfigurationType(OsgiConfigurationType.class));
    for (RunConfiguration runConfiguration : runConfigurations) {
      OsgiRunConfiguration osgiRunConfiguration = (OsgiRunConfiguration)runConfiguration;
      List<SelectedBundle> bundleList = osgiRunConfiguration.getBundlesToDeploy();
      for (final SelectedBundle selectedBundle : bundleList) {
        if (oldName.equals(selectedBundle.getName())) {
          ApplicationManager.getApplication().runWriteAction(new Runnable() {
            public void run() {
              selectedBundle.setName(module.getName());
            }
          });
          break;
        }
      }
    }
  }

  public void disposeComponent() {
  }
}
