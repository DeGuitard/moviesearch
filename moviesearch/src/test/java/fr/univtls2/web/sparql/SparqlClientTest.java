package fr.univtls2.web.sparql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import fr.univtls2.web.moviesearch.model.Term;

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
		String query = "SELECT distinct ?p WHERE {{inst:Intouchables ?t1 ?o1. ?p ?t1 inst:Personne.} UNION {inst:Personne ?t1 ?o1. ?p ?t1 inst:Intouchables.}}";

		Collection<Map<String, String>> results = sparqlClient.select(query);
		for (Map<String, String> result : results) {
			for (String val : result.values()) {
				System.out.println(val);
			}
		}
		Assert.assertEquals("Wrong results count.", 54, results.size());
	}

	@Test
	public void testQueryMix() {
		SparqlRequest requestGenerator = new SparqlRequest();
		List<Term> terms = new ArrayList<Term>();
		terms.add(new Term("Intouchables"));
		terms.add(new Term("Personnes"));
		Assert.assertEquals("SELECT distinct ?p WHERE {{inst:Intouchables ?t1 ?o1. ?p ?t1 inst:Personnes.} "
				+ "UNION {inst:Personnes ?t1 ?o1. ?p ?t1 inst:Intouchables.}}", requestGenerator.generatorSelect(terms));
	}
	
	@Test
	public void testQueryAsk() {
		SparqlRequest requestGenerator = new SparqlRequest();
		SparqlClient sparqlClient = new SparqlClient("localhost:8080/space");
		Assert.assertEquals(true, sparqlClient.ask(requestGenerator.generatorAsk(new Term("Personnes"))));
	}
	@Test
	public void testQueryAskNotFound() {
		SparqlRequest requestGenerator = new SparqlRequest();
		SparqlClient sparqlClient = new SparqlClient("localhost:8080/space");
		Assert.assertEquals(false, sparqlClient.ask(requestGenerator.generatorAsk(new Term("Personneaze"))));
	}
	
	@Test
	public void testQueryFor() {
		List<Term> doubles = new ArrayList<Term>();
		doubles.add(new Term("2"));
		doubles.add(new Term("3"));
		List<Term> copy = new ArrayList<Term>();
		for(Term d : doubles){
			copy.add(d);
		}
		
		System.out.println(copy.size());
	}
}
