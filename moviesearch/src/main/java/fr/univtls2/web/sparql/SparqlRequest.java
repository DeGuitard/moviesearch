package fr.univtls2.web.sparql;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import fr.univtls2.web.moviesearch.model.Term;

public class SparqlRequest {

	private final String SELECT = "SELECT distinct ?p";
	private final String WHERE = " WHERE {{";
	private final String INST = "inst:";
	private final String MIDDLE_WHERE = " ?t1 ?o1. ?p ?t1 ";
	private final String MIDDLE_WHERE_ALONE = " ?t1 ?o1";
	private final String END_WHERE = ".}";
	private final String UNION = " UNION {";
	private final String MIDDLE_UNION = " ?t1 ?o1. ?p ?t1 ";
	private final String END_UNION = ".}";
	private final String END = "}";

	/**
	 * Generate a ask request with a term.
	 * @param term of the user
	 * @return the request
	 */
	public String generatorAsk(Term term) {
		
		return "ASK {?s ?t inst:" + StringUtils.capitalize(term.getWord()) + ".}";
	}

	/**
	 * Generate a select request in using all terms and doing all possible combination.
	 * @param terms of the user
	 * @return the request
	 */
	public String generatorSelect(List<Term> terms) {

		final StringBuilder query = new StringBuilder();
		query.append(SELECT);

		if (terms.size() == 1) {
			query.append(WHERE).append(INST).append(terms.get(0)).append(MIDDLE_WHERE_ALONE).append(END_WHERE);
		} else if (terms.size() > 1) {
			Iterator<Term> iStart = terms.iterator();
			boolean first = true;
			while (iStart.hasNext()) {
				String term = iStart.next().getWord();
				iStart.remove();

				for (Term st : terms) {
					String capWord = StringUtils.capitalize(st.getWord());
					if (first) {
						query.append(WHERE).append(INST).append(term).append(MIDDLE_WHERE).append(INST).append(capWord).append(END_WHERE)
						.append(UNION).append(INST).append(capWord).append(MIDDLE_UNION).append(INST).append(term).append(END_UNION);
						first = false;
					} else {
						query.append(UNION).append(INST).append(capWord).append(MIDDLE_UNION).append(INST).append(term).append(END_UNION);
					}
				}
			}
		}
		query.append(END);

		return query.toString();
	}
}
