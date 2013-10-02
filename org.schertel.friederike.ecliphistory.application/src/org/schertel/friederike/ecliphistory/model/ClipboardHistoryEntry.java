package org.schertel.friederike.ecliphistory.model;


/**
 * The POJO describing the entries for the history table.
 */
public class ClipboardHistoryEntry {
	public int position;
	public String content;

	public ClipboardHistoryEntry(int pos, String txt) {
		position = pos;
		content = txt;
	}
}
