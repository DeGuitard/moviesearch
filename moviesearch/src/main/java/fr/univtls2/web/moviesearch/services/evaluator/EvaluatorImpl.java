package fr.univtls2.web.moviesearch.services.evaluator;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import fr.univtls2.web.moviesearch.model.SourceDoc;
import fr.univtls2.web.moviesearch.services.query.QueryExecutor;

/**
 * <p>Simple implementation of {@link Evaluator}.</p>
 *
 * @author Vianney Dupoy de Guitard
 */
public class EvaluatorImpl implements Evaluator {

	/** Applicative logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(EvaluatorImpl.class);

	/** The query executor to evaluate. */
	@Inject private QueryExecutor executor;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double exhaustiveScore(String query, List<SourceDoc> expectedDocs) {
		return exhaustiveScore(query, expectedDocs, expectedDocs.size());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double precisionScore(String query, List<SourceDoc> expectedDocs) {
		return precisionScore(query, expectedDocs, expectedDocs.size());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double exhaustiveScore(String query, List<SourceDoc> expectedDocs, int n) {
		List<SourceDoc> results = getSubList(executor.execute(query), n);
		expectedDocs = getSubList(expectedDocs, n);
		double pertinentDocsFoundCount = getPertinentDocsFoundCount(expectedDocs, results);
		return pertinentDocsFoundCount / expectedDocs.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double precisionScore(String query, List<SourceDoc> expectedDocs, int n) {
		List<SourceDoc> results = getSubList(executor.execute(query), n);
		expectedDocs = getSubList(expectedDocs, n);
		double pertinentDocsFoundCount = getPertinentDocsFoundCount(expectedDocs, results);
		return pertinentDocsFoundCount / results.size();
	}

	/**
	 * @param list : the list to limit.
	 * @param size : amount of items needed.
	 * @return the first size elements of the list.
	 */
	private <T> List<T> getSubList(List<T> list, int size) {
		if (size >= list.size()) {
			return list;
		}
		return list.subList(0, size);
	}

	/**
	 * <p>Return the amount of pertinent documents found.</p>
	 * @param expectedDocs : the expected documents.
	 * @param results : the documents found in the database.
	 * @return the amount of found documents that were expected.
	 */
	private double getPertinentDocsFoundCount(List<SourceDoc> expectedDocs, List<SourceDoc> results) {
		double pertinentDocsFound = 0;
		for (SourceDoc expectedDoc : expectedDocs) {
			if (results.contains(expectedDoc)) {
				pertinentDocsFound++;
			}
		}
		LOGGER.debug("Pertinent docs found: {}", pertinentDocsFound);
		return pertinentDocsFound;
	}
}
