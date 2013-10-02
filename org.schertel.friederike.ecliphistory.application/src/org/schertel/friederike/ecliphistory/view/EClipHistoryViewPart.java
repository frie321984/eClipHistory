package org.schertel.friederike.ecliphistory.view;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;
import org.schertel.friederike.ecliphistory.model.ClipboardHistory;
import org.schertel.friederike.ecliphistory.model.ClipboardHistoryEntry;
import org.schertel.friederike.ecliphistory.util.LogUtility;

public class EClipHistoryViewPart extends ViewPart implements Observer {

	private Label lblHistory;
	private Label lblLatestEntry;
	private Label lblNumberOfEntries;
	private Label lblEntries;
	private Button btnUpdate;

	public EClipHistoryViewPart() {
		// do nothing
	}

	@Focus
	public void setFocus() {
		LogUtility.debug("setFocus");
		lblHistory.setFocus();
	}

	private void updateContent() {
		lblNumberOfEntries.setText(String.format("%d", ClipboardHistory
				.getInstance().size()));
		lblNumberOfEntries.pack();

		ClipboardHistoryEntry latestEntry = ClipboardHistory.getInstance()
				.getItem(0);
		lblLatestEntry.setText("");
		if (latestEntry != null) {
			lblLatestEntry.setText(latestEntry.content);
		}
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(2, false));

		lblHistory = new Label(parent, SWT.NONE);
		GridData gd_lblHistory = new GridData(SWT.FILL, SWT.CENTER, true,
				false, 2, 1);
		gd_lblHistory.widthHint = 442;
		lblHistory.setLayoutData(gd_lblHistory);
		lblHistory.setText("History");

		lblEntries = new Label(parent, SWT.NONE);
		lblEntries.setText("Entries: ");

		lblNumberOfEntries = new Label(parent, SWT.NONE);
		lblNumberOfEntries.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER,
				true, false, 1, 1));
		lblNumberOfEntries.setText("0");

		lblLatestEntry = new Label(parent, SWT.NONE);
		lblLatestEntry.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 2, 1));
		lblLatestEntry.setText("[latest entry]");
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);

		btnUpdate = new Button(parent, SWT.NONE);
		btnUpdate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateContent();
			}
		});
		btnUpdate.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true,
				false, 2, 1));
		btnUpdate.setText("Update");
		
		ClipboardHistory.getInstance().addObserver(this);

		updateContent();
	}

	@Override
	public void update(Observable o, Object arg) {
		updateContent();
	}

}
