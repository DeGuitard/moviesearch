package fr.univtls2.web.moviesearch.services.indexation.weighting.rules;

import java.util.HashMap;

import fr.univtls2.web.moviesearch.model.SourceDoc;
import fr.univtls2.web.moviesearch.model.Term;

/**
 * <p>Implementation of a tagging rule.</p>
 *
 * @author Vianney Dupoy de Guitard
 *
 */
public class TaggingRule implements WeightingRule {

	private HashMap<String, Double> tagWeights = new HashMap<>();

	public TaggingRule() {
		tagWeights.put("title", 1d);
		tagWeights.put("a", 0.2d);
		tagWeights.put("nav", 0.2d);
		tagWeights.put("td", 0.4d);
		tagWeights.put("p", 0.3d);
		tagWeights.put("span", 0.4d);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double weight(SourceDoc docToWeight, Term term) {
		double weight = 0;
		for (String tag : docToWeight.getTags()) {
			if (tagWeights.containsKey(tag)) {
				weight += tagWeights.get(tag);
			}
		}
		return weight;
	}

	@Override
	public double ratio() {
		return 0;
	}
}
