package fr.univtls2.web.moviesearch.model.builders;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import fr.univtls2.web.moviesearch.model.SourceDoc;
import fr.univtls2.web.moviesearch.model.Term;

public class TermBuilder {

	private String word;
	private List<SourceDoc> sourceDocs = new ArrayList<>();

	public TermBuilder word(String word) {
		this.word = word;
		return this;
	}

	public TermBuilder sourceDocs(List<SourceDoc> sourceDocs) {
		this.sourceDocs = sourceDocs;
		return this;
	}

	public Term create() {
		if (StringUtils.isBlank(word)) {
			throw new IllegalArgumentException("The term doesn't have any associated word.");
		}
		Term term = new Term(word);
		term.setDocuments(sourceDocs);

		return term;
	}
}
