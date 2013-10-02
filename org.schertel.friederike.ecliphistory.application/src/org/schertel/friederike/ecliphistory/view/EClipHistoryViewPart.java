package org.schertel.friederike.ecliphistory.view;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.part.ViewPart;
import org.schertel.friederike.ecliphistory.model.ClipboardHistory;
import org.schertel.friederike.ecliphistory.model.ClipboardHistoryEntry;
import org.schertel.friederike.ecliphistory.util.LogUtility;

public class EClipHistoryViewPart extends ViewPart implements Observer {
	private List list;

	public EClipHistoryViewPart() {
		// do nothing
	}

	@Focus
	public void setFocus() {
		LogUtility.debug("setFocus");
		list.setFocus();
	}

	private void updateContent() {
		ClipboardHistory clip = ClipboardHistory.getInstance();
		String[] strings = new String[clip.size()];
		for (ClipboardHistoryEntry entry : clip.getList()) {
			strings[entry.position] = entry.content;
		}
		list.setItems(strings);
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));

		list = new List(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		list.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		ClipboardHistory.getInstance().addObserver(this);

		updateContent();
	}

	@Override
	public void update(Observable o, Object arg) {
		updateContent();
	}

}
