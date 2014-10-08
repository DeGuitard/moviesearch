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
		double completenessSum = 0, completenessSum5 = 0, completenessSum10 = 0, completenessSum25 = 0;
		double precisionSum = 0, precisionSum5 = 0, precisionSum10 = 0, precisionSum25 = 0;

		int i = 1;
		for (Entry<String, List<SourceDoc>> qrel : qrels.entrySet()) {
			// Get expected docs (docs with weight > 0, sorted descending).
			LOGGER.info("-----------------------------------------------");
			LOGGER.info("QUERY no. {}: '{}'.", i, qrel.getKey());
			LOGGER.info("-----------------------------------------------");
			List<SourceDoc> expectedDocs = getExpectedDocs(qrel.getValue());

			// Global scores.
			double completeness = evaluator.exhaustiveScore(qrel.getKey(), expectedDocs);
			double precision = evaluator.precisionScore(qrel.getKey(), expectedDocs);
			double completeness5 = evaluator.exhaustiveScore(qrel.getKey(), expectedDocs, 5);
			double precision5 = evaluator.precisionScore(qrel.getKey(), expectedDocs, 5);
			double completeness10 = evaluator.exhaustiveScore(qrel.getKey(), expectedDocs, 10);
			double precision10 = evaluator.precisionScore(qrel.getKey(), expectedDocs, 10);
			double completeness25 = evaluator.exhaustiveScore(qrel.getKey(), expectedDocs, 25);
			double precision25 = evaluator.precisionScore(qrel.getKey(), expectedDocs, 25);
			LOGGER.info("			   ∑	5	10	25");
			LOGGER.info("-> Precision		: {} / {} / {} / {}", String.format("%.2f", precision),
					String.format("%.2f", precision5), String.format("%.2f", precision10), String.format("%.2f", precision25));
			LOGGER.info("-> Completeness	: {} / {} / {} / {}", String.format("%.2f", completeness),
					String.format("%.2f", completeness5),  String.format("%.2f", completeness10), String.format("%.2f", completeness25));

			// Missing documents.
			List<SourceDoc> missingDocs = evaluator.getMissingDocs(qrel.getKey(), expectedDocs);
			LOGGER.info("-> Missing docs	: {}", getDocUrls(missingDocs));

			// Unexpected documents.
			List<SourceDoc> unexpectedDocs = evaluator.getUnexpectedDocs(qrel.getKey(), expectedDocs);
			LOGGER.info("-> Unexpected docs	: {}", getDocUrls(unexpectedDocs));

			// Sums for average calculation.
			completenessSum += completeness;
			completenessSum5 += completeness5;
			completenessSum10 += completeness10;
			completenessSum25 += completeness25;
			precisionSum += precision;
			precisionSum5 += precision5;
			precisionSum10 += precision10;
			precisionSum25 += precision25;
			i++;
		}
		int sum = i - 1;
		LOGGER.info("-----------------------------------------------");
		LOGGER.info("AVERAGE OF {} REQUESTS", sum);
		LOGGER.info("-----------------------------------------------");
		LOGGER.info("-> Precision		: {} / {} / {} / {}", String.format("%.2f", precisionSum / sum),
				String.format("%.2f", precisionSum5 / sum), String.format("%.2f", precisionSum10 / sum), String.format("%.2f", precisionSum25 / sum));
		LOGGER.info("-> Completeness	: {} / {} / {} / {}", String.format("%.2f", completenessSum / sum),
				String.format("%.2f", completenessSum5 / sum),  String.format("%.2f", completenessSum10 / sum), String.format("%.2f", completenessSum25 / sum));
	}

	/**
	 * @param docs : list of documents' urls to return.
	 * @return the list of the documents urls.
	 */
	private String getDocUrls(List<SourceDoc> docs) {
		StringBuilder bld = new StringBuilder();
		for (SourceDoc doc : docs) {
			bld.append(doc.getUrl());
			bld.append(";");
		}

		return bld.length() > 0 ? bld.substring(0, bld.length() - 1).toString() : bld.toString();
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
