/**
 * Reads a .shado or .txt file and extracts the attributs and their data
 * <p>
 * NOTE: The file must be in the following format:
 * <p>
 * attribute: data
 * anotherAttribute: anotherData
 */

package dataGetters;

import logger.Logger;
import sample.Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ReadShadoFile {

	private List<List<String>> data = new ArrayList<>();
	private String filename;
	private Logger logger;

	public ReadShadoFile(String filename, Logger logger) {
		this.filename = filename;
		this.logger = logger;
		readDataFile();
	}

	private void readDataFile() {

		List<String> temp = new ArrayList<>();

		// Read the file
		try {
			Scanner reader = new Scanner(new File(filename));

			// Read every line of the file
			while (reader.hasNextLine()) {
				temp.add(reader.nextLine());
			}

			// Separate attributes from data
			for (String s : temp) {
				String[] sperated = s.split(":");

				// Rmove comments
				for (int i = 0; i < sperated.length; i++)
					sperated[i] = sperated[i].replaceAll("\\/\\/.*", "");

				data.add(Arrays.asList(sperated));
			}

		} catch (FileNotFoundException e) {
			Main.LOGGER.error("Cannot open " + filename + ". File doesn't exist");
		}
	}

	public String get(String attribute, boolean removeWhiteSpace) {

		// Find the element the matches the attribute
		List<List<String>> result = data.stream()
				.filter(e -> e.get(0).equals(attribute))
				.collect(Collectors.toList());

		// If no element matches the attribute passed
		if (result.size() == 0) {
			logger.error("No element matches the given attribute: " + attribute);
			return null;
		}

		// Return only the first result of the search without the attribute itself (for example, hp: 80 --> will return; 80)
		String temp = result.get(0).get(1).replaceAll("\"", "");
		if (removeWhiteSpace)
			return temp.replaceAll("\\s", "");
		else
			return temp;
	}

	public String get(String attribute) {
		return get(attribute, false);
	}
}
