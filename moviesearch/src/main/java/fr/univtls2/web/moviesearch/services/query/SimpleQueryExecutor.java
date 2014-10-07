package fr.univtls2.web.moviesearch.services.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;

import fr.univtls2.web.moviesearch.model.SourceDoc;
import fr.univtls2.web.moviesearch.model.Term;
import fr.univtls2.web.moviesearch.model.builders.TermBuilder;
import fr.univtls2.web.moviesearch.services.persistence.dao.TermDao;

/**
 * Simple implementation of {@link QueryExecutor}.
 *
 * @author Vianney Dupoy de Guitard
 */
public class SimpleQueryExecutor implements QueryExecutor {

	@Inject private TermDao dao;

	@Override
	public List<SourceDoc> execute(String query) {
		List<SourceDoc> results = new ArrayList<SourceDoc>();
		Term termToFind = new TermBuilder().word(query).create();
		Collection<Term> termsFound = dao.findByWords(Arrays.asList(termToFind));
		for (Term termFound : termsFound) {
			results.addAll(termFound.getDocuments());
		}
		return results;
	}

}
