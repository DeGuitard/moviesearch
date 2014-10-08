package fr.univtls2.web.moviesearch.services.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import fr.univtls2.web.moviesearch.model.SourceDoc;
import fr.univtls2.web.moviesearch.model.Term;
import fr.univtls2.web.moviesearch.services.indexation.extraction.Extractor;
import fr.univtls2.web.moviesearch.services.indexation.normalization.Normalizer;
import fr.univtls2.web.moviesearch.services.indexation.weighting.SearchWeigher;
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
	@Inject	private Set<SearchWeigher> weightRules;

	@Override
	public List<SourceDoc> execute(String query) {
		
		List<SourceDoc> results = new ArrayList<SourceDoc>();
		List<Term> termsToFind = extractor.extract(query);
		termsToFind = normalizer.normalize(termsToFind);
	
		Collection<Term> termsFound = dao.findByWords(termsToFind);
		for (Term termFound : termsFound) {
			if (results.isEmpty()) {
				results.addAll(termFound.getDocuments());
				for (SourceDoc result : results) {
					result.setTagsList(new ArrayList<List<String>>());
					result.setPositionsList(new ArrayList<List<Integer>>());
					result.getTagsList().add(result.getTags());
					result.getPositionsList().add(result.getPositions());
				}
			} else {
				results.retainAll(termFound.getDocuments());
				for (SourceDoc result : results) {
					int docIndex = termFound.getDocuments().indexOf(result);
					SourceDoc docFound = termFound.getDocuments().get(docIndex);
					mergeDoc(docFound, result);
				}
			}
		}

		for (SourceDoc result : results) {
			for (SearchWeigher weigher : weightRules) {
				weigher.weight(result);
			}
		}

		Collections.sort(results);
		return results;
	}

	/**
	 * Merge two documents.
	 * @param termFound : the term with its list of document.
	 * @param result : the document to merge.
	 */
	private void mergeDoc(SourceDoc docFound, SourceDoc result) {
		result.setWeight(result.getWeight() + docFound.getWeight());
		result.getTagsList().add(docFound.getTags());
		result.getPositionsList().add(docFound.getPositions());
	}

}
