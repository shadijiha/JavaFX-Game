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

public class Logger implements AutoCloseable, Flushable {
	private PrintWriter writer;
	private boolean overwrite;

	public Logger(boolean overwrite) {
		this.overwrite = overwrite;
		try {
			writer = new PrintWriter(new FileWriter("Logs.txt", !overwrite));
		} catch (IOException e) {
			System.out.print(e.getMessage() + " ");
			e.printStackTrace();
		}
	}

	public void error(Exception e) {
		write("error", e);
	}

	public void info(Exception e) {
		write("info", e);
	}

	public void log(Exception e) {
		this.info(e);
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
