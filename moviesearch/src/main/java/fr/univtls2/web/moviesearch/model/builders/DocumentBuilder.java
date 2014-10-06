package fr.univtls2.web.moviesearch.model.builders;

import java.util.List;

import fr.univtls2.web.moviesearch.model.Document;

public class DocumentBuilder {

	private String url;
	private int size;
	private List<Integer> positions;
	private List<String> balises;
	private int occurrences;
	private int df;
	private int idf;

	public DocumentBuilder url(String url) {
		this.url = url;
		return this;
	}

	public DocumentBuilder size(int size) {
		this.size = size;
		return this;
	}

	public DocumentBuilder positions(List<Integer> positions) {
		this.positions = positions;
		return this;
	}

	public DocumentBuilder balises(List<String> balises) {
		this.balises = balises;
		return this;
	}

	public DocumentBuilder occurrences(int occurrences) {
		this.occurrences = occurrences;
		return this;
	}

	public DocumentBuilder df(int df) {
		this.df = df;
		return this;
	}

	public DocumentBuilder idf(int idf) {
		this.idf = idf;
		return this;
	}

	public DocumentBuilder create() {
		Document document = new Document();
		document.setUrl(url);
		document.setSize(size);
		document.setPositions(positions);
		document.setBalises(balises);
		document.setOccurrences(occurrences);
		document.setDf(df);
		document.setIdf(idf);
		
		return null;
	}
}
