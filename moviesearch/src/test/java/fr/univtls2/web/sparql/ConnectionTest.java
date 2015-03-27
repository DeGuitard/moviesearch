package fr.univtls2.web.sparql;

import org.junit.Assert;
import org.junit.Test;

public class ConnectionTest {

	@Test
	public void testConnection() {
		SparqlClient sparqlClient = new SparqlClient("localhost:8080/space");
		String query = "ASK WHERE { ?s ?p ?o }";
		boolean serverIsUp = sparqlClient.ask(query);		
		Assert.assertTrue("La connexion au serveur n'aurait pas dû échouer.", serverIsUp);
	}
}
