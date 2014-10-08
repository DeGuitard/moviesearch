package fr.univtls2.web.moviesearch;

import org.junit.Assert;
import org.junit.Test;

public class MovieSearchTest {

	@Test
	public void testSearchOk() {
		try {
			MovieSearch.main("search", "lieu, naissance, Omar Sy");
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Exception was thrown!");
		}
	}

	@Test
	public void testSearchKo() {
		try {
			MovieSearch.main("searh", "membre, jury, Globes de Cristal, 2012");
			Assert.fail("No exception was thrown.");
		} catch (Exception e) {
			Assert.assertNotNull(e.getMessage());
		}

		try {
			MovieSearch.main("search");
			Assert.fail("No exception was thrown.");
		} catch (Exception e) {
			Assert.assertNotNull(e.getMessage());
		}
	}
}
