package fr.univtls2.web.moviesearch.services.indexation.normalization.rules;

import fr.univtls2.web.moviesearch.model.Term;

/**
 * <p>Interface that describe a transformation rule.</p>
 * <p>Typically used to transform words, for instance: 'documentation' => 'documen'.</p>
 *
 * @author Vianney Dupoy de Guitard
 */
public interface TransformationRule {

	/**
	 * Transforms a term with a normalization rule.
	 * @param termToTransform : the term to transform.
	 * @return the new transformed term.
	 */
	Term transform(Term termToTransform);
}
