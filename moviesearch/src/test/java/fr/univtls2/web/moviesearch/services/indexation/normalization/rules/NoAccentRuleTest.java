package fr.univtls2.web.moviesearch.services.indexation.normalization.rules;


import org.junit.Assert;
import org.junit.Test;

import fr.univtls2.web.moviesearch.model.Term;
import fr.univtls2.web.moviesearch.model.builders.TermBuilder;

/**
 * <p>Simple test for {@link TruncateRule} transformation rule.</p>
 *
 * @author Vianney Dupoy de Guitard
 */
public class NoAccentRuleTest {

	/** Test data for a short term. */
	private static final String ACCENTED = "êlíôüà";

	/** Result expected for a short term. */
	private static final String NO_ACCENT_EXPECTED = "elioua";

	/** The rule to test. */
	private TransformationRule rule = new NoAccentRule();

	/**
	 * Tests the rule with a accented term.
	 */
	@Test
	public void shortTermTest() {
		Term term = new TermBuilder().word(ACCENTED).create();
		Term result = rule.transform(term);
		Assert.assertEquals("Rule failed to remove accents.", NO_ACCENT_EXPECTED, result.getWord());
	}
}
