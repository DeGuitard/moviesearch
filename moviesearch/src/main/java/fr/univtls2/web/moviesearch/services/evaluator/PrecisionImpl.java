package fr.univtls2.web.moviesearch.services.evaluator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.univtls2.web.moviesearch.model.SourceDoc;
import fr.univtls2.web.moviesearch.model.builders.SourceDocBuilder;

public class PrecisionImpl implements Precision {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(PrecisionImpl.class);

	@Override
	public List<SourceDoc> loading(final File directory) {
		// All SourceDocs loaded
		final List<SourceDoc> allSourceDocs = new ArrayList<SourceDoc>();

		if (!directory.isDirectory()) {
			LOGGER.error("No directory supplied.");
			throw new IllegalArgumentException("No directory supplied.");
		}

		LOGGER.info("Full of '{}' import starting.",
				directory.getAbsolutePath());
		float progress = 0;
		float filesCount = directory.listFiles().length;

		for (File file : directory.listFiles()) {
			allSourceDocs.addAll(readLines(file));
			progress++;
			LOGGER.info("{}% done.", (progress / filesCount) * 100f);
		}

		return allSourceDocs;
	}

	/**
	 * Read the lines of the file open.
	 * 
	 * @param sourceDocs
	 * @param file
	 */
	@SuppressWarnings("unchecked")
	private final List<SourceDoc> readLines(final File file) {
		final List<SourceDoc> sourceDocs = new ArrayList<SourceDoc>();
		List<String> lines = null;
		try {
			lines = FileUtils.readLines(file);
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}

		boolean first = false;
		for (String line : lines) {
			// we don't read the first line because it's the request.
			if (first) {
				final String[] tab = line.split("\\s");
				final SourceDoc doc = new SourceDocBuilder().url(tab[0])
						.weight(new Double(tab[1].replace(',', '.'))).create();
				sourceDocs.add(doc);
			} else {
				first = true;
			}
		}
		return sourceDocs;
	}

}
