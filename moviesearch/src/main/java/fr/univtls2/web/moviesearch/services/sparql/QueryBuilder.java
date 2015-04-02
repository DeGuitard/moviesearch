package fr.univtls2.web.moviesearch.services.sparql;

/**
 * Common interface to easily build SPARQL queries.
 *
 * @author Vianney Dupoy de Guitard
 */
public interface QueryBuilder {

	/**
	 * Adds an ASK directive.
	 * @return the current instance to chain calls.
	 */
	QueryBuilder ask();

	/**
	 * Adds a SELECT directive with some fields.
	 * @param query : the fields to query.
	 * @return the current instance to chain calls.
	 */
	QueryBuilder select(String query);

	/**
	 * Adds a WHERE directive.
	 * @return the current instance to chain calls.
	 */
	QueryBuilder where();

	/**
	 * Ends a request, by appending '}' and returning the final string.
	 * @return the query.
	 */
	String end();

	/**
	 * Starts a subset, which is contained between two curly braces ('{' and '}').
	 * @return the current instance to chain calls.
	 */
	QueryBuilder subsetStart();

	/**
	 * Ends a subset, which is contained between two braces ('{' and '}').
	 * @return the current instance to chain calls.
	 */
	QueryBuilder subsetEnd();

	/**
	 * Appends a triple.
	 * @return the current instance to chain calls.
	 */
	QueryBuilder triple(String subject, String predicate, String object);
	/**
	 * Appends a triple using a comma to separate the data.
	 * @return the current instance to chain calls.
	 */
	QueryBuilder tripleComma(String subject, String predicate, String object);
	/**
	 * Appends the AND ('.') directive.
	 * @return the current instance to chain calls.
	 */
	QueryBuilder and();

	/**
	 * Appends the UNION directive (without the braces).
	 * @return the current instance to chain calls.
	 */
	QueryBuilder union();

	/**
	 * Appends the FILTER directive (without the braces).
	 * @return the current instance to chain calls.
	 */
	QueryBuilder filter();

	/**
	 * Starts a bracket, which is contained between two curly bracket '(' and ')'.
	 * @return the current instance to chain calls.
	 */
	QueryBuilder bracketStart();
	
	/**
	 * Ends a bracket, which is contained between two bracket '(' and ')'.
	 * @return the current instance to chain calls.
	 */
	QueryBuilder bracketEnd();
	/**
	 * Appends the REGEX directive (without the braces).
	 * @return the current instance to chain calls.
	 */
	QueryBuilder regex();

}
