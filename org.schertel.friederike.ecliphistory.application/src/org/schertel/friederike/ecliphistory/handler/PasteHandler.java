package org.schertel.friederike.ecliphistory.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.schertel.friederike.ecliphistory.model.ClipboardHistory;
import org.schertel.friederike.ecliphistory.model.ClipboardHistoryEntry;
import org.schertel.friederike.ecliphistory.util.Constants;
import org.schertel.friederike.ecliphistory.util.LogUtility;

/**
 * This handler pastes text from the clipboard history to an editor view. It
 * requires one parameter (PARAMETER_ID) that says which entry of the history
 * should be pasted.
 * 
 * If any error happens this Handler will fail silently.
 * 
 * The configuration for shortcuts is placed in plugin.xml
 */
public class PasteHandler extends AbstractHandler {

	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		LogUtility.debug("execute");
		// How to pass parameters for commands:
		// http://blog.eclipse-tips.com/2008/12/commands-part-3-parameters-for-commands.html
		String positionParameter = event.getParameter(Constants.PARAMETER_ID);
		LogUtility.debug(String.format("positionParameter=%s",
				positionParameter));

		if (null == positionParameter) {
			// abort because no valid parameter given
			throw new ExecutionException(
					"No parameter set. Please set Parameter for "
							+ Constants.PARAMETER_ID);
		}

		int position = Integer.parseInt(positionParameter);
		LogUtility.debug(String.format("position=%d", position));

		ClipboardHistoryEntry historyItem = ClipboardHistory.getInstance()
				.getItem(position);

		if (null == historyItem) {
			// abort because no history item found
			throw new ExecutionException(String.format(
					"No history item found for position %d", position - 1));
		}

		LogUtility
				.debug(String.format("historyItem=%s", historyItem.toString()));

		pasteIntoEditor(historyItem.content);

		return null;
	}

	// Code source:
	// http://wiki.eclipse.org/FAQ_How_do_I_insert_text_in_the_active_text_editor%3F
	private void pasteIntoEditor(String pasteText) throws ExecutionException {
		// Get active Text Editor (if available)
		IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().getActiveEditor();

		if (!(part instanceof AbstractTextEditor)) {
			throw new ExecutionException("Text Editor not found.");
		}
		ITextEditor editor = (ITextEditor) part;
		IDocumentProvider dp = editor.getDocumentProvider();
		IDocument doc = dp.getDocument(editor.getEditorInput());

		ITextSelection textSelection = (ITextSelection) editor.getSite()
				.getSelectionProvider().getSelection();

		// get Position of Cursor in the editor
		int offset = textSelection.getOffset();

		try {
			// replace selected text with pasteText
			doc.replace(offset, textSelection.getLength(), pasteText);
			// position the cursor after the entered text
			editor.selectAndReveal(offset + pasteText.length(), 0);
			editor.setFocus();
		} catch (BadLocationException e) {
			throw new ExecutionException("Could not replace text in editor.", e);
		}

	}

}
