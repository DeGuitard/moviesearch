package fr.univtls2.web.moviesearch.model;

import java.util.List;

public class Term {

	private String word;
	private List<Document> documents;

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public List<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}
}
