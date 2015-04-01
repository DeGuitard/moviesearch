package fr.univtls2.web.moviesearch.services.sparql;

/**
 * Simplistic implementation of a query builder. But enough for our needs.
 *
 * @author Vianney Dupoy de Guitard
 */
public class QueryBuilderImpl implements QueryBuilder {

	/** Common prefixes frequently used. */
	private static final String[] PREFIXES = {
		"PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ",
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> ",
		"PREFIX owl:  <http://www.w3.org/2002/07/owl#> ",
		"PREFIX xsd:  <http://www.w3.org/2001/XMLSchema#> ",
		"PREFIX inst: <http://www.irit.fr/recherches/MELODI/ontologies/FilmographieV1.owl#> "
	};

	/** The current query. */
	private final StringBuilder query = new StringBuilder();

	/** Simple constructors. It automagically adds most frequent prefixes. */
	public QueryBuilderImpl() {
		for (String prefix : PREFIXES) {
			query.append(prefix);
		}
	}

	/** {@inheritDoc} */
	@Override
	public QueryBuilder ask() {
		query.append("ASK {");
		return this;
	}

	/** {@inheritDoc} */
	@Override
	public QueryBuilder select(final String terms) {
		query.append("SELECT ");
		query.append(terms);
		query.append(" ");
		return this;
	}

	/** {@inheritDoc} */
	@Override
	public QueryBuilder where() {
		query.append("WHERE { ");
		return this;
	}

	/** {@inheritDoc} */
	@Override
	public String end() {
		query.append("}");
		return query.toString();
	}

	/** {@inheritDoc} */
	@Override
	public QueryBuilder triple(final String subject, final String predicate, final String object) {
		query.append(subject);
		query.append(" ");
		query.append(predicate);
		query.append(" ");
		query.append(object);
		query.append(" ");
		return this;
	}

	/** {@inheritDoc} */
	@Override
	public QueryBuilder and() {
		query.append(". ");
		return this;
	}

	/** {@inheritDoc} */
	@Override
	public QueryBuilder union() {
		query.append("UNION ");
		return this;
	}

	/** {@inheritDoc} */
	@Override
	public QueryBuilder subsetStart() {
		query.append("{ ");
		return this;
	}

	/** {@inheritDoc} */
	@Override
	public QueryBuilder subsetEnd() {
		query.append("} ");
		return this;
	}

}
