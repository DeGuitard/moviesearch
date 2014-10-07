package fr.univtls2.web.moviesearch.services;

import org.bson.types.ObjectId;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.multibindings.Multibinder;

import fr.univtls2.web.moviesearch.services.evaluator.Evaluator;
import fr.univtls2.web.moviesearch.services.evaluator.EvaluatorImpl;
import fr.univtls2.web.moviesearch.services.indexation.ImportService;
import fr.univtls2.web.moviesearch.services.indexation.ImportServiceImpl;
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
import fr.univtls2.web.moviesearch.services.persistence.DatabaseConnection;
import fr.univtls2.web.moviesearch.services.persistence.DatabaseConnectionImpl;
import fr.univtls2.web.moviesearch.services.persistence.serialization.ObjectIdTypeAdapter;
import fr.univtls2.web.moviesearch.services.properties.PropertyService;
import fr.univtls2.web.moviesearch.services.properties.PropertyServiceImpl;
import fr.univtls2.web.moviesearch.services.query.QueryExecutor;
import fr.univtls2.web.moviesearch.services.query.SimpleQueryExecutor;

/**
 * <p>Main guice module, that will inject implementation to main services.</p>
 *
 * @author Vianney Dupoy de Guitard
 */
public class MovieSearchModule extends AbstractModule {

	@Override
	protected void configure() {
		// Persistence binding.
		bind(DatabaseConnection.class).to(DatabaseConnectionImpl.class);

		// Indexation bindings.
		bind(ImportService.class).to(ImportServiceImpl.class);
		bind(Extractor.class).to(SimpleExtractor.class);
		bind(Normalizer.class).to(SimpleNormalizer.class);
		bind(Weigher.class).to(SimpleWeighter.class);

		// Transformation rules binding.
		Multibinder<TransformationRule> trsfrmRuleBinder = Multibinder.newSetBinder(binder(), TransformationRule.class);
		trsfrmRuleBinder.addBinding().to(TruncateRule.class);

		// Weight rules binding.
		Multibinder<WeightingRule> weightRuleBinder = Multibinder.newSetBinder(binder(), WeightingRule.class);
		weightRuleBinder.addBinding().to(RobertsonRule.class);

		// Tools binding.
		bind(PropertyService.class).to(PropertyServiceImpl.class);
		bind(Evaluator.class).to(EvaluatorImpl.class);
		bind(QueryExecutor.class).to(SimpleQueryExecutor.class);
	}

	/** Provides a GSON with a special type adapter for object ids. */
	@Provides
	Gson provideGson() {
		GsonBuilder builder = new GsonBuilder();
		ObjectIdTypeAdapter ObjectIdAdapter = new ObjectIdTypeAdapter();
		builder.registerTypeAdapter(ObjectId.class, ObjectIdAdapter);
		return builder.create();
	}
}
