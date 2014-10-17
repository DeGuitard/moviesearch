package fr.univtls2.web.moviesearch.services.persistence.dao;

import fr.univtls2.web.moviesearch.model.GlobalStat;
import fr.univtls2.web.moviesearch.services.persistence.GenericDao;

public class GlobalStatDao extends GenericDao<GlobalStat> {

	public GlobalStatDao() {
		super(GlobalStat.class);
	}

	public GlobalStat findByKey(String key) {
		final GlobalStat stat = new GlobalStat();
		stat.setKey(key);
		final GlobalStat result = findOne(stat);
		return result;
	}

	@Override
	protected String getCollectionName() {
		return "globalStats";
	}

}
