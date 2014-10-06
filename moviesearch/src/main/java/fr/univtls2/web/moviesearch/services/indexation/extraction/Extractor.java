package fr.univtls2.web.moviesearch.services.indexation.extraction;

import java.util.List;

import org.jsoup.nodes.Document;

import fr.univtls2.web.moviesearch.model.Term;

public interface Extractor {

	List<Term> extract(final Document doc);
	
}
