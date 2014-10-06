package fr.univtls2.web.moviesearch.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.univtls2.web.moviesearch.model.builders.SourceDocBuilder;

public class SourceDocTest {

	private SourceDoc docA;
	private SourceDoc docB;
	private SourceDoc docC;

	@Before
	public void setUp() {
		docA = new SourceDocBuilder().url("test").create();
		docB = new SourceDocBuilder().url("test").occurrences(250).create();
		docC = new SourceDocBuilder().url("test 2").create();
	}

	@Test
	public void testEquals() {
		Assert.assertEquals("Documents should be equal.", docA, docB);
		Assert.assertNotEquals("Documents should not be equal.", docA, docC);
	}

	@Test
	public void testHashcode() {
		Assert.assertEquals("Documents should have the same hashcode.", docA.hashCode(), docB.hashCode());
		Assert.assertNotEquals("Documents should not have the same hashcode.", docA.hashCode(), docC.hashCode());
	}

}
