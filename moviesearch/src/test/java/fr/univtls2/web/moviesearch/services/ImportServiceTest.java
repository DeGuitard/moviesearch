package fr.univtls2.web.moviesearch.services;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;

import fr.univtls2.web.moviesearch.MovieSearchTestRunner;
import fr.univtls2.web.moviesearch.MovieSearchTestRunner.GuiceModules;
import fr.univtls2.web.moviesearch.services.indexation.ImportService;

@RunWith(MovieSearchTestRunner.class)
@GuiceModules(MovieSearchModule.class)
public class ImportServiceTest {

	@Inject private ImportService importService;

	/**
	 * Test of {@link ImportService#start(java.io.File)} method.
	 * @throws URISyntaxException if the test data is invalid.
	 */
	@Test
	public void startTest() throws URISyntaxException {
		URL corpusUrl = getClass().getResource("/corpus");
		File corpus = new File(corpusUrl.toURI());
		importService.start(corpus);
	}
}
