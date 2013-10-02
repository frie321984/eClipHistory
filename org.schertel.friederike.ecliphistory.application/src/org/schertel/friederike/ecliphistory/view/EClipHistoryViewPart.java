package org.schertel.friederike.ecliphistory.view;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.part.ViewPart;
import org.schertel.friederike.ecliphistory.model.ClipboardHistory;
import org.schertel.friederike.ecliphistory.model.ClipboardHistoryEntry;
import org.schertel.friederike.ecliphistory.util.LogUtility;
import org.schertel.friederike.ecliphistory.util.PasteHandlerExecutor;

public class EClipHistoryViewPart extends ViewPart implements Observer {
	private ListViewer viewer;

	public EClipHistoryViewPart() {
		// do nothing
	}

	@Focus
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));

		viewer = new ListViewer(parent);
		viewer.getControl().setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		viewer.setContentProvider(new ClipContentProvider());
		viewer.setLabelProvider(new ClipLabelProvider());
		viewer.setInput(ClipboardHistory.getInstance());

		hookRightclickMenu();
		hookDoubleClickOnTable();

		ClipboardHistory.getInstance().addObserver(this);

	}

	private void hookDoubleClickOnTable() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				// get the selected row
				IStructuredSelection selection = (IStructuredSelection) viewer
						.getSelection();
				ClipboardHistoryEntry firstElement = (ClipboardHistoryEntry) selection
						.getFirstElement();

				if (null == firstElement) {
					// abort if empty line selected
					return;
				}

				// do something with the selected element
				try {
					(new PasteHandlerExecutor(getSite()))
							.pasteHistoryItem(firstElement.position);
				} catch (ExecutionException e) {
					// log error
					LogUtility.error(e.getMessage());
				}
			}
		});
	}

	/**
	 * This method tells the table viewer that it is able to add a rightclick
	 * (popup) menu. IF a menu is actually added is defined in the plugin.xml
	 * file under menuContributions.
	 */
	private void hookRightclickMenu() {
		MenuManager menuManager = new MenuManager();
		Menu menu = menuManager.createContextMenu(this.viewer.getControl());
		this.viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuManager, this.viewer);
		getSite().setSelectionProvider(this.viewer);
	}

	@Override
	public void update(Observable o, Object arg) {
		viewer.refresh();
	}

	private class ClipContentProvider implements IStructuredContentProvider {

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// nothing todo
		}

		@Override
		public void dispose() {
			// nothing todo
		}

		@Override
		public Object[] getElements(Object inputElement) {
			ClipboardHistory clip = (ClipboardHistory) inputElement;
			return clip.getList().toArray();
		}

	}

	private class ClipLabelProvider extends LabelProvider {
		@Override
		public String getText(Object element) {
			ClipboardHistoryEntry clipboardEntry = (ClipboardHistoryEntry) element;
			return String.format("(%d) \"%s\"", clipboardEntry.position,
					clipboardEntry.content);
		}
	}
}
