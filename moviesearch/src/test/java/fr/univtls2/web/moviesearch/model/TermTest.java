package fr.univtls2.web.moviesearch.model;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.univtls2.web.moviesearch.model.builders.SourceDocBuilder;
import fr.univtls2.web.moviesearch.model.builders.TermBuilder;

public class TermTest {

	private Term termA;
	private Term termB;
	private Term termC;

	@Before
	public void setUp() {
		SourceDoc doc = new SourceDocBuilder().url("test").create();
		termA = new TermBuilder().word("test").create();
		termB = new TermBuilder().word("test").sourceDocs(Arrays.asList(doc)).create();
		termC = new TermBuilder().word("test 2").create();
	}

	@Test
	public void testEquals() {
		Assert.assertEquals("Terms are not equal whereas they should.", termA, termB);
		Assert.assertNotEquals("Terms are equal whereas they should not.", termA, termC);
	}

	@Test
	public void testHashcode() {
		Assert.assertEquals("Terms have not the same hashcode whereas they should.", termA.hashCode(), termB.hashCode());
		Assert.assertNotEquals("Terms share the same hashcode whereas they should not.", termA.hashCode(), termC.hashCode());
	}

}
