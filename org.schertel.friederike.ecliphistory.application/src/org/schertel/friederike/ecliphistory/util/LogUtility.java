package org.schertel.friederike.ecliphistory.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogUtility {
	private static final LogUtility INSTANCE = new LogUtility();
	public static final int LOG_DEBUG = 4;
	public static final int LOG_INFO = 3;
	public static final int LOG_WARN = 2;
	public static final int LOG_ERROR = 1;
	private int logLevel;

	private LogUtility() {
		logLevel = LOG_ERROR;
	}

	public void log(int logLevel, String logMessage) {

		boolean logging = LogUtility.isLogging(logLevel);
		if (logging == false) {
			return;
		}

		String levelDescription = getLevelDescription(logLevel);
		SimpleDateFormat formatter = new SimpleDateFormat("d.M.Y H:mm:ss");

		String message = String.format("[%s] %5s: \"%s\"",
				formatter.format(new Date()), levelDescription, logMessage);

		// show message
		switch (logLevel) {

		case LogUtility.LOG_ERROR:
		case LogUtility.LOG_WARN:
			System.err.println(message);
			break;

		case LogUtility.LOG_DEBUG:
		case LogUtility.LOG_INFO:
		default:
			System.out.println(message);
			break;

		}
	}

	private String getLevelDescription(int logLevel) {
		switch (logLevel) {
		case LogUtility.LOG_ERROR:
			return "ERROR";

		case LogUtility.LOG_WARN:
			return "WARN";

		case LogUtility.LOG_DEBUG:
			return "DEBUG";

		case LogUtility.LOG_INFO:
			return "INFO";
		}
		return null;
	}

	/**
	 * Query the current log level.
	 * 
	 * @param level
	 *            The level to compare against.
	 * @return True if the log level is equal or greater than the specified
	 *         level, otherwise false.
	 */
	private static boolean isLogging(int level) {
		LogUtility instance = LogUtility.getInstance();
		int threshold = instance.getLogLevel();
		boolean logging = threshold >= level;
		return logging;
	}

	private int getLogLevel() {
		return this.logLevel;
	}

	private void setLogLevel(int level) {
		this.logLevel = level;
	}

	/**
	 * Public getter for the <code>LogUtility</code> singleton instance.
	 * 
	 * @return The <code>LogUtility</code> singleton instance.
	 */
	public static LogUtility getInstance() {
		return LogUtility.INSTANCE;
	}

	public static void info(String text) {
		LogUtility.getInstance().log(LOG_INFO, text);
	}

	public static void warn(String text) {
		LogUtility.getInstance().log(LOG_WARN, text);
	}

	public static void error(String text) {
		LogUtility.getInstance().log(LOG_ERROR, text);
	}

	public static void debug(String text) {
		LogUtility.getInstance().log(LOG_DEBUG, text);
	}

	public static void setLevel(int level) {
		LogUtility.getInstance().setLogLevel(level);
	}
}
