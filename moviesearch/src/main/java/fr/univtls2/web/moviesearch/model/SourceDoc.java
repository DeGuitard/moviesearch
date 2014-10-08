package fr.univtls2.web.moviesearch.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class SourceDoc extends Entity implements Comparable<SourceDoc> {

	private final String url;
	private int size;
	private List<Integer> positions;
	private List<String> tags;
	private int occurrences;
	private int df;
	private int idf;
	private double weight;
	private transient List<List<Integer>> positionsList = new ArrayList<>();
	private transient List<List<String>> tagsList = new ArrayList<>();

	public SourceDoc(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
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

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
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

	public void incrementOccurrences() {
		occurrences++;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public List<List<Integer>> getPositionsList() {
		return positionsList;
	}

	public void setPositionsList(List<List<Integer>> positionsList) {
		this.positionsList = positionsList;
	}

	public List<List<String>> getTagsList() {
		return tagsList;
	}

	public void setTagsList(List<List<String>> tagsList) {
		this.tagsList = tagsList;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || obj == this || obj.getClass() != getClass()) {
			return false;
		}
		SourceDoc doc = (SourceDoc) obj;
		return new EqualsBuilder().append(url, doc.url).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(url).hashCode();
	}

	@Override
	public int compareTo(SourceDoc o) {
		if (this.weight == o.weight) {
			return 0;
		}
		return this.weight > o.weight ? -1 : 1;
	}
}