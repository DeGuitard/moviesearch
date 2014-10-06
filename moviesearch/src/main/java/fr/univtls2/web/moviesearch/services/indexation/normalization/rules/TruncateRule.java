package fr.univtls2.web.moviesearch.services.indexation.normalization.rules;

import fr.univtls2.web.moviesearch.model.Term;
import fr.univtls2.web.moviesearch.model.builders.TermBuilder;

/**
 * <p>Simple rule that troncate a term to a fixed length.</p>
 * <p>For instance: 'documentation' => 'documen'.</p>
 *
 * @author Vianney Dupoy de Guitard
 *
 */
public class TruncateRule implements TransformationRule {

	/** Term maximum length. **/
	private static final int MAX_LENGTH = 7;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Term transform(Term termToTransform) {
		// Sets the maximum length.
		int termLength = termToTransform.getWord().length();
		int length = termLength > MAX_LENGTH ? MAX_LENGTH : termLength;

		// Truncates the word.
		String shortWord = termToTransform.getWord().substring(0, length);
		Term normalized = new TermBuilder().word(shortWord).sourceDocs(termToTransform.getDocuments()).create();

		return normalized;
	}
}
