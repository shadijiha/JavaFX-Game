/**
 *
 */

package dataGetters;

import logger.Logger;

import java.io.FileReader;
import java.util.Scanner;

public class ReadShadoFile {

	private String filename;
	private Logger logger;

	public ReadShadoFile(String filename, Logger logger) {
		this.filename = filename;
		this.logger = logger;
		readDataFile();
	}

	private void readDataFile() {

		try {
			Scanner scan = new Scanner(new FileReader(filename));


			scan.close();
		} catch (Exception e) {
			logger.writetoFile(e.getMessage());
		}

	}
}
