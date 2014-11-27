package fr.univtls2.web.moviesearch.stat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrintStat {
	private static final Logger LOGGER = LoggerFactory.getLogger(PrintStat.class);

	private List<RowCSV> rowCSVs = new ArrayList<RowCSV>();

	private File fileOut;

	/**
	 * Manage the printing the stat in a file. The csv use the separator ,
	 * @param fileOut the absolute path to write the data
	 */
	public PrintStat(File fileOut) {
		this.fileOut = fileOut;
	}

	public List<RowCSV> getRowCSVs() {
		return rowCSVs;
	}

	public void write() {
		try {
			FileUtils.writeLines(fileOut, rowCSVs);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());

		}
	}
}
