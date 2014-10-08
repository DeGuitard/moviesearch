package fr.univtls2.web.moviesearch.services;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;

import fr.univtls2.web.moviesearch.MovieSearchTestRunner;
import fr.univtls2.web.moviesearch.MovieSearchTestRunner.GuiceModules;

@RunWith(MovieSearchTestRunner.class)
@GuiceModules(MovieSearchModule.class)
public class EvaluationGeneratorTest {

	/** The evaluation generator to test. */
	@Inject private EvaluationGenerator eval = new EvaluationGenerator();

	/**
	 * Simple test that checks that no exception is thrown during the stats generation.
	 */
	@Test
	public void simpleTest() {
		try {
			URL qrelDirUrl = getClass().getResource("/qrels/");
			File directory = new File(qrelDirUrl.toURI());
			eval.printStats(directory);
		} catch (URISyntaxException e) {
			Assert.fail("Failed to load test data.");
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Evaluation failed.");
		}
	}
}
