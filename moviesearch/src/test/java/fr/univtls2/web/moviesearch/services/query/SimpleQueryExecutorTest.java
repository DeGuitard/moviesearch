package fr.univtls2.web.moviesearch.services.query;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;

import fr.univtls2.web.moviesearch.MovieSearchTestRunner;
import fr.univtls2.web.moviesearch.MovieSearchTestRunner.GuiceModules;
import fr.univtls2.web.moviesearch.model.SourceDoc;
import fr.univtls2.web.moviesearch.services.MovieSearchModule;

@RunWith(MovieSearchTestRunner.class)
@GuiceModules(MovieSearchModule.class)
public class SimpleQueryExecutorTest {

	/** Executor to test. */
	@Inject private QueryExecutor executor;

	@Test
	public void simpleQuery() {
		String query = "Omar,";
		List<SourceDoc> docs = executor.execute(query);
		Assert.assertFalse("No result returned.", docs.isEmpty());
		Assert.assertEquals("Incorrect result count.", 109, docs.size());
	}
}
