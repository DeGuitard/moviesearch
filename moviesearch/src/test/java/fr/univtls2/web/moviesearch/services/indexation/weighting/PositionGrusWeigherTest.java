package fr.univtls2.web.moviesearch.services.indexation.weighting;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import fr.univtls2.web.moviesearch.model.SourceDoc;

public class PositionGrusWeigherTest {

	private PositionGrusWeigher rule = new PositionGrusWeigher();

	@Test
	public void simpleTest() {
		SourceDoc doc = new SourceDoc("test");
		List<Integer> posListA = Arrays.asList(1, 5, 10);
		List<Integer> posListB = Arrays.asList(19, 40);
		List<Integer> posListC = Arrays.asList(18);
		List<Integer> posListD = Arrays.asList(25);

		doc.getPositionsList().add(posListA);
		doc.getPositionsList().add(posListB);
		doc.getPositionsList().add(posListC);
		doc.getPositionsList().add(posListD);

		rule.weight(doc);
		System.out.println(doc.getWeight());
		Assert.assertTrue(doc.getWeight() == 11);
	}
}
