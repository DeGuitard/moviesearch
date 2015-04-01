package fr.univtls2.web.moviesearch.services.sparql;

/**
 * Common interface to easily build SPARQL queries.
 *
 * @author Vianney Dupoy de Guitard
 */
public interface QueryBuilder {
	QueryBuilder ask();
	QueryBuilder select(String terms);
	QueryBuilder where();
	String end();
	
	QueryBuilder triple(String subject, String predicate, String object);
	QueryBuilder and();

	QueryBuilder union();
}
