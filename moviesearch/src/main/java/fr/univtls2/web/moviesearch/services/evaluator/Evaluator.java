package fr.univtls2.web.moviesearch.services.evaluator;

import java.util.List;

import fr.univtls2.web.moviesearch.model.SourceDoc;

public interface Evaluator {

	double exhaustiveScore(String query, List<SourceDoc> expectedDocs);

	double precisionScore(String query);
}
