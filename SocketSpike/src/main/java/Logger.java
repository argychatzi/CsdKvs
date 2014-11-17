package main.java;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import com.sun.swing.internal.plaf.synth.resources.synth;

public class Logger {

	private static boolean DEBUG = true;
	private static String DEFAULT_ENCODING = "UTF-8";
	private static String LOG_FILE_NAME = "logs.txt";
	private static boolean SHOULD_APPEND_TO_FILE = true;

	private static Calendar mCalendar = Calendar.getInstance();

	private synchronized static void debugLog(String tag, String message,
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

	private synchronized static void errorLog(String tag, String message,
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

	private synchronized static void writeToLogFile(String logMessage, boolean appendToFile) {
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

	private synchronized static String formatTagAndLogMessage(String tag, String message) {
		mCalendar = Calendar.getInstance();
		String date = formatDate(mCalendar.get(Calendar.HOUR),
				mCalendar.get(Calendar.MINUTE), mCalendar.get(Calendar.SECOND), mCalendar.get(Calendar.MILLISECOND));
		return formatLogMessage(date, tag, message);
	}

	private synchronized static String formatDate(int hours, int min, int sec, int nano) {
		return String.format("%s:%s:%s:%s", hours, min, sec, nano);
	}

	private synchronized static String formatLogMessage(String date, String tag,
			String message) {
		return date + ":::" + tag + ":::" + message;
	}

	/**
	 * Debug Log for file output
	 * 
	 * @param tag
	 *            Tags the log (to ease filtering)
	 * @param message
	 *            the actual message
	 */
	public synchronized static void df(String tag, String message) {
		if (DEBUG) {
			debugLog(tag, message, true);
		}
	}

	public synchronized static void cf(String tag, String message) {
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
	public synchronized static void d(String tag, String message) {
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
	public synchronized static void e(String tag, String message) {
		if (DEBUG) {
			errorLog(tag, message, false);
		}
	}
}
