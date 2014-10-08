package fr.univtls2.web.moviesearch.services.indexation.normalization.rules;

import fr.univtls2.web.moviesearch.model.Term;
import fr.univtls2.web.moviesearch.model.builders.TermBuilder;

/**
 * <p>Simple rule that removes the last s, if any.</p>
 * <p>For instance: 'membres' => 'membre'.</p>
 *
 * @author Vianney Dupoy de Guitard
 *
 */
public class NoEndingSRule implements TransformationRule {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Term transform(Term termToTransform) {
		// Removes the accents.
		if (termToTransform.getWord().endsWith("s")) {
			String noSWord = termToTransform.getWord().substring(0, termToTransform.getWord().length());
			Term normalized = new TermBuilder().word(noSWord).sourceDocs(termToTransform.getDocuments()).create();
			return normalized;
		}
		return termToTransform;
	}
}
