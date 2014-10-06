package fr.univtls2.web.moviesearch.model.builders;

import java.util.List;

import fr.univtls2.web.moviesearch.model.SourceDoc;

public class SourceDocBuilder {

	private String url;	
	private int docSize;
	private List<Integer> positions;
	private List<String> balises;
	private int occurrences;
	private int df;
	private int idf;

	public SourceDocBuilder url(String url) {
		this.url = url;
		return this;
	}

	public SourceDocBuilder size(int size) {
		this.docSize = size;
		return this;
	}

	public SourceDocBuilder positions(List<Integer> positions) {
		this.positions = positions;
		return this;
	}

	public SourceDocBuilder balises(List<String> balises) {
		this.balises = balises;
		return this;
	}

	public SourceDocBuilder occurrences(int occurrences) {
		this.occurrences = occurrences;
		return this;
	}

	public SourceDocBuilder df(int df) {
		this.df = df;
		return this;
	}

	public SourceDocBuilder idf(int idf) {
		this.idf = idf;
		return this;
	}

	public SourceDocBuilder create() {
		SourceDoc sourceDoc = new SourceDoc();
		sourceDoc.setUrl(url);
		sourceDoc.setSize(docSize);
		sourceDoc.setPositions(positions);
		sourceDoc.setBalises(balises);
		sourceDoc.setOccurrences(occurrences);
		sourceDoc.setDf(df);
		sourceDoc.setIdf(idf);
		
		return null;
	}
}
