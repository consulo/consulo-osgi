package org.osmorc.run;

import com.intellij.debugger.engine.DebuggerUtils;
import com.intellij.debugger.impl.GenericDebuggerRunner;
import com.intellij.debugger.settings.DebuggerSettings;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.*;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.RunContentDescriptor;
import consulo.logging.Logger;

import javax.annotation.Nonnull;

/**
 * Implementation of {@link GenericDebuggerRunner}
 *
 * @author <a href="janthomae@janthomae.de">Jan Thom&auml;</a>
 * @version $Id:$
 */
public class OsgiDebuggerRunner extends GenericDebuggerRunner
{
	private final Logger logger = Logger.getInstance(OsgiDebuggerRunner.class);

	@Override
	public boolean canRun(@Nonnull String executorId, @Nonnull RunProfile profile)
	{
		return executorId.equals(DefaultDebugExecutor.EXECUTOR_ID) && profile instanceof OsgiRunConfiguration;
	}

	@Nonnull
	@Override
	public String getRunnerId()
	{
		return "OsgiDebugRunner";
	}

	@Override
	protected RunContentDescriptor createContentDescriptor(@Nonnull RunProfileState state, @Nonnull ExecutionEnvironment env) throws ExecutionException
	{
		OsgiRunState runState = (OsgiRunState) state;
		final RunnerSettings myRunnerSettings = ((OsgiRunState) state).getRunnerSettings();

		if(runState.requiresRemoteDebugger())
		{
			// this is actually copied from the default, but well
			String myDebugPort = null;
			if(myRunnerSettings instanceof DebuggingRunnerData)
			{
				myDebugPort = ((DebuggingRunnerData) myRunnerSettings).getDebugPort();
				if(myDebugPort.length() == 0)
				{
					try
					{
						myDebugPort = DebuggerUtils.getInstance().findAvailableDebugAddress(DebuggerSettings.SOCKET_TRANSPORT).address();
					}
					catch(ExecutionException e)
					{
						logger.error(e);
					}
					((DebuggingRunnerData) myRunnerSettings).setDebugPort(myDebugPort);
				}
				((DebuggingRunnerData) myRunnerSettings).setLocal(false);
			}
			final RemoteConnection connection = new RemoteConnection(true, "127.0.0.1", myDebugPort, true);
			return attachVirtualMachine(state, env, connection, false);
		}
		else
		{
			// let the default debugger do it's job.
			return super.createContentDescriptor(state, env);
		}
	}
}
