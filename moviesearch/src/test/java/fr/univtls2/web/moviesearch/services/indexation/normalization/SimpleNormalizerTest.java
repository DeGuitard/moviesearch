package fr.univtls2.web.moviesearch.services.indexation.normalization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;

import fr.univtls2.web.moviesearch.MovieSearchTestRunner;
import fr.univtls2.web.moviesearch.MovieSearchTestRunner.GuiceModules;
import fr.univtls2.web.moviesearch.model.SourceDoc;
import fr.univtls2.web.moviesearch.model.Term;
import fr.univtls2.web.moviesearch.model.builders.SourceDocBuilder;
import fr.univtls2.web.moviesearch.model.builders.TermBuilder;
import fr.univtls2.web.moviesearch.services.MovieSearchModule;
import fr.univtls2.web.moviesearch.services.indexation.normalization.SimpleNormalizer;

/**
 * Test class for {@link SimpleNormalizer} implementation.
 *
 * @author Vianney Dupoy de Guitard
 */
@RunWith(MovieSearchTestRunner.class)
@GuiceModules(MovieSearchModule.class)
public class SimpleNormalizerTest {

	/** An expected term. */
	private static final String EXPECTED_TERM_A = "documen";

	/** An expected term. */
	private static final String EXPECTED_TERM_B = "bacon";

	/** Expected occurrences of term A. */
	private static final int OCCURRENCES_TERM_A = 8;

	/** Expected occurrences of term B. */
	private static final int OCCURRENCES_TERM_B = 3;

	/** The normalizer to test. */
	@Inject
	private SimpleNormalizer normalizer;

	/** Test data. */
	private List<Term> terms;

	/**
	 * Test data initialization.
	 */
	@Before
	public void setUp() {
		// Creates the documents associated to the terms.
		SourceDoc srcDocA = new SourceDocBuilder().url("test").occurrences(2).create();
		SourceDoc srcDocB = new SourceDocBuilder().url("test").occurrences(5).create();
		SourceDoc srcDocC = new SourceDocBuilder().url("test").occurrences(1).create();
		SourceDoc srcDocD = new SourceDocBuilder().url("test").occurrences(3).create();

		// Creates the terms.
		Term termA = new TermBuilder().word(EXPECTED_TERM_A + "tation").sourceDocs(new ArrayList<>(Arrays.asList(srcDocA))).create();
		Term termB = new TermBuilder().word(EXPECTED_TERM_A + "ts").sourceDocs(new ArrayList<>(Arrays.asList(srcDocB))).create();
		Term termC = new TermBuilder().word(EXPECTED_TERM_A + "ter").sourceDocs(new ArrayList<>(Arrays.asList(srcDocC))).create();
		Term termD = new TermBuilder().word(EXPECTED_TERM_B).sourceDocs(new ArrayList<>(Arrays.asList(srcDocD))).create();

		// Adds the terms to the list.
		terms = Arrays.asList(termA, termB, termC, termD);
	}

	/**
	 * Test of {@link SimpleNormalizer#normalize(List)} method.
	 */
	@Test
	public void normalitionTest() {
		// Calls the service.
		terms = normalizer.normalize(terms);
		Assert.assertEquals("Unexpected number of terms.", 2, terms.size());

		// Checks that no term is missing.
		Term expectedTermA = new TermBuilder().word(EXPECTED_TERM_A).create();
		Term expectedTermB = new TermBuilder().word(EXPECTED_TERM_B).create();
		Assert.assertTrue("A term is missing.", terms.contains(expectedTermA));
		Assert.assertTrue("A term is missing.", terms.contains(expectedTermB));

		// Checks the source document merge.
		for (Term term : terms) {
			if (term.getWord().equals(EXPECTED_TERM_A)) {
				int actualOccurrences = term.getDocuments().get(0).getOccurrences();
				Assert.assertEquals("Incorrect occurrences.", OCCURRENCES_TERM_A, actualOccurrences);
			} else if (term.getWord().equals(EXPECTED_TERM_B)) {
				int actualOccurrences = term.getDocuments().get(0).getOccurrences();
				Assert.assertEquals("Incorrect occurrences.", OCCURRENCES_TERM_B, actualOccurrences);
			}
		}
	}
}
