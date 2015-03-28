package fr.univtls2.web.sparql;

import java.util.Collection;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the Sparl Client.
 * @see {@link SparqlClient}.
 * @author Vianney Dupoy de Guitard
 */
public class SparqlClientTest {

	/** Tests the connection to a sparql server. */
	@Test
	public void testConnection() {
		SparqlClient sparqlClient = new SparqlClient("localhost:8080/space");
		String query = "ASK WHERE { ?s ?p ?o }";
		boolean serverIsUp = sparqlClient.ask(query);
		Assert.assertTrue("Server connection should not have failed.", serverIsUp);
	}

	/** Tests a query on a sparql server with an ontology about movies. */
	@Test
	public void testQuery() {
		SparqlClient sparqlClient = new SparqlClient("localhost:8080/space");
		String query = "SELECT ?p WHERE { inst:Intouchables inst:aPourContributeur ?p }";

		Collection<Map<String, String>> results = sparqlClient.select(query);
		for (Map<String, String> result : results) {
			for (String val : result.values()) {
				System.out.println(val);
			}
		}
		Assert.assertEquals("Wrong results count.", 40, results.size());
	}

	@Test
	public void testQueryQ1() {
		SparqlClient sparqlClient = new SparqlClient("localhost:8080/space");
		String query = "SELECT distinct ?p WHERE {{inst:Intouchables ?t1 ?o1. ?p ?t1 inst:Personne;} UNION {inst:Personne ?t1 ?o1. ?p ?t1 inst:Intouchables;}}";

		Collection<Map<String, String>> results = sparqlClient.select(query);
		for (Map<String, String> result : results) {
			for (String val : result.values()) {
				System.out.println(val);
			}
		}
		Assert.assertEquals("Wrong results count.", 54, results.size());
	}
}
