package fr.univtls2.web.moviesearch.services.indexation.weighting.rules;

import java.util.Collection;

import fr.univtls2.web.moviesearch.model.SourceDoc;
import fr.univtls2.web.moviesearch.model.Term;

/**
 * <p>Implementation of the Robertson Term Frequency rule.</p>
 * <p>Follows this calculation: freq/(freq+0.5+1.5*(docSize/avgDocSize))</p>
 *
 * @author Vianney Dupoy de Guitard
 *
 */
public class RobertsonRule implements WeightingRule {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double weight(SourceDoc docToWeight, Term term) {
		int freq = docToWeight.getOccurrences();
		int docSize = docToWeight.getSize();
		double avgDocSize = getAvgDocSize(term.getDocuments());
		return freq / (freq + 0.5 + 1.5 * (docSize / avgDocSize));
	}

	/**
	 * @param documents : the list of documents.
	 * @return the average size of the documents.
	 */
	private double getAvgDocSize(Collection<SourceDoc> documents) {
		int docCount = documents.size();
		int totalSize = 0;
		for (SourceDoc doc : documents) {
			totalSize += doc.getSize();
		}
		return totalSize / docCount;
	}
}
