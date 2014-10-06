package fr.univtls2.web.moviesearch.model;

import java.util.List;

public class Term {

	private String word;
	private List<SourceDoc> sourceDocs;

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public List<SourceDoc> getDocuments() {
		return sourceDocs;
	}

	public void setDocuments(List<SourceDoc> sourceDocs) {
		this.sourceDocs = sourceDocs;
	}
}
