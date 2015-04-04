package fr.univtls2.web.moviesearch.services.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;

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

	@Inject
	private TermDao dao;
	@Inject
	private Extractor extractor;
	@Inject
	private Normalizer normalizer;
	@Inject
	private Set<SearchWeigher> weightRules;

	@Override
	public List<SourceDoc> execute(String pQuery) {
		SparqlClient sparqlClient = new SparqlClient("localhost:8080/space");
		SparqlRequest sparqlRequest = new SparqlRequest();

		List<SourceDoc> results = null;
		List<Term> termsUser = extractor.extract(pQuery);
		List<Term> termsInstance = new ArrayList<Term>();
		List<Term> termsOther = new ArrayList<Term>();

		// We test the term to know if a instance of the term exist
		// if the term exist then we put it in other list to use later in a select
		// TODO bug when we add in termsInstance the term
		for (Term term : termsUser) {
		
			Term instance = searchInstance(sparqlClient, term,sparqlRequest);
			if (instance != null) {
				termsInstance.add(instance);
			} else if(sparqlClient.ask(sparqlRequest.generatorAsk(term))) {
				termsInstance.add(term);	
			}else {
				termsOther.add(term);
			}
		}

		if (!termsInstance.isEmpty() && termsOther.isEmpty()) {
			Collection<Map<String, String>> resultSparql = sparqlClient.select(sparqlRequest.generatorSelect(termsInstance));
		
			StringBuilder newQuery = new StringBuilder();
			for (Map<String, String> map : resultSparql) {
				for (String v : map.values()) {
					newQuery.append(v).append(" ");
				}
			}
			results = search(newQuery.toString());
		} else {
			Collection<Map<String, String>> resultLinks = sparqlClient.select(sparqlRequest.generatorFilterLink(termsOther));
			for (Map<String, String> map : resultLinks) {
				for (String v : map.values()) {
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
		return results;
	}

	private Term searchInstance(SparqlClient sparqlClient, Term term, SparqlRequest requestGenerator) {
	    Collection<Map<String, String>> resultSparql = sparqlClient.select(requestGenerator.generatorSelectInstance(term));
	    Term instance = null;
	    for (Map<String, String> map : resultSparql) {
	    	for (String word : map.values()) {
	    	instance = new Term(word);
	    	}
	    }
	    return instance;
    }

	private List<SourceDoc> search(String pQuery) {
		List<Term> termsToFind = extractor.extract(pQuery);
		termsToFind = normalizer.normalize(termsToFind);
		List<SourceDoc> results = new ArrayList<SourceDoc>();

		Collection<Term> termsFound = dao.findByWords(termsToFind);
		for (Term termFound : termsFound) {
			if (results.isEmpty()) {
				results.addAll(termFound.getDocuments());
				for (SourceDoc result : results) {
					result.setTagsList(new ArrayList<List<String>>());
					result.setPositionsList(new ArrayList<List<Integer>>());
					result.getTagsList().add(result.getTags());
					result.getPositionsList().add(result.getPositions());
				}
			} else {
				results.retainAll(termFound.getDocuments());
				for (SourceDoc result : results) {
					int docIndex = termFound.getDocuments().indexOf(result);
					SourceDoc docFound = termFound.getDocuments().get(docIndex);
					mergeDoc(docFound, result);
				}
			}
		}

		for (SourceDoc result : results) {
			for (SearchWeigher weigher : weightRules) {
				weigher.weight(result);
			}
		}

		Collections.sort(results);
		return results;
	}
	
	/**
	 * Merge two documents.
	 * @param termFound : the term with its list of document.
	 * @param result : the document to merge.
	 */
	private void mergeDoc(SourceDoc docFound, SourceDoc result) {
		result.setWeight(result.getWeight() + docFound.getWeight());
		result.getTagsList().add(docFound.getTags());
		result.getPositionsList().add(docFound.getPositions());
	}
}
