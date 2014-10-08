package fr.univtls2.web.moviesearch.services.persistence;

import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;

import fr.univtls2.web.moviesearch.MovieSearchTestRunner;
import fr.univtls2.web.moviesearch.MovieSearchTestRunner.GuiceModules;
import fr.univtls2.web.moviesearch.model.GlobalStat;
import fr.univtls2.web.moviesearch.model.builders.GlobalStatBuilder;
import fr.univtls2.web.moviesearch.services.MovieSearchModule;
import fr.univtls2.web.moviesearch.services.persistence.dao.GlobalStatDao;

/**
 * Tests the Generic DAO implementation, using Term.
 * Checks the CRUD operations and the bulk operations.
 *
 * @author Vianney Dupoy de Guitard
 */
@RunWith(MovieSearchTestRunner.class)
@GuiceModules(MovieSearchModule.class)
public class GlobalStatDaoTest {

	/** DAO to test. */
	@Inject
	private GlobalStatDao dao;

	/**
	 * Tests basic CRUD operations.
	 */
	@Test
	public void crudTest() {
		// Create
		GlobalStat stat = new GlobalStatBuilder().key("unit").value("test").create();
		dao.saveOrUpdate(stat);
		Assert.assertNotNull("Stat was not saved.", stat.getId());

		// Read
		try {
			GlobalStat statFound = dao.findOne(stat);
			Assert.assertNotNull("Term returned is null.", statFound);
		} catch (NoSuchElementException e) {
			Assert.fail("Wasn't able to find the stat to read.");
		}

		// Update
		try {
			stat.setValue("test2");
			dao.saveOrUpdate(stat);
			GlobalStat statFound = dao.findOne(stat);
			Assert.assertEquals("Stat wasn't updated.", stat.getValue(), statFound.getValue());
		} catch (NoSuchElementException e) {
			Assert.fail("Updated stat wasn't found.");
		}

		// Delete
		try {
			dao.delete(stat);
			dao.findOne(stat);
			Assert.fail("stat wasn't removed.");
		} catch (NoSuchElementException e) { }
	}
}
