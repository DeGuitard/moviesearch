package fr.univtls2.web.moviesearch.services.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import fr.univtls2.web.moviesearch.model.SourceDoc;
import fr.univtls2.web.moviesearch.model.Term;
import fr.univtls2.web.moviesearch.services.indexation.extraction.Extractor;
import fr.univtls2.web.moviesearch.services.indexation.normalization.Normalizer;
import fr.univtls2.web.moviesearch.services.persistence.dao.TermDao;

/**
 * Simple implementation of {@link QueryExecutor}.
 *
 * @author Vianney Dupoy de Guitard
 */
public class SimpleQueryExecutor implements QueryExecutor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleQueryExecutor.class);

	@Inject private TermDao dao;
	@Inject private Extractor extractor;
	@Inject private Normalizer normalizer;

	@Override
	public List<SourceDoc> execute(String query) {
		
		List<SourceDoc> results = new ArrayList<SourceDoc>();
		List<Term> termsToFind = extractor.extract(query);
		termsToFind = normalizer.normalize(termsToFind);
	
		Collection<Term> termsFound = dao.findByWords(termsToFind);
		for (Term termFound : termsFound) {
			if (results.isEmpty()) {
				results.addAll(termFound.getDocuments());
			} else {
				results.retainAll(termFound.getDocuments());
				for (SourceDoc result : results) {
					int docIndex = termFound.getDocuments().indexOf(result);
					SourceDoc docFound = termFound.getDocuments().get(docIndex);
					result.setWeight(result.getWeight() + docFound.getWeight());
				}
			}
		}

		Collections.sort(results);
		return results;
	}

}
