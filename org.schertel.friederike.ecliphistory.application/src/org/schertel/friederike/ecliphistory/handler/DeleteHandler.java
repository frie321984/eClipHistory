package org.schertel.friederike.ecliphistory.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.schertel.friederike.ecliphistory.model.ClipboardHistory;
import org.schertel.friederike.ecliphistory.model.ClipboardHistoryEntry;
import org.schertel.friederike.ecliphistory.util.LogUtility;

public class DeleteHandler extends AbstractHandler {


	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		LogUtility.setLevel(LogUtility.LOG_DEBUG);
		LogUtility.debug(getClass().getSimpleName() + ".execute() called");

		ClipboardHistoryEntry entry = getSelection(event);

		if (entry != null) {
			LogUtility.debug("deleting entry #" + entry.position);
			ClipboardHistory.getInstance().removeEntry(entry.position);
		}

		return null;

	}

	// Can not inject ESelectionService due to bug:
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=376486
	// So we need this nasty workaround
	private ClipboardHistoryEntry getSelection(ExecutionEvent event) {
		ISelection curSel = HandlerUtil.getCurrentSelection(event);
		IStructuredSelection currentSelection = (IStructuredSelection) curSel;

		if (null == currentSelection) {
			return null;
		}

		Object firstSelection = currentSelection.getFirstElement();
		if (null == firstSelection) {
			return null;
		}

		LogUtility.debug("First selected element: " + firstSelection);

		if (!(firstSelection instanceof ClipboardHistoryEntry)) {
			return null;
		}

		return (ClipboardHistoryEntry) firstSelection;
	}
}