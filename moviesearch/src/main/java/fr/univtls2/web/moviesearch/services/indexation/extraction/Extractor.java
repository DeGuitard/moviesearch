package fr.univtls2.web.moviesearch.services.indexation.extraction;

import java.util.List;

import org.jsoup.nodes.Document;

import fr.univtls2.web.moviesearch.model.Term;

/**
 * <p>An extractor is a service that will take an HTML document and extract all the
 * terms from this document.</p>
 * <p>Each word is cleaned with different mechanisms to become a term.</p>
 * <p>Statistics about the containing tags, occurrences and text position are stored.</p>
 *
 * @author Vianney Dupoy de Guitard
 */
public interface Extractor {

	/**
	 * <p>Given a html document, extracts all the terms.</p>
	 * <p>For each term, it will store information about its tag, occurrences and position.</p>
	 * <p>Each term is word that has been cleaned, with a stop list and without special characters.</p>
	 *
	 * @param doc : the document to parse.
	 * @return the list of terms found in the document.
	 */
	List<Term> extract(final Document doc);

	/**
	 * <p>Given a query, returns a list of terms.</p>
	 * @param query : the query to parse.
	 * @return the terms extracted from the query.
	 */
	Term extract(final String query);

}
