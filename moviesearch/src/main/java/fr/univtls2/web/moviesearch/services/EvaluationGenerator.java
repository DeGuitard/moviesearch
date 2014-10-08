package fr.univtls2.web.moviesearch.services;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import fr.univtls2.web.moviesearch.model.SourceDoc;
import fr.univtls2.web.moviesearch.services.evaluator.Evaluator;
import fr.univtls2.web.moviesearch.services.evaluator.QRelLoader;

public class EvaluationGenerator {

	/** Applicative logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(EvaluationGenerator.class);

	/** Evaluator to get statistics. */
	@Inject private Evaluator evaluator;

	/** Loader of QRel files. */
	@Inject private QRelLoader qrelLoader;

	/**
	 * <p>Print stats (completeness & precision) for a set of QRels contained in a directory.</p>
	 * @param directory : the directory to parse.
	 */
	public void printStats(File directory) {
		Map<String, List<SourceDoc>> qrels = qrelLoader.load(directory);
		for (Entry<String, List<SourceDoc>> qrel : qrels.entrySet()) {
			// Get expected docs (docs with weight > 0, sorted descending).
			LOGGER.info("QUERY: '{}'.", qrel.getKey());
			List<SourceDoc> expectedDocs = getExpectedDocs(qrel.getValue());

			// Global scores.
			double completeness = evaluator.exhaustiveScore(qrel.getKey(), expectedDocs);
			double precision = evaluator.precisionScore(qrel.getKey(), expectedDocs);
			LOGGER.info("-> Completeness: {}.", completeness);
			LOGGER.info("-> Precision: {}.", precision);
		}
	}

	/**
	 * @param list : the list to limit.
	 * @param size : amount of items needed.
	 * @return the first size elements of the list.
	 */
	private <T> List<T> getSubList(List<T> list, int size) {
		List<T> subList = list.subList(0, Math.min(size, list.size()));
		return subList;
	}

	/**
	 * @param docs : documents to filter.
	 * @return docs with weight > 0, sorted descending.
	 */
	private List<SourceDoc> getExpectedDocs(List<SourceDoc> docs) {
		List<SourceDoc> expectedDocs = new ArrayList<>();
		for (SourceDoc doc : docs) {
			if (doc.getWeight() > 0) {
				expectedDocs.add(doc);
			}
		}
		Collections.sort(expectedDocs);
		return expectedDocs;
	}
}
