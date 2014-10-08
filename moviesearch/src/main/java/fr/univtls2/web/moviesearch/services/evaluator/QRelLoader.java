package fr.univtls2.web.moviesearch.services.evaluator;

import java.io.File;
import java.util.List;
import java.util.Map;

import fr.univtls2.web.moviesearch.model.SourceDoc;

public interface QRelLoader {

	/**
	 * Load the qrel files in a list of SourceDoc.
	 * 
	 * @param directory
	 *            where we can find the qrel files
	 * @return the list of SourceDoc loaded with the qrel files
	 */
	Map<String, List<SourceDoc>> load(final File directory);
}
