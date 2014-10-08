package fr.univtls2.web.moviesearch.services.indexation.weighting.rule;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import fr.univtls2.web.moviesearch.model.SourceDoc;
import fr.univtls2.web.moviesearch.model.builders.SourceDocBuilder;
import fr.univtls2.web.moviesearch.services.indexation.weighting.rules.TaggingRule;

public class TaggingRuleTest {

	private TaggingRule rule = new TaggingRule();

	@Test
	public void simpleTest() {
		List<String> tags = Arrays.asList("p", "title");
		SourceDoc doc = new SourceDocBuilder().url("test").tags(tags).create();
		Assert.assertTrue(doc.getWeight() == 0);
		rule.weight(doc, null);
		Assert.assertTrue(doc.getWeight() > 0);
	}
}
