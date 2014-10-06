package fr.univtls2.web.moviesearch.services.indexation.weighting;

import fr.univtls2.web.moviesearch.model.Term;

public interface Weigher {

	void weight(final Term term);
}
