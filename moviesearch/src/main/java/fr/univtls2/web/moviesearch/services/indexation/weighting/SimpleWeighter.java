package fr.univtls2.web.moviesearch.services.indexation.weighting;

import java.util.Set;

import com.google.inject.Inject;

import fr.univtls2.web.moviesearch.model.SourceDoc;
import fr.univtls2.web.moviesearch.model.Term;
import fr.univtls2.web.moviesearch.services.indexation.weighting.rules.WeightingRule;

/**
 * <p>A simple implementation that will sum all the results of the weighting rules implementations.</p>
 *
 * @author Vianney Dupoy de Guitard
 */
public class SimpleWeighter implements Weigher {

	/** Weighting rules to apply. */
	@Inject
	private Set<WeightingRule> weightRules;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void weight(final SourceDoc doc, final Term term) {
		double weight = 0;
		for (WeightingRule rule : weightRules) {
			weight += (rule.weight(doc, term) * rule.ratio());
		}
		doc.setWeight(weight);
	}
}
