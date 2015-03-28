package fr.univtls2.web.moviesearch.services.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.inject.Inject;

import fr.univtls2.web.moviesearch.model.SourceDoc;
import fr.univtls2.web.moviesearch.model.Term;
import fr.univtls2.web.moviesearch.services.indexation.extraction.Extractor;
import fr.univtls2.web.moviesearch.services.indexation.normalization.Normalizer;
import fr.univtls2.web.moviesearch.services.indexation.weighting.SearchWeigher;
import fr.univtls2.web.moviesearch.services.persistence.dao.TermDao;

public class SparqlQueryExecutor implements QueryExecutor {

	@Inject
	private TermDao dao;
	@Inject
	private Extractor extractor;
	@Inject
	private Normalizer normalizer;
	@Inject
	private Set<SearchWeigher> weightRules;

	@Override
	public List<SourceDoc> execute(String query) {

		List<SourceDoc> results = new ArrayList<SourceDoc>();
		List<Term> termsToFind = extractor.extract(query);

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

	private void searchInstances(List<Term> pTermsToFind) {
		StringBuilder query = new StringBuilder();
		// String query =
		// "SELECT distinct ?p WHERE {{inst:Intouchables ?t1 ?o1. ?p ?t1 inst:Personne;} UNION {inst:Personne ?t1 ?o1. ?p ?t1 inst:Intouchables;}}";

	}
}
