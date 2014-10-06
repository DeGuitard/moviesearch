package fr.univtls2.web.moviesearch.services.persistence.dao;

import fr.univtls2.web.moviesearch.model.Term;
import fr.univtls2.web.moviesearch.services.persistence.GenericDao;

public class TermDao extends GenericDao<Term> {

	public TermDao() {
		super(Term.class);
	}

	@Override
	protected String getCollectionName() {
		return "terms";
	}

}
