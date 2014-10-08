package fr.univtls2.web.moviesearch.services.evaluator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.univtls2.web.moviesearch.model.SourceDoc;
import fr.univtls2.web.moviesearch.model.builders.SourceDocBuilder;

public class QRelLoaderImpl implements QRelLoader {
	private static final Logger LOGGER = LoggerFactory.getLogger(QRelLoaderImpl.class);

	@Override
	public Map<String, List<SourceDoc>> load(final File directory) {
		// All SourceDocs loaded
		final Map<String, List<SourceDoc>> qrels = new LinkedHashMap<>();

		if (!directory.isDirectory()) {
			LOGGER.error("No directory supplied.");
			throw new IllegalArgumentException("No directory supplied.");
		}

		LOGGER.info("Importing QRels from '{}'.", directory.getAbsolutePath());
		float progress = 0;
		float filesCount = directory.listFiles().length;

		for (File file : directory.listFiles()) {
			qrels.putAll(readLines(file));
			progress++;
			LOGGER.info("{}% done.", (progress / filesCount) * 100f);
		}

		return qrels;
	}

	/**
	 * Read the lines of the file open.
	 * 
	 * @param sourceDocs
	 * @param file
	 */
	@SuppressWarnings("unchecked")
	private Map<String, List<SourceDoc>> readLines(final File file) {
		final Map<String, List<SourceDoc>> qrel = new HashMap<>();
		String query = null;
		try {
			List<String> lines = FileUtils.readLines(file);
			boolean first = true;
			for (String line : lines) {
				// the first line is the request.
				if (first) {
					query = line;
					qrel.put(query, new ArrayList<SourceDoc>());
					first = false;
				} else {
					final String[] splittedLine = line.split("\\s");
					String url = splittedLine[0];
					double weight = new Double(splittedLine[1].replace(',', '.'));
					SourceDoc doc = new SourceDocBuilder().url(url).weight(weight).create();
					qrel.get(query).add(doc);
				}
			}
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}
		return qrel;
	}

}
