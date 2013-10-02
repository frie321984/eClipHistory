package org.schertel.friederike.ecliphistory;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IExecutionListener;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.schertel.friederike.ecliphistory.model.ClipboardHistory;
import org.schertel.friederike.ecliphistory.util.LogUtility;

public class EClipHistoryActivator implements BundleActivator, IStartup {

	private Command defaultCopyCommand = null;
	private IExecutionListener executionListener = null;

	public EClipHistoryActivator() {
		// do nothing
	}

	public void start(BundleContext context) throws Exception {

		LogUtility.info("Start EClipHistory Plugin");

		// add COPY listener
		ICommandService commandService = (ICommandService) PlatformUI
				.getWorkbench().getAdapter(ICommandService.class);

		if (null == commandService) {
			LogUtility.error("Failed to get commandService.");
			throw new Exception("Failed to get commandService.");
		}

		defaultCopyCommand = commandService
				.getCommand(org.eclipse.ui.IWorkbenchCommandConstants.EDIT_COPY);

		executionListener = new IExecutionListener() {

			@Override
			public void preExecute(String commandId, ExecutionEvent event) {
				// do nothing
			}

			@Override
			public void postExecuteSuccess(String commandId, Object returnValue) {
				// something was added to the clipboard!
				Clipboard clipboard = Toolkit.getDefaultToolkit()
						.getSystemClipboard();
				try {

					// get text from the clipboard and save it in our history
					String stringData = (String) clipboard
							.getData(DataFlavor.stringFlavor);
					ClipboardHistory.getInstance().add(stringData);

				} catch (UnsupportedFlavorException | IOException exception) {
					// can not copy - ignore and abort
					LogUtility.error(exception.getMessage());
				}
			}

			@Override
			public void postExecuteFailure(String commandId,
					ExecutionException exception) {
				LogUtility.error(exception.getMessage());
			}

			@Override
			public void notHandled(String commandId,
					NotHandledException exception) {
				LogUtility.error(exception.getMessage());
			}

		};

		defaultCopyCommand.addExecutionListener(executionListener);
	}

	public void stop(BundleContext context) throws Exception {

		LogUtility.info("Stop EClipHistory plugin");

		// remove listener
		if (null != defaultCopyCommand) {
			defaultCopyCommand.removeExecutionListener(executionListener);
		}
	}

	@Override
	public void earlyStartup() {
		// do nothing. start method is called automatically
	}
}
