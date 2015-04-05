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

		// We test the term to know if a instance of the term exist
		// if the term exist then we put it in other list to use later in a select

		List<Term> instances = searchAllInstance(sparqlClient, sparqlRequest, termsUser);
		List<Term> links = searchLink(sparqlClient, termsUser, sparqlRequest);
		List<Term> others = searchOtherTerms(termsUser, instances, links);
		
		// We build build a query to search in ours BDD
		results = search(generateQuery(sparqlClient, sparqlRequest, termsUser, instances, links, others));
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

	private String generateQuery(SparqlClient sparqlClient, SparqlRequest sparqlRequest, List<Term> termsUser, List<Term> instances,
            List<Term> links, List<Term> others) {
	    StringBuilder newQuery = new StringBuilder();
		if (!instances.isEmpty() && links.isEmpty()) {
			// We build a new sparql query to found all data link we the instance and the links identify
			Collection<Map<String, String>> resultSparql = sparqlClient.select(sparqlRequest.generatorSelect(instances));

			for (Map<String, String> map : resultSparql) {
				for (String v : map.values()) {
					newQuery.append(v).append(" ");
				}
			}
		}
		if (!instances.isEmpty() && !links.isEmpty()) {
			Collection<Map<String, String>> resultSparql = sparqlClient.select(sparqlRequest.generatorSelectLink(instances, links));

			// we add the resultat of the link request in the query for ours BDD
			for (Map<String, String> map : resultSparql) {
				for (String v : map.values()) {
					newQuery.append(v).append(" ");
				}
			}
		}
		if (newQuery.length() == 0) {
			LOGGER.error("No query generate with sparql then we use the old system");
			others = termsUser;
		}
		// and finally the terms not used
		if (!others.isEmpty()) {

			for (Term term : others) {
				newQuery.append(term.getWord()).append(" ");
			}
		}
	    return newQuery.toString();
    }

	/**
	 * Search a intances in using one term of the users.
	 * 
	 * @param sparqlClient
	 * @param term
	 * @param sparqlRequest
	 * @return
	 */
	private Term searchInstance(SparqlClient sparqlClient, Term term, SparqlRequest sparqlRequest) {
		Collection<Map<String, String>> resultSparql = sparqlClient.select(sparqlRequest.generatorSelectInstance(term));
		Term instance = null;
		if (instance == null) {
			resultSparql = sparqlClient.select(sparqlRequest.generatorSelectInstanceOther(term));
			for (Map<String, String> map : resultSparql) {
				for (String word : map.values()) {
					instance = new Term(word.substring(word.indexOf("#") + 1));
					break;
				}
			}
		}
		return instance;
	}

	/**
	 * Search all intances in using the term of the users.
	 * 
	 * @param sparqlClient
	 * @param term
	 * @param sparqlRequest
	 * @return
	 */
	private List<Term> searchAllInstance(SparqlClient sparqlClient, SparqlRequest sparqlRequest, List<Term> termsUser) {
		List<Term> instances = new ArrayList<Term>();
		for (Term term : termsUser) {
			Term instance = searchInstance(sparqlClient, term, sparqlRequest);
			if (instance != null) {
				if (!isLink(sparqlClient, instance, sparqlRequest)) {
					instances.add(instance);
				}
			}
		}
		return instances;
	}

	/**
	 * Test if the instance is a link.
	 * @param sparqlClient
	 * @param term
	 * @param sparqlRequest
	 * @return true for is a link otherwise false.
	 */
	private boolean isLink(SparqlClient sparqlClient, Term term, SparqlRequest sparqlRequest) {
		Collection<Map<String, String>> resultSparql = sparqlClient.select(sparqlRequest.generatorSelectInstanceType(term));
		Term instance = null;
		if (instance == null) {
			resultSparql = sparqlClient.select(sparqlRequest.generatorSelectInstanceOther(term));
			for (Map<String, String> map : resultSparql) {
				for (String word : map.values()) {
					if (word.endsWith("ObjectProperty")) {
						return true;
					} else if (word.endsWith("Resource")) {
						return true;
					} else if (word.endsWith("Property")) {
						return true;
					}
					break;
				}
			}
		}
		return false;
	}

	/**
	 * Search all links in using the term of the users.
	 * 
	 * @param sparqlClient
	 * @param term
	 * @param sparqlRequest
	 * @return
	 */
	private List<Term> searchLink(SparqlClient sparqlClient, List<Term> pTerms, SparqlRequest sparqlRequest) {
		List<Term> resultat = new ArrayList<Term>();
		List<Term> terms = new ArrayList<Term>(pTerms);
		Iterator<Term> iStart = terms.iterator();

		while (iStart.hasNext()) {
			Term term1 = iStart.next();
			iStart.remove();

			for (Term term2 : terms) {
				//to have a link we must found 2 terms. It's more precise
				String q = sparqlRequest.generatorContainsLink(term1, term2);
				// System.out.println(q);
				Collection<Map<String, String>> resultSparql = sparqlClient.select(q);
				for (Map<String, String> map : resultSparql) {
					for (String word : map.values()) {
						resultat.add(new Term(word.substring(word.indexOf("#") + 1)));
					}
				}
			}
		}

		return resultat;
	}

	/**
	 * Old system of search .
	 * @param pQuery
	 * @return
	 */
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

	/**
	 * The others terms aren't a instance or a link. 
	 * @param usersTerms
	 * @param instances
	 * @param links
	 * @return
	 */
	private List<Term> searchOtherTerms(List<Term> usersTerms, List<Term> instances, List<Term> links) {
		List<Term> termsOther = new ArrayList<Term>();

		for (Term term : usersTerms) {
			boolean already = false;
			for (Term instance : instances) {
				if (instance.getWord().equals(term.getWord())) {
					already=true;
					break;
				}
			}
			if (!already) {
				for (Term link : links) {
					if (link.getWord().equals(term.getWord())) {
						already=true;
						break;
					}
				}
			}
			if (!already) {
				termsOther.add(term);
			}
		}
		return termsOther;
	}
}
