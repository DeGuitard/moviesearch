package fr.univtls2.web.sparql;

import java.util.List;

import fr.univtls2.web.moviesearch.model.Term;

public interface RequestGenerator {

	String generator(List<Term> terms);

}
