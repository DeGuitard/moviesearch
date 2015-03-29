package fr.univtls2.web.moviesearch.services.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.inject.Inject;

import fr.univtls2.web.moviesearch.model.SourceDoc;
import fr.univtls2.web.moviesearch.model.Term;
import fr.univtls2.web.moviesearch.services.indexation.extraction.Extractor;
import fr.univtls2.web.moviesearch.services.indexation.normalization.Normalizer;
import fr.univtls2.web.moviesearch.services.indexation.weighting.SearchWeigher;
import fr.univtls2.web.moviesearch.services.persistence.dao.TermDao;
import fr.univtls2.web.sparql.SparqlClient;
import fr.univtls2.web.sparql.SparqlRequest;

public class SparqlQueryExecutor implements QueryExecutor {

	@Inject private TermDao dao;
	@Inject	private Extractor extractor;
	@Inject	private Normalizer normalizer;
	@Inject	private Set<SearchWeigher> weightRules;

	@Override
	public List<SourceDoc> execute(String query) {
		SparqlClient sparqlClient = new SparqlClient("localhost:8080/space");
		SparqlRequest sparqlRequest = new SparqlRequest();

		List<SourceDoc> results = new ArrayList<SourceDoc>();
		List<Term> termsUser = extractor.extract(query);
		List<Term> termsInstance = new ArrayList<Term>();
		List<Term> termsOther = new ArrayList<Term>();
		
		//We test the term to know if a instance of the term exist
		//if the term exist then we put it in other list to use later in a select
		//TODO bug when we add in termsInstance the term 
		for (Term term : termsUser) {
			if (sparqlClient.ask(sparqlRequest.generatorAsk(term))) {
				termsInstance.add(term);
			}else{
				termsOther.add(term);
			}
		}
		
		if(!termsInstance.isEmpty() && termsOther.isEmpty()){
			Collection<Map<String, String>> result = sparqlClient.select(sparqlRequest.generatorSelect(termsInstance));
			//TODO Just to see replace by a the old treatment did on the user term.
			for(Map<String, String> map : result){
				for(String v : map.values()){
					System.out.println(v);
				}
			}
		}
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
