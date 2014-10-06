package fr.univtls2.web.moviesearch.services.indexation.weighting.rule;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import fr.univtls2.web.moviesearch.model.SourceDoc;
import fr.univtls2.web.moviesearch.model.Term;
import fr.univtls2.web.moviesearch.model.builders.SourceDocBuilder;
import fr.univtls2.web.moviesearch.model.builders.TermBuilder;
import fr.univtls2.web.moviesearch.services.indexation.weighting.rules.RobertsonRule;
import fr.univtls2.web.moviesearch.services.indexation.weighting.rules.WeightingRule;

/**
 * <p>Simple test of {@link RobertsonRule} implementation.</p>
 *
 * @author Vianney Dupoy de Guitard
 */
public class RobertsonRuleTest {

	/** The rule to test. */
	private WeightingRule rule = new RobertsonRule();

	/**
	 * Test of {@link RobertsonRule#weight(SourceDoc, Term)}.
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
		double expectedWeightA = 3 / ( 3 + 0.5 + 1.5 * ( 2500 / 1275D ) );
		double expectedWeightB = 2 / ( 2 + 0.5 + 1.5 * ( 50 / 1275D ) );

		// Rule call.
		double actualWeightA = rule.weight(docA, term);
		double actualWeightB = rule.weight(docB, term);

		// Assertions.
		Assert.assertEquals("Incorrect weight.", expectedWeightA, actualWeightA, 0);
		Assert.assertEquals("Incorrect weight.", expectedWeightB, actualWeightB, 0);
	}
}
