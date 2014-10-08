package fr.univtls2.web.moviesearch.services.indexation.weighting.rules;

import com.google.inject.Inject;

import fr.univtls2.web.moviesearch.model.GlobalStat;
import fr.univtls2.web.moviesearch.model.SourceDoc;
import fr.univtls2.web.moviesearch.model.Term;
import fr.univtls2.web.moviesearch.services.persistence.dao.GlobalStatDao;

/**
 * <p>Implementation of the Robertson Term Frequency rule.</p>
 * <p>Follows this calculation: freq/(freq+0.5+1.5*(docSize/avgDocSize))</p>
 *
 * @author Vianney Dupoy de Guitard
 *
 */
public class IDFRule implements WeightingRule {

	/** Data access object for stats. */
	@Inject private GlobalStatDao statDao;

	/** Amount of documents in the collection. */
	private Double docCount;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double weight(SourceDoc docToWeight, Term term) {
		if (docCount == null) {
			GlobalStat docCountStat = statDao.findByKey(GlobalStat.KEY_ALL_DOCS_COUNT);
			docCount = (Double) docCountStat.getValue();
		}
		return Math.log(docCount / term.getDocuments().size());
	}

	@Override
	public double ratio() {
		return 0;
	}
}
