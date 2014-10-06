package fr.univtls2.web.moviesearch.services;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import fr.univtls2.web.moviesearch.services.indexation.extraction.Extractor;
import fr.univtls2.web.moviesearch.services.indexation.extraction.SimpleExtractor;
import fr.univtls2.web.moviesearch.services.indexation.normalization.Normalizer;
import fr.univtls2.web.moviesearch.services.indexation.normalization.SimpleNormalizer;
import fr.univtls2.web.moviesearch.services.indexation.normalization.rules.TransformationRule;
import fr.univtls2.web.moviesearch.services.indexation.normalization.rules.TruncateRule;
import fr.univtls2.web.moviesearch.services.indexation.weighting.SimpleWeighter;
import fr.univtls2.web.moviesearch.services.indexation.weighting.Weigher;
import fr.univtls2.web.moviesearch.services.indexation.weighting.rules.RobertsonRule;
import fr.univtls2.web.moviesearch.services.indexation.weighting.rules.WeightingRule;
import fr.univtls2.web.moviesearch.services.properties.PropertyService;
import fr.univtls2.web.moviesearch.services.properties.PropertyServiceImpl;

/**
 * <p>Main guice module, that will inject implementation to main services.</p>
 *
 * @author Vianney Dupoy de Guitard
 */
public class MovieSearchModule extends AbstractModule {

	@Override
	protected void configure() {
		// Simple bindings.
		bind(PropertyService.class).to(PropertyServiceImpl.class);
		bind(Extractor.class).to(SimpleExtractor.class);
		bind(Normalizer.class).to(SimpleNormalizer.class);
		bind(Weigher.class).to(SimpleWeighter.class);

		// Transformation rules binding.
		Multibinder<TransformationRule> trsfrmRuleBinder = Multibinder.newSetBinder(binder(), TransformationRule.class);
		trsfrmRuleBinder.addBinding().to(TruncateRule.class);

		// Weight rules binding.
		Multibinder<WeightingRule> weightRuleBinder = Multibinder.newSetBinder(binder(), WeightingRule.class);
		weightRuleBinder.addBinding().to(RobertsonRule.class);
	}

}
