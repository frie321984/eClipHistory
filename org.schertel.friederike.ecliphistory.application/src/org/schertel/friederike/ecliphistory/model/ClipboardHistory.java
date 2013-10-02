package org.schertel.friederike.ecliphistory.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Stack;

import org.schertel.friederike.ecliphistory.util.LogUtility;

/**
 * This is the model that contains all clipboard entries. It can store up to 9
 * entries (MAX_SIZE) and is a Singleton -> so every part of the application
 * gets the same list.
 * 
 * <p>
 * Usage:
 * 
 * <p>
 * 
 * <pre>
 * <code>ClipboardHistory.getInstance().getList();</code>
 * </pre>
 * 
 * For more usage examples see the unit test ClipboardHistoryTest.
 */
public class ClipboardHistory extends Observable {

	/**
	 * Maximum number of entries in the history.
	 */
	static int MAX_SIZE = 9;

	// the history
	private Stack<String> hist;

	// -- Singleton implementation
	private static ClipboardHistory instance;

	/**
	 * Use this method to gain access to the history list.
	 * 
	 * @return history (the one and only)
	 */
	public static ClipboardHistory getInstance() {
		if (null == instance) {
			instance = new ClipboardHistory();
		}
		return instance;
	}

	// note that the constructor is PRIVATE (Singleton pattern)
	private ClipboardHistory() {
		LogUtility.debug("initialize history");
		this.hist = new Stack<String>();
	}

	// -- /Singleton

	/**
	 * Add a new entry to the history. Notifies observers. For usage example see
	 * CopyHandler.
	 * 
	 * @param text
	 */
	public void add(String text) {
		LogUtility.debug("add text: " + text);
		if (this.hist.size() == MAX_SIZE) {
			LogUtility.debug("remove last element");
			this.hist.remove(MAX_SIZE - 1);
		}
		this.hist.add(0, text);
		this.setChanged();
		this.notifyObservers();
		LogUtility.debug("new size: " + this.size());
	}

	/**
	 * Remove all entries from the history. Notifies observers.
	 */
	public void clear() {
		LogUtility.debug("clear history list");
		this.hist.clear();
		this.setChanged();
		this.notifyObservers();
	}

	/**
	 * Get the size of the history.
	 * 
	 * @return
	 */
	public int size() {
		return this.hist.size();
	}

	/**
	 * Get the history as a list of ClipboardHistoryEntries.
	 * 
	 * @return
	 */
	public List<ClipboardHistoryEntry> getList() {
		List<ClipboardHistoryEntry> entries = new ArrayList<>();
		for (int pos = 0; pos < this.hist.size(); pos++) {
			entries.add(new ClipboardHistoryEntry(pos, this.hist.get(pos)));
		}
		return entries;
	}

	/**
	 * Get the item from the history at the given position.
	 * 
	 * @param position
	 * @return
	 */
	public ClipboardHistoryEntry getItem(int position) {
		ClipboardHistoryEntry item = null;
		if (position >= 0 && position < this.hist.size()) {
			String text = this.hist.get(position);
			item = new ClipboardHistoryEntry(position, text);
		}
		return item;
	}

	/**
	 * Remove the entry from the history at the given position.
	 * 
	 * @param position
	 */
	public void removeEntry(int position) {
		if (position < 0 || position > this.hist.size()) {
			// fail silently
			return;
		}
		LogUtility.debug("remove entry " + position);
		this.hist.remove(position);
		this.setChanged();
		this.notifyObservers();
	}
}
