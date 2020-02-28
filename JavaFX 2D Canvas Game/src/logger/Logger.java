/**
 *
 */

package logger;

import java.io.FileWriter;
import java.io.Flushable;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Logger implements AutoCloseable, Flushable {
	private PrintWriter writer;
	private boolean overwrite;
	private List<String> buffer = new ArrayList<>();

	private final String YELLOW = "\\u001b[33m";
	private final String RED = "\\u001b[31m";
	private final String GREEN = "\\u001b[32m";
	private final String RESET = "\\u001b[0m";

	private boolean debug_mode = false;

	public Logger(boolean overwrite) {
		this.overwrite = overwrite;
		try {
			writer = new PrintWriter(new FileWriter("Logs.txt", !overwrite));
		} catch (IOException e) {
			System.out.print(e.getMessage() + " ");
			e.printStackTrace();
		}
	}

	public void error(String e) {
		logToConsole("ERROR: " + e, RED);
	}

	public void warn(String e) {
		logToConsole("WARNING: " + e, YELLOW);
	}

	public void info(String e) {
		logToConsole("INFO: " + e, GREEN);
	}

	public void info(String[] e) {
		StringBuilder sb = new StringBuilder();

		sb.append("{ ");
		for (var s : e) {
			sb.append(s);
			sb.append(", ");
		}
		sb.append(" }");

		this.info(sb.toString());
	}

	public void logToFile(Exception e) {
		for (String s : buffer) {
			writetoFile(s);
		}
	}

	public void writetoFile(String s) {
		try {
			writer.write(s);
			writer.flush();
		} catch (Exception e) {
			error("Cannot export Logger buffer to file");
			e.printStackTrace();
		}
	}

	private void logToConsole(String msg, String color) {
		// Get Date and format it
		DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String d = date.format(now);
		String final_msg = String.format("%s[%s] : %s%s\n", color, d, msg, RESET);

		System.out.printf(final_msg);
		buffer.add(final_msg);
	}

	public void setDebugModeTo(boolean mode) {
		debug_mode = mode;
	}

	public boolean isDebugMode() {
		return debug_mode;
	}

	// Private stuff
	private void write(String label, Exception e) {
		// Get Date and format it
		DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();

		// Get error line number
		StackTraceElement l = e.getStackTrace()[0];

		// Get message
		String msg = e.getMessage();

		try {
			// Format message and write it to file
			msg = String.format("[%s]:\t%s\t--> %s\t@%s::%s()@Line#%s\n", date.format(now), label.toUpperCase(), msg, l.getClassName(), l.getMethodName(), l.getLineNumber());
			writer.write(msg);
		} catch (Exception err) {
			System.out.println("Could not write to file ");
			err.printStackTrace();
		}
	}

	@Override
	public void flush() throws IOException {
		writer.flush();
	}

	@Override
	public void close() throws Exception {
		if (writer != null) {
			writer.close();
		}
	}
}
