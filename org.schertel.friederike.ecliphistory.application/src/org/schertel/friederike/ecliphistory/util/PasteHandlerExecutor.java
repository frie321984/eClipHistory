package org.schertel.friederike.ecliphistory.util;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.SerializationException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;

/**
 * This utility class can be used by any IWorkbenchPartSite to execute the
 * PasteHandler.
 * 
 * <p>
 * Handler should not be executed directly but always using the IHandlerService
 * of the eclipse framework.
 * 
 * <p>
 * See the official docu site for more information:
 * http://wiki.eclipse.org/Platform_Command_Framework
 * 
 * <p>
 * The PasteHandler (in the handler package) uses a parameter that must be set.
 * This executor encapsulates that functionality with the parameter calling and
 * all so you can just use <code>pasteHistoryItem(itemId)</code>
 * 
 * 
 * <p>
 * Example:
 * 
 * <p>
 * <code>(new PasteHandlerExecutor(getSite())).pasteHistoryItem(2)</code>
 * 
 * <p>
 * This will call the PasteHandler with the position parameter value "2". So the
 * second entry from the ClipboardHistory will be added in the active
 * TextEditor.
 * 
 */
public class PasteHandlerExecutor {

	private IHandlerService handlerService;
	private ICommandService commandService;

	/**
	 * Set up the HandlerExecutor using getSite() in your current WorkbenchPart.
	 * 
	 * @param site
	 *            This is needed to get the handlerService and commandService by
	 *            the eclipse framework.
	 */
	public PasteHandlerExecutor(IWorkbenchPartSite site) {
		handlerService = (IHandlerService) site
				.getService(IHandlerService.class);
		commandService = (ICommandService) site
				.getService(ICommandService.class);
	}

	/**
	 * This executes the PasteHandler with the given position as parameter.
	 * 
	 * This method will fail silently if the execution fails.
	 * 
	 * @param position
	 * @throws ExecutionException
	 *             if the PasteHandler could not be executed
	 */
	public void pasteHistoryItem(int position) throws ExecutionException {
		String commandString = String.format("%s(%s=%d)", Constants.COMMAND_ID,
				Constants.PARAMETER_ID, position);
		try {
			ParameterizedCommand parametrizedCmd = commandService
					.deserialize(commandString);
			handlerService.executeCommand(parametrizedCmd, null);
		} catch (NotDefinedException | SerializationException
				| NotEnabledException | NotHandledException e) {
			LogUtility.warn(e.getMessage());
		}
	}

}
