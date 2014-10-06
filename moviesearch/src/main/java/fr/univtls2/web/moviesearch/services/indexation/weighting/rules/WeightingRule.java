package fr.univtls2.web.moviesearch.services.indexation.weighting.rules;

import fr.univtls2.web.moviesearch.model.SourceDoc;
import fr.univtls2.web.moviesearch.model.Term;

/**
 * <p>Describe a rule that given a document and a term can give a weight.</p>
 *
 * @author Vianney Dupoy de Guitard
 */
public interface WeightingRule {

	/**
	 * <p>Weights a term given its information and the collection it comes from.</p>
	 * @param docToWeight : the document to weight.
	 * @param term : the term associated to this document.
	 * @return a weight.
	 */
	double weight(SourceDoc docToWeight, Term term);
}
