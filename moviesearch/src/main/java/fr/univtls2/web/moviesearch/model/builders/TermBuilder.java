package fr.univtls2.web.moviesearch.model.builders;

import java.util.List;

import fr.univtls2.web.moviesearch.model.Document;
import fr.univtls2.web.moviesearch.model.Term;

public class TermBuilder {

	private String word;
	private List<Document> documents;

	public TermBuilder term(String word) {
		this.word = word;
		return this;
	}

	public TermBuilder documents(List<Document> documents) {
		this.documents = documents;
		return this;
	}

	public Term create() {
		Term term = new Term();
		term.setWord(word);
		term.setDocuments(documents);

		return term;
	}
}
