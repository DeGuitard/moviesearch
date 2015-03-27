package fr.univtls2.web.moviesearch.services.query;

import java.util.List;

import fr.univtls2.web.moviesearch.model.SourceDoc;

public class SparqlQueryExecutor implements QueryExecutor {

	@Override
	public List<SourceDoc> execute(String query) {

		// instances = rechercherInstances(req)
		// liens = rechercherLiens(req)
		// composerTriplets(instances, liens)

		// lieu, naissance, Omar Sy
		// lieu -> lien
		// naissance -> lien
		// omar sy -> instance
		// TODO Guillaume Ruscassie
		return null;
	}

}
