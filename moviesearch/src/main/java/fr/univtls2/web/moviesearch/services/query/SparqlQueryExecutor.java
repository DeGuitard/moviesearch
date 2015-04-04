package fr.univtls2.web.moviesearch.services.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import fr.univtls2.web.moviesearch.model.SourceDoc;
import fr.univtls2.web.moviesearch.model.Term;
import fr.univtls2.web.moviesearch.services.evaluator.QRelLoaderImpl;
import fr.univtls2.web.moviesearch.services.indexation.extraction.Extractor;
import fr.univtls2.web.moviesearch.services.indexation.normalization.Normalizer;
import fr.univtls2.web.moviesearch.services.indexation.weighting.SearchWeigher;
import fr.univtls2.web.moviesearch.services.persistence.dao.TermDao;
import fr.univtls2.web.sparql.SparqlClient;
import fr.univtls2.web.sparql.SparqlRequest;

public class SparqlQueryExecutor implements QueryExecutor {
	private static final Logger LOGGER = LoggerFactory.getLogger(QRelLoaderImpl.class);

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
		List<Term> mayBeLink = new ArrayList<Term>();
		List<Term> isLink = new ArrayList<Term>();
		
		// We test the term to know if a instance of the term exist
		// if the term exist then we put it in other list to use later in a select
		for (Term term : termsUser) {
			// search the instance of the word input by the user
			Term instance = searchInstance(sparqlClient, term, sparqlRequest);
			if (instance != null) {
				termsInstance.add(instance);
			} else
				if (sparqlClient.ask(sparqlRequest.generatorAsk(term))) {
				// else we test if the term is a instance
				termsInstance.add(term);
			} else {
				mayBeLink.add(term);
			}
		}
		mayBeLink.addAll(termsInstance);
		
		if (!mayBeLink.isEmpty()) {
			// the rest of the term not used are may be a link

			isLink = searchLink(sparqlClient, mayBeLink, sparqlRequest);
			if(isLink.isEmpty()){
			
				// we haven't identify the term
				termsOther.addAll(termsOther);
			}
		}
		// We build build a query to search in ours BDD
		StringBuilder newQuery = new StringBuilder();
		if (!termsInstance.isEmpty()) {
			// We build a new sparql query to found all data link we the instance and the links identify
			Collection<Map<String, String>> resultSparql = sparqlClient.select(sparqlRequest.generatorSelect(termsInstance));

			for (Map<String, String> map : resultSparql) {
				for (String v : map.values()) {
					newQuery.append(v).append(" ");
				}
			}
		}
		if(!termsInstance.isEmpty() && !isLink.isEmpty()){
			String q = sparqlRequest.generatorSelectLink(termsInstance, isLink);
			System.err.println(q);
			Collection<Map<String, String>> resultSparql = sparqlClient.select(q);

			//we add the resultat of the link request in the query for ours BDD
			for (Map<String, String> map : resultSparql) {
				for (String v : map.values()) {
					newQuery.append(v).append(" ");
				}
			}
		}
		if(newQuery.length()==0){
			LOGGER.error("No query generate with sparql then we use the old system");
			termsOther = termsUser;
		}
		//and finally the terms not used 
		if (!termsOther.isEmpty()) {
			
			for (Term term : termsOther) {
				newQuery.append(term.getWord()).append(" ");
			}
		}
		results = search(newQuery.toString());
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

	private Term searchInstance(SparqlClient sparqlClient, Term term, SparqlRequest sparqlRequest) {
		Collection<Map<String, String>> resultSparql = sparqlClient.select(sparqlRequest.generatorSelectInstance(term));
		Term instance = null;
		for (Map<String, String> map : resultSparql) {
			for (String word : map.values()) {
				instance = new Term(word.substring(word.indexOf("#")+1));
			}
		}
		return instance;
	}

	private List<Term> searchLink(SparqlClient sparqlClient, List<Term> terms, SparqlRequest sparqlRequest) {
		List<Term> resultat = new ArrayList<Term>();
		Iterator<Term> iStart = terms.iterator();

		while (iStart.hasNext()) {
			Term term1 = iStart.next();
			iStart.remove();

			for (Term term2 : terms) {
				String q = sparqlRequest.generatorContainsLink(term1, term2);
				System.out.println(q);
				Collection<Map<String, String>> resultSparql = sparqlClient.select(q);
				for (Map<String, String> map : resultSparql) {
					for (String word : map.values()) {
						resultat.add( new Term(word.substring(word.indexOf("#")+1)));
					}
				}
			}
		}
		
		return resultat;
	}			
	/*Collection<Map<String, String>> resultLinks = sparqlClient.select(sparqlRequest.generatorContainsLink(mayBeLink));
	if (resultLinks != null) {
	for (Map<String, String> map : resultLinks) {
		for (String v : map.values()) {
			isLink.add(new Term(v.substring(v.indexOf("#")+1)));
		}
	}*/
	
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
