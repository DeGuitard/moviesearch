package fr.univtls2.web.moviesearch.services.indexation.weighting.searchrules;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import fr.univtls2.web.moviesearch.model.SourceDoc;

public class PositionWeigherTest {

	private PositionWeigher rule = new PositionWeigher();

	@Test
	public void simpleTest() {
		SourceDoc doc = new SourceDoc("test");
		List<Integer> posListA = Arrays.asList(132);
		List<Integer> posListB = Arrays.asList(122, 140, 11);
		List<Integer> posListC = Arrays.asList(152, 13);
		doc.getPositionsList().add(posListA);
		doc.getPositionsList().add(posListB);
		doc.getPositionsList().add(posListC);

		rule.weight(doc);
		Assert.assertTrue(doc.getWeight() > 0);
	}
}
