package fr.univtls2.web.sparql;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import fr.univtls2.web.moviesearch.model.Term;
import fr.univtls2.web.moviesearch.services.sparql.QueryBuilder;
import fr.univtls2.web.moviesearch.services.sparql.QueryBuilderImpl;

public class SparqlRequest {

	/**
	 * Generate a ask request with a term.
	 * @param term of the user
	 * @return the request
	 */
	public String generatorAsk(Term term) {
		QueryBuilder qb = new QueryBuilderImpl().ask();
		return qb.triple("?s", "?t", "inst:" + StringUtils.capitalize(term.getWord())).end();
	}

	/**
	 * Generate a select request in using all terms and doing all possible combination.
	 * @param terms of the user
	 * @return the request
	 */
	public String generatorSelect(List<Term> pTerms) {
		List<Term> terms = new ArrayList<Term>(pTerms);
		QueryBuilder qb = new QueryBuilderImpl();
		qb.select("distinct ?label");

		if (terms.size() == 1) {
			String inst = "inst:" + StringUtils.capitalize(terms.get(0).getWord());
			qb.where().triple(inst, "?t", "?o").and();
			qb.triple("?o", "rdfs:label", "?label");
		} else if (terms.size() > 1) {
			Iterator<Term> iStart = terms.iterator();
			boolean first = true;
			while (iStart.hasNext()) {
				String instA = "inst:" + StringUtils.capitalize(iStart.next().getWord());
				iStart.remove();

				for (Term st : terms) {
					String instB = "inst:" + StringUtils.capitalize(st.getWord());
					if (first) {
						qb.where().subsetStart();
						qb.triple(instA, "?t", "?o").and();
						qb.triple("?p", "?t", instB).and();
						qb.triple("?p", "rdfs:label", "?label").subsetEnd();
						first = false;
					}
					qb.union().subsetStart();
					qb.triple(instB, "?t", "?o").and();
					qb.triple("?p", "?t", instA).and();
					qb.triple("?p", "rdfs:label", "?label").subsetEnd();
				}
			}
		}
		return qb.end();
	}

	/*
	 * SELECT distinct ?label WHERE { { inst:OmarSy inst:aPourLieuNaissance ?o . ?o rdfs:label ?label } UNION { inst:Personnes inst:aPourLieuNaissance
	 * ?o . ?o rdfs:label ?label } }
	 */
	public String generatorSelectLink(List<Term> terms, List<Term> pTermsLink) {
		List<Term> termsLink = new ArrayList<Term>(pTermsLink);
		QueryBuilder qb = new QueryBuilderImpl();
		qb.select("distinct ?label");
		qb.where().subsetStart();
		boolean first = true;
		for (Term term : terms) {
			for (Term link : termsLink) {
				if (first) {
					qb.triple("inst:"+term.getWord(), "inst:"+link.getWord(), "?o").and();
					qb.triple("?o", "rdfs:label", "?label").subsetEnd();
					first = false;
				}else{
					qb.union().subsetStart();
					qb.triple("inst:"+term.getWord(), "inst:"+link.getWord(), "?o").and();
					qb.triple("?o", "rdfs:label", "?label").subsetEnd();
				}

			}
		}
		return qb.end();
	}
	/*
	 * SELECT distinct ?label WHERE {  ?a inst:recompensePersonne ?o . ?o rdfs:label ?label } 
	 */
	public String generatorSelectLink( List<Term> pTermsLink) {
		List<Term> termsLink = new ArrayList<Term>(pTermsLink);
		QueryBuilder qb = new QueryBuilderImpl();
		qb.select("distinct ?label");
		qb.where().subsetStart();
		boolean first = true;
		for (Term link : termsLink) {
			if (first) {
				qb.triple("?a", "inst:"+link.getWord(), "?o").and();
				qb.triple("?o", "rdfs:label", "?label").subsetEnd();
				first = false;
			}else{
				qb.union().subsetStart();
				qb.triple("?b", "inst:"+link.getWord(), "?o").and();
				qb.triple("?o", "rdfs:label", "?label").subsetEnd();
			}

		}
		return qb.end();
	}

	/**
	 * Generate a request to found a link.
	 * @param terms users input
	 * @return the request
	 */
	public String generatorFilterLink(List<Term> terms) {
		QueryBuilder qb = new QueryBuilderImpl();
		qb.select("distinct ?property");
		qb.where();
		qb.triple("?subject", "?property", "?value").and();
		qb.triple("?property", "rdfs:label", "?label").and();
		qb.filter().bracketStart();
		qb.regex().bracketStart();

		StringBuilder regex = new StringBuilder();
		regex.append("\"");
		for (Term term : terms) {
			regex.append(term.getWord()).append("|");
		}
		regex.replace(regex.length() - 1, regex.length(), "");
		regex.append("\"");

		qb.tripleComma("?label", regex.toString(), "\"i\"").bracketEnd().bracketEnd();

		return qb.end();
	}

	/**
	 * Generate a request to found a link.
	 * @param terms users input
	 * @return the request
	 */
	public String generatorContainsLink(Term term1,Term term2) {
		QueryBuilder qb = new QueryBuilderImpl();
		qb.select("distinct ?property");
		qb.where();
		qb.triple("?subject", "?property", "?value").and();
		qb.triple("?property", "rdfs:label", "?label").and();
		
		qb.filter().contains().bracketStart().duo("?label", "\""+term1.getWord().toLowerCase()+"\"").bracketEnd();
		qb.filter().contains().bracketStart().duo("?label", "\""+term2.getWord().toLowerCase()+"\"").bracketEnd();	

		return qb.end();
	}
	/**
	 * Generate request to filter on label.
	 * @param terms
	 * @return
	 */
	public String generatorSelectInstance(Term term) {
		// SELECT distinct ?value WHERE {
		// ?subject rdfs:subClassOf ?value .
		// ?value rdfs:label ?label .
		// FILTER ( REGEX ( ?label, "personnes", "i" ) )
		// }
		QueryBuilder qb = new QueryBuilderImpl();
		qb.select("distinct ?value");
		qb.where();
		qb.triple("?subject", "rdfs:subClassOf", "?value").and();
		qb.triple("?value", "rdfs:label", "?label").and();
		qb.filter().bracketStart();
		qb.regex().bracketStart();

		qb.tripleComma("?label", "\"" + term.getWord() + "\"", "\"i\"").bracketEnd().bracketEnd();

		return qb.end();
	}
	/**
	 * Generate request to filter on label.
	 * @param terms
	 * @return
	 */
	public String generatorSelectInstanceOther(Term term) {
	
		QueryBuilder qb = new QueryBuilderImpl();
		qb.select("distinct ?value");
		qb.where();
	//	qb.triple("?subject", "rdfs:subClassOf", "?value").and();
		qb.triple("?value", "rdfs:label", "?label").and();
		qb.filter().bracketStart();
		qb.regex().bracketStart();

		qb.tripleComma("?label", "\"" + term.getWord() + "\"", "\"i\"").bracketEnd().bracketEnd();

		return qb.end();
	}
	
	/**
	 * Generate request to filter on label.
	 * @param terms
	 * @return
	 */
	public String generatorSelectInstanceType(Term term) {
	
		QueryBuilder qb = new QueryBuilderImpl();
		qb.select("distinct ?value");
		qb.where();
		qb.triple("\""+term.getWord()+"\"", "rdf:type", "?type").and();
		
		return qb.end();
	}
}
