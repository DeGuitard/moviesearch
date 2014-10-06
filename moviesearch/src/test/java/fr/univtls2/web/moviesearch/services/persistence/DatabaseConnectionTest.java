package fr.univtls2.web.moviesearch.services.persistence;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import fr.univtls2.web.moviesearch.MovieSearchTestRunner;
import fr.univtls2.web.moviesearch.MovieSearchTestRunner.GuiceModules;
import fr.univtls2.web.moviesearch.services.MovieSearchModule;

/**
 * Test the database connection.
 *
 * @author Vianney Dupoy de Guitard
 */
@RunWith(MovieSearchTestRunner.class)
@GuiceModules(MovieSearchModule.class)
public class DatabaseConnectionTest {

	@Inject
	private DatabaseConnection co;

	@Test
	public void connectionTest() {
		Assert.assertNotNull("The database connection is null.", co.getDb());
	}

	@Test
	public void crudTest() {
		int initialColCount = co.getDb().getCollectionNames().size();

		// Create
		DBCollection testCol = co.getDb().createCollection("TestCollection", null);
		Assert.assertNotNull("The collection could not be created", testCol);
		DBObject testObj = new BasicDBObject().append("key", "value");
		testCol.insert(testObj);

		// Read
		DBCollection gatheredCol = co.getDb().getCollection(testCol.getName());
		Assert.assertNotNull("The collection could not be gathered.", gatheredCol);
		DBObject gatheredObj = gatheredCol.findOne();
		Assert.assertNotNull("The object could not be gathered.", gatheredObj);

		// Update (& read to check)
		BasicDBObject searchQuery = new BasicDBObject().append("key", "value");
		BasicDBObject updateValue = new BasicDBObject().append("key", "val");
		gatheredCol.update(searchQuery, updateValue);
		DBObject updatedObj = gatheredCol.findOne();
		Assert.assertEquals("The object hasn't been updated.", "val", updatedObj.get("key"));

		// Delete
		gatheredCol.drop();
		Assert.assertEquals("The collection could not be removed.", initialColCount, co.getDb().getCollectionNames().size());
	}
}
