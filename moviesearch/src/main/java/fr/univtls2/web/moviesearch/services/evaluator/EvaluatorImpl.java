package fr.univtls2.web.moviesearch.services.evaluator;

import java.util.List;

import fr.univtls2.web.moviesearch.model.SourceDoc;

public class EvaluatorImpl implements Evaluator {

	@Override
	public double exhaustiveScore(String query, List<SourceDoc> expectedDocs) {
		return 0;
	}

	@Override
	public double precisionScore(String query) {
		return 0;
	}

}
