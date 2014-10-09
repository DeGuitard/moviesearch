package fr.univtls2.web.moviesearch.services.indexation.normalization.rules;

import org.junit.Assert;
import org.junit.Test;

import fr.univtls2.web.moviesearch.model.Term;
import fr.univtls2.web.moviesearch.model.builders.TermBuilder;

public class TreeTaggingRuleTest {

	private TreeTaggingRule rule = new TreeTaggingRule();

	@Test
	public void simpleTest() {
		Term term = new TermBuilder().word("né").create();
		Term normalized = rule.transform(term);
		Assert.assertEquals("naître", normalized.getWord());
	}
}
