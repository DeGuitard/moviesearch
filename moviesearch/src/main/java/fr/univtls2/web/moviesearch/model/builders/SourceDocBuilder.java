package fr.univtls2.web.moviesearch.model.builders;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import fr.univtls2.web.moviesearch.model.SourceDoc;

public class SourceDocBuilder {

	private String url;	
	private int docSize;
	private List<Integer> positions = new ArrayList<>();
	private List<String> tags = new ArrayList<>();
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

	public SourceDocBuilder tags(List<String> tags) {
		this.tags = tags;
		return this;
	}

	public SourceDocBuilder tags(String tag) {
		this.tags.add(tag);
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

	public SourceDoc create() {
		if (StringUtils.isBlank(url)) {
			throw new IllegalArgumentException("The document doesn't have any associated url.");
		}

		SourceDoc sourceDoc = new SourceDoc(url);
		sourceDoc.setSize(docSize);
		sourceDoc.setPositions(positions);
		sourceDoc.setTags(tags);
		sourceDoc.setOccurrences(occurrences);
		sourceDoc.setDf(df);
		sourceDoc.setIdf(idf);
		
		return sourceDoc;
	}
}
