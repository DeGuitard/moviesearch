package fr.univtls2.web.moviesearch.model;

import java.util.List;

public class Document {

	private String url;
	private int size;
	private List<Integer> positions;
	private List<String> balises;
	private int occurrences;
	private int df;
	private int idf;

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public List<Integer> getPositions() {
		return positions;
	}
	public void setPositions(List<Integer> positions) {
		this.positions = positions;
	}
	public List<String> getBalises() {
		return balises;
	}
	public void setBalises(List<String> balises) {
		this.balises = balises;
	}
	public int getOccurrences() {
		return occurrences;
	}
	public void setOccurrences(int occurrences) {
		this.occurrences = occurrences;
	}
	public int getDf() {
		return df;
	}
	public void setDf(int df) {
		this.df = df;
	}
	public int getIdf() {
		return idf;
	}
	public void setIdf(int idf) {
		this.idf = idf;
	}
}