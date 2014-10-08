package fr.univtls2.web.moviesearch.services.indexation.weighting;

import fr.univtls2.web.moviesearch.model.SourceDoc;

public interface SearchWeigher {

	void weight(SourceDoc doc);
}
