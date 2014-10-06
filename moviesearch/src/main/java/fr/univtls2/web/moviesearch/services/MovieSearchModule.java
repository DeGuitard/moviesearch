package fr.univtls2.web.moviesearch.services;

import com.google.inject.AbstractModule;

import fr.univtls2.web.moviesearch.services.properties.PropertyService;
import fr.univtls2.web.moviesearch.services.properties.PropertyServiceImpl;

public class MovieSearchModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(PropertyService.class).to(PropertyServiceImpl.class);
	}

}
