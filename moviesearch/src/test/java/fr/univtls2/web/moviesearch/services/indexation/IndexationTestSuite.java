package fr.univtls2.web.moviesearch.services.indexation;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import fr.univtls2.web.moviesearch.services.indexation.extraction.SimpleExtractorTest;
import fr.univtls2.web.moviesearch.services.indexation.normalization.SimpleNormalizerTest;
import fr.univtls2.web.moviesearch.services.indexation.normalization.rules.TruncateRuleTest;
import fr.univtls2.web.moviesearch.services.indexation.weighting.SimpleWeighterTest;
import fr.univtls2.web.moviesearch.services.indexation.weighting.rule.RobertsonRuleTest;

@RunWith(Suite.class)
@SuiteClasses({
	SimpleExtractorTest.class,
	SimpleNormalizerTest.class,
	SimpleWeighterTest.class,
	TruncateRuleTest.class,
	RobertsonRuleTest.class
})
public class IndexationTestSuite { }
