package fr.univtls2.web.moviesearch.services.indexation.normalization.rules;


import org.junit.Assert;
import org.junit.Test;

import fr.univtls2.web.moviesearch.model.Term;
import fr.univtls2.web.moviesearch.model.builders.TermBuilder;
import fr.univtls2.web.moviesearch.services.indexation.normalization.rules.TransformationRule;
import fr.univtls2.web.moviesearch.services.indexation.normalization.rules.TruncateRule;

/**
 * <p>Simple test for {@link TruncateRule} transformation rule.</p>
 *
 * @author Vianney Dupoy de Guitard
 */
public class TruncateRuleTest {

	/** Test data for a short term. */
	private static final String SHORT = "AB";

	/** Result expected for a short term. */
	private static final String SHORT_EXPECTED = "AB";

	/** Test data for a short term. */
	private static final String LONG = "ABCDEFGHIJKLM";

	/** Result expected for a short term. */
	private static final String LONG_EXPECTED = "ABCDEFG";

	/** The rule to test. */
	private TransformationRule rule = new TruncateRule();

	/**
	 * Tests the rule with a very short term.
	 */
	@Test
	public void shortTermTest() {
		Term term = new TermBuilder().word(SHORT).create();
		Term result = rule.transform(term);
		Assert.assertEquals("Rule failed with a short term.", SHORT_EXPECTED, result.getWord());
	}

	/**
	 * Tests the rule with a very long term.
	 */
	@Test
	public void longTermTest() {
		Term term = new TermBuilder().word(LONG).create();
		Term result = rule.transform(term);
		Assert.assertEquals("Rule failed with a long term.", LONG_EXPECTED, result.getWord());
	}
}
