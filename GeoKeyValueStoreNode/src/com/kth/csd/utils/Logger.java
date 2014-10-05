package com.kth.csd.utils;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

public class Logger {

	private static boolean DEBUG = true;
	private static String DEFAULT_ENCODING = "UTF-8";
	private static String LOG_FILE_NAME = "logs.txt";
	private static boolean SHOULD_APPEND_TO_FILE = true;

	private static Calendar mCalendar = Calendar.getInstance();

	private static void debugLog(String tag, String message,
			boolean dumpToFile) {
		if (DEBUG) {
			String logMessage = formatTagAndLogMessage(tag, message);
			if (dumpToFile) {
				writeToLogFile(logMessage, SHOULD_APPEND_TO_FILE);
			} else {
				System.out.println(logMessage);
			}
		}
	}

	private static void errorLog(String tag, String message,
			boolean dumpToFile) {
		if (DEBUG) {
			String logMessage = formatTagAndLogMessage(tag, message);
			if (dumpToFile) {
				writeToLogFile(logMessage, SHOULD_APPEND_TO_FILE);
			} else {
				System.err.println(logMessage);
			}
		}
	}

	private static void writeToLogFile(String logMessage, boolean appendToFile) {
		PrintWriter writer;
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter(
					LOG_FILE_NAME, appendToFile)));
			writer.println(logMessage);
			writer.close();
		} catch (UnsupportedEncodingException | FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String formatTagAndLogMessage(String tag, String message) {
		String date = formatDate(mCalendar.get(Calendar.HOUR),
				mCalendar.get(Calendar.MINUTE), mCalendar.get(Calendar.SECOND));
		return formatLogMessage(date, tag, message);
	}

	private static String formatDate(int hours, int min, int sec) {
		return String.format("%s:%s:%s", hours, min, sec);
	}

	private static String formatLogMessage(String date, String tag,
			String message) {
		return String.format(date + ":::" + tag + ":::" + message);
	}

	/**
	 * Debug Log for file output
	 * 
	 * @param tag
	 *            Tags the log (to ease filtering)
	 * @param message
	 *            the actual message
	 */
	public static void df(String tag, String message) {
		if (DEBUG) {
			debugLog(tag, message, true);
		}
	}

	public static void cf(String tag, String message) {
		if (DEBUG) {
			debugLog(tag, message, true);
		}
	}

	/**
	 * Debug Log for command line output
	 *
	 * @param tag
	 *            Tags the log (to ease filtering)
	 * @param message
	 *            the actual message
	 */
	public static void d(String tag, String message) {
		if (DEBUG) {
			debugLog(tag, message, false);
		}
	}

	/**
	 * Error log.
	 * 
	 * @param tag
	 *            Tags the log (to ease filtering)
	 * @param message
	 *            the actual message
	 */
	public static void e(String tag, String message) {
		if (DEBUG) {
			errorLog(tag, message, false);
		}
	}
}
