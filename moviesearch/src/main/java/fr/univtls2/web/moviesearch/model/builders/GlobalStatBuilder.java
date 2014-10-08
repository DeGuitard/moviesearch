package fr.univtls2.web.moviesearch.model.builders;

import fr.univtls2.web.moviesearch.model.GlobalStat;

public class GlobalStatBuilder {

	private String key;
	private Object value;

	public GlobalStatBuilder key(String key) {
		this.key = key;
		return this;
	}

	public GlobalStatBuilder value(Object value) {
		this.value = value;
		return this;
	}

	public GlobalStat create() {
		GlobalStat stat = new GlobalStat();
		stat.setKey(key);
		stat.setValue(value);
		return stat;
	}
}
