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

/**
 * Test class for {@link SimpleExtractor} implementation.
 *
 * @author Vianney Dupoy de Guitard
 */
public class SimpleExtractorTest {

	/** File to parse. */
	private static final String TEST_FILE_URL = "/corpus/D10.html";
	/** A word in the file that should be extracted. */
	private static final String TEST_WORD = "chambre";
	/** Occurrences of the word to find. */
	private static final int TEST_WORD_COUNT = 2;

	/** The extractor to test. */
	private Extractor extractor = new SimpleExtractor();

	/**
	 * Test of {@link SimpleExtractor#extract(Document)} method.
	 */
	@Test
	public void extractionTest() {
		try {
			// Calls the service.
			InputStream stream = getClass().getResourceAsStream(TEST_FILE_URL);
			Document doc = Jsoup.parse(stream, "UTF-8", TEST_FILE_URL);
			List<Term> terms = extractor.extract(doc);

			// Tests the list of term and created document.
			Assert.assertFalse("Returned list of term is empty.", terms.isEmpty());
			SourceDoc srcDoc = terms.get(0).getDocuments().get(0);
			Assert.assertEquals("Incorrect url document.", TEST_FILE_URL, srcDoc.getUrl());

			// Checks the term list found.
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

	/**
	 * Test of {@link SimpleExtractor#extract(Document)} method.
	 */
	@Test
	public void extractionTestNoFile() {
		try {
			Document document = null;
			extractor.extract(document);
			Assert.fail("No exception was thrown.");
		} catch (IllegalArgumentException e) {
			Assert.assertNotNull("No message associated to the exception.", e.getMessage());
		} catch (Exception e) {
			Assert.fail("An incorrect exception was thrown.");
		}
	}
}
