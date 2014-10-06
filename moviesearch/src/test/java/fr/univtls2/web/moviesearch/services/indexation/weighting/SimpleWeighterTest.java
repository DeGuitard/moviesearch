package fr.univtls2.web.moviesearch.services.indexation.weighting;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
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
import fr.univtls2.web.moviesearch.services.indexation.weighting.rules.RobertsonRule;

/**
 * <p>Test of {@link SimpleWeighter} implementation.</p>
 *
 * @author Vianney Dupoy de Guitard
 */
@RunWith(MovieSearchTestRunner.class)
@GuiceModules(MovieSearchModule.class)
public class SimpleWeighterTest {

	/** The weighter to test. */
	@Inject
	private SimpleWeighter weighter;

	/**
	 * Test of {@link SimpleWeighter#weight(SourceDoc, Term)}.
	 */
	@Test
	public void test() {
		// Test data creation.
		List<SourceDoc> docs = new ArrayList<SourceDoc>();
		SourceDoc docA = new SourceDocBuilder().url("test1").occurrences(3).size(2500).create();
		SourceDoc docB = new SourceDocBuilder().url("test2").occurrences(2).size(50).create();
		docs.add(docA);
		docs.add(docB);
		Term term = new TermBuilder().word("test").sourceDocs(docs).create();

		// Calculation of expectations.
		RobertsonRule rule = new RobertsonRule();
		double expectedWeightA = rule.weight(docA, term);
		double expectedWeightB = rule.weight(docB, term);

		// Weighting.
		weighter.weight(docA, term);
		weighter.weight(docB, term);

		// Assertions.
		Assert.assertEquals("Unexpected weight.", expectedWeightA, docA.getWeight(), 0);
		Assert.assertEquals("Unexpected weight.", expectedWeightB, docB.getWeight(), 0);
	}
}
