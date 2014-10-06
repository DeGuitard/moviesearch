package fr.univtls2.web.moviesearch.services.indexation.normalizer;

import java.util.List;

import fr.univtls2.web.moviesearch.model.Term;

/**
 * <p>Service to stem the words. Uses rules of transformation to transform words into terms.</p>
 * <p>A normalization could, for instance, truncate a word.</p>
 *
 * @author Vianney Dupoy de Guitard
 */
public interface Normalizer {

	/**
	 * <p>Normalizes the terms by applying specific transformation rules.</p>
	 *
	 * @param terms : the terms to normalize.
	 * @return the normalized terms.
	 */
	List<Term> normalize(final List<Term> terms);
}
