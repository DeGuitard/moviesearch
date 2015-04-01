package fr.univtls2.web.moviesearch.services.sparql;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;

public class QueryBuilderTest {

	@Test
	public void simpleSelect() {
		String expectation = "SELECT ?p WHERE { inst:Intouchables inst:aPourContributeur ?p }";
		QueryBuilder qb = new QueryBuilderImpl();
		qb.select("?p").where().triple("inst:Intouchables", "inst:aPourContributeur", "?p");
		Assert.assertTrue("Malformed request.", qb.end().endsWith(expectation));
	}

	@Test
	public void simpleSelectAnd() {
		String expectation = "SELECT ?label WHERE { inst:Intouchables inst:aPourContributeur ?p . ?p rdfs:label ?label }";
		QueryBuilder qb = new QueryBuilderImpl().select("?label").where();
		qb.triple("inst:Intouchables", "inst:aPourContributeur", "?p").and();
		qb.triple("?p", "rdfs:label", "?label");
		Assert.assertTrue("Malformed request.", qb.end().endsWith(expectation));
	}

	@Test
	public void simpleAsk() {
		QueryBuilder qb = new QueryBuilderImpl().ask();
		String expectation = "ASK {?s ?t inst:Personne }";
		String result = qb.triple("?s", "?t", "inst:" + StringUtils.capitalize("Personne")).end();
		Assert.assertTrue("Malformed request.", result.endsWith(expectation));
	}
}
