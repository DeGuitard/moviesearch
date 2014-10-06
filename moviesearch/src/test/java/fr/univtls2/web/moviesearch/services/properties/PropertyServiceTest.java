package fr.univtls2.web.moviesearch.services.properties;

import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;

import fr.univtls2.web.moviesearch.MovieSearchTestRunner;
import fr.univtls2.web.moviesearch.MovieSearchTestRunner.GuiceModules;
import fr.univtls2.web.moviesearch.services.MovieSearchModule;

@RunWith(MovieSearchTestRunner.class)
@GuiceModules(MovieSearchModule.class)
public class PropertyServiceTest {

	private static final String TEST_KEY = "database.host";

	@Inject
	private PropertyService propertyService;

	@Test
	public void readPropertyTest() {
		try {
			Assert.assertNotNull(propertyService.getValue(TEST_KEY));
		} catch (NoSuchElementException e) {
			Assert.fail("Property could not be found.");
		}
	}

	@Test
	public void writePropertyTest() {
		try {
			String oldValue = propertyService.getValue(TEST_KEY);
			String newValue = "test";

			propertyService.setValue(TEST_KEY, newValue);
			Assert.assertEquals("The value of the property is different from what it should be.", newValue, propertyService.getValue(TEST_KEY));
			propertyService.setValue(TEST_KEY, oldValue);
			Assert.assertEquals("The value of the property is different from what it should be.", oldValue, propertyService.getValue(TEST_KEY));
		} catch (NoSuchElementException e) {
			Assert.fail("Old property vaue could not be read.");
		}
	}
}
