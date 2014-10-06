package fr.univtls2.web.moviesearch.services.indexation.normalizer;

import java.util.List;

import fr.univtls2.web.moviesearch.model.Term;

public interface Normalizer {

	List<Term> normalize(final List<Term> terms);
}
