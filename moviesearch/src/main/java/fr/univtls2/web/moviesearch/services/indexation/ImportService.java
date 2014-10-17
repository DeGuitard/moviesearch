package fr.univtls2.web.moviesearch.services.indexation;

import java.io.File;

public interface ImportService {

	/**
	 * Directory containing the files to be indexed.
	 * 
	 * @param directory
	 *            where found the html files to index.
	 */
	void start(final File directory);
}
