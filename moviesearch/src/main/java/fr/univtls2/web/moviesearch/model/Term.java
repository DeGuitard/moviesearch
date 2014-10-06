package fr.univtls2.web.moviesearch.model;

import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Term extends Entity {

	private final String word;
	private List<SourceDoc> sourceDocs;

	public Term(String word) {
		this.word = word;
	}

	public String getWord() {
		return word;
	}

	public List<SourceDoc> getDocuments() {
		return sourceDocs;
	}

	public void setDocuments(List<SourceDoc> sourceDocs) {
		this.sourceDocs = sourceDocs;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || obj == this || obj.getClass() != getClass()) {
			return false;
		}
		Term term = (Term) obj;
		return new EqualsBuilder().append(word, term.word).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(word).hashCode();
	}
}
