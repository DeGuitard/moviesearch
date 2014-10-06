package fr.univtls2.web.moviesearch.services.indexation.extraction;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

import fr.univtls2.web.moviesearch.model.SourceDoc;
import fr.univtls2.web.moviesearch.model.Term;

public class SimpleExtractorTest {

	private static final String TEST_FILE_URL = "/corpus/D10.html";
	private static final String TEST_WORD = "chambre";
	private static final int TEST_WORD_COUNT = 2;
	private Extractor extractor = new SimpleExtractor();

	@Test
	public void extractionTest() {
		try {
			InputStream stream = getClass().getResourceAsStream(TEST_FILE_URL);
			Document doc = Jsoup.parse(stream, "UTF-8", TEST_FILE_URL);
			List<Term> terms = extractor.extract(doc);

			Assert.assertFalse("Returned list of term is empty.", terms.isEmpty());
			SourceDoc srcDoc = terms.get(0).getDocuments().get(0);
			Assert.assertEquals("Incorrect url document.", TEST_FILE_URL, srcDoc.getUrl());

			boolean testWordFound = false;
			for (Term term : terms) {
				if (term.getWord().equals(TEST_WORD)) {
					testWordFound = true;
					int occurrences = term.getDocuments().get(0).getOccurrences();
					Assert.assertEquals("Occurrences of the test word is different from what was expected.", TEST_WORD_COUNT, occurrences);
				}
			}
			Assert.assertTrue("Test word wasn't found.", testWordFound);
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}
	}
}
