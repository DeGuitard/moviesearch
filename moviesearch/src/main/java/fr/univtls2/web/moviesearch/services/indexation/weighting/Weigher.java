package fr.univtls2.web.moviesearch.services.indexation.weighting;

import fr.univtls2.web.moviesearch.model.SourceDoc;
import fr.univtls2.web.moviesearch.model.Term;

/**
 * <p>Weights a document for a specific term, by updating its weight value.</p>
 * <p>It can apply several weighting rules to provide a specific weight.</p>
 *
 * @author Vianney Dupoy de Guitard
 */
public interface Weigher {

	/**
	 * Weights a given document for a specific term.
	 * @param doc : the document to weight.
	 * @param term : the associated term.
	 */
	void weight(final SourceDoc doc, final Term term);
}
