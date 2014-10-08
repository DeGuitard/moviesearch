package fr.univtls2.web.moviesearch.services.evaluator;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
public class QRelImplTest {

	@Inject
	private QRelLoaderImpl precisionImpl;

	@Test
	public void loadingTest() {
		Map<String, List<SourceDoc>> qrels = precisionImpl.load(new File("./src/test/resources/qrels"));
		Assert.assertEquals("Incorrect qrel count.", 9, qrels.size());
		int index = 0;
		for (Entry<String, List<SourceDoc>> entry : qrels.entrySet()) {
			if (index == 9) {
				List<SourceDoc> docList = entry.getValue();
				SourceDoc doc = docList.get(docList.size() - 1);
				Assert.assertEquals("The last fetched document is incorrect.", doc, "D138.html");
				doc = docList.get(0);
				Assert.assertEquals("Invalid weight.", doc.getWeight(), 0.5, 0);
				Assert.assertEquals("Invalid count", 43, entry.getValue().size());
			}
			index++;
		}
	}
}
