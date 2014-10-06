package fr.univtls2.web.moviesearch.services.persistence.dao;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;

import fr.univtls2.web.moviesearch.model.Term;
import fr.univtls2.web.moviesearch.services.persistence.GenericDao;

public class TermDao extends GenericDao<Term> {

	public TermDao() {
		super(Term.class);
	}

	public Deque<Term> findByWords(Collection<Term> termList) {
		// Gets all the titles.
		Deque<String> words = new ArrayDeque<>();
		for (Term term : termList) {
			words.add(term.getWord());
		}

		return findByField(words, "word");
	}

	@Override
	protected String getCollectionName() {
		return "terms";
	}

}
