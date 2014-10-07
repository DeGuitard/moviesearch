package fr.univtls2.web.moviesearch.services.evaluator;

import java.util.List;

import com.google.inject.Inject;

import fr.univtls2.web.moviesearch.model.SourceDoc;
import fr.univtls2.web.moviesearch.services.query.QueryExecutor;

/**
 * <p>Simple implementation of {@link Evaluator}.</p>
 *
 * @author Vianney Dupoy de Guitard
 */
public class EvaluatorImpl implements Evaluator {

	/** The query executor to evaluate. */
	@Inject private QueryExecutor executor;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double exhaustiveScore(String query, List<SourceDoc> expectedDocs) {
		List<SourceDoc> results = executor.execute(query);
		int pertinentDocsCount = expectedDocs.size();
		return getPertinentDocsFoundCount(expectedDocs, results) / pertinentDocsCount;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double precisionScore(String query, List<SourceDoc> expectedDocs) {
		List<SourceDoc> results = executor.execute(query);
		double pertinentDocsFound = getPertinentDocsFoundCount(expectedDocs, results);
		return pertinentDocsFound / results.size();
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
		return pertinentDocsFound;
	}
}
