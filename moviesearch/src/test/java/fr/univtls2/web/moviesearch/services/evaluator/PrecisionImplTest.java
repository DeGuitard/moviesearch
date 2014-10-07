package fr.univtls2.web.moviesearch.services.evaluator;

import java.io.File;
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
public class PrecisionImplTest {

	@Inject
	private PrecisionImpl precisionImpl;

	@Test
	public void loadingTest() {

		final List<SourceDoc> sourceDocs = precisionImpl.loading(new File(
				"./src/test/resources/qrels"));

		Assert.assertEquals("Check if the , is replace .", sourceDocs.get(29)
				.getWeight(), 0.5, 0);

		Assert.assertEquals("Check if we have the last element", sourceDocs
				.get(sourceDocs.size()-1).getUrl(), "D138.html");
	}
}
