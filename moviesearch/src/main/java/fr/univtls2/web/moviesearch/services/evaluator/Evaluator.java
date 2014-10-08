package fr.univtls2.web.moviesearch.services.evaluator;

import java.util.List;

import fr.univtls2.web.moviesearch.model.SourceDoc;

/**
 * <p>An evaluator is used to produce two statistics:</p>
 * <ul><li>Completeness</li>
 * <li>Precision</li></ul>
 *
 * @author Vianney Dupoy de Guitard
 */
public interface Evaluator {

	/**
	 * <p>Completeness indicator of the results.</p>
	 * @param query : the query to execute.
	 * @param expectedDocs : the expected documents.
	 * @return the completeness score.
	 */
	double exhaustiveScore(String query, List<SourceDoc> expectedDocs);

	/**
	 * <p>Precision indicator of the results.</p>
	 * @param query : the query to execute.
	 * @param expectedDocs : the expected documents.
	 * @return the precision score.
	 */
	double precisionScore(String query, List<SourceDoc> expectedDocs);

	/**
	 * <p>Completeness indicator of the n first results.</p>
	 * @param query : the query to execute.
	 * @param expectedDocs : the expected documents.
	 * @param n : amount of docs to count
	 * @return the completeness score.
	 */
	double exhaustiveScore(String query, List<SourceDoc> expectedDocs, int n);

	/**
	 * <p>Precision indicator of the n first results.</p>
	 * @param query : the query to execute.
	 * @param expectedDocs : the expected documents.
	 * @param n : amount of docs to count.
	 * @return the precision score.
	 */
	double precisionScore(String query, List<SourceDoc> expectedDocs, int n);
}
