package fr.univtls2.web.sparql;

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
	public String generatorSelect(List<Term> terms) {
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
						qb.triple("?p", "?t", "inst:" + instB).and();
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
}
