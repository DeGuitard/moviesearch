package fr.univtls2.web.moviesearch.services.query;

import java.util.List;

import fr.univtls2.web.moviesearch.model.SourceDoc;

public interface QueryExecutor {

	List<SourceDoc> execute(String query);
}
