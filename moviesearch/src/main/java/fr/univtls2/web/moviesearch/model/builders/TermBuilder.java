package fr.univtls2.web.moviesearch.model.builders;

import java.util.List;

import fr.univtls2.web.moviesearch.model.SourceDoc;
import fr.univtls2.web.moviesearch.model.Term;

public class TermBuilder {

	private String word;
	private List<SourceDoc> sourceDocs;

	public TermBuilder term(String word) {
		this.word = word;
		return this;
	}

	public TermBuilder sourceDocs(List<SourceDoc> sourceDocs) {
		this.sourceDocs = sourceDocs;
		return this;
	}

	public Term create() {
		Term term = new Term();
		term.setWord(word);
		term.setDocuments(sourceDocs);

		return term;
	}
}
