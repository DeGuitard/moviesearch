package fr.univtls2.web.moviesearch.model;

public class GlobalStat extends Entity {

	public transient static final String KEY_ALL_DOCS_COUNT = "ALL_DOCS_COUNT";

	private String key;
	private Object value;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
