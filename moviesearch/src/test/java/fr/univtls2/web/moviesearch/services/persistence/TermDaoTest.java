package fr.univtls2.web.moviesearch.services.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;

import fr.univtls2.web.moviesearch.MovieSearchTestRunner;
import fr.univtls2.web.moviesearch.MovieSearchTestRunner.GuiceModules;
import fr.univtls2.web.moviesearch.model.Term;
import fr.univtls2.web.moviesearch.model.builders.SourceDocBuilder;
import fr.univtls2.web.moviesearch.model.builders.TermBuilder;
import fr.univtls2.web.moviesearch.services.MovieSearchModule;
import fr.univtls2.web.moviesearch.services.persistence.dao.TermDao;

/**
 * Tests the Generic DAO implementation, using Term.
 * Checks the CRUD operations and the bulk operations.
 *
 * @author Vianney Dupoy de Guitard
 */
@RunWith(MovieSearchTestRunner.class)
@GuiceModules(MovieSearchModule.class)
public class TermDaoTest {

	/** DAO to test. */
	@Inject
	private TermDao dao;

	/**
	 * Tests basic CRUD operations.
	 */
	@Test
	public void crudTest() {
		// Create
		Term term = new TermBuilder().word("Unit test").create();
		dao.saveOrUpdate(term);
		Assert.assertNotNull("Term was not saved.", term.getId());

		// Read
		try {
			Term termFound = dao.findOne(term);
			Assert.assertNotNull("Term returned is null.", termFound);
		} catch (NoSuchElementException e) {
			Assert.fail("Wasn't able to find the term to read.");
		}

		// Update
		try {
			term.getDocuments().add(new SourceDocBuilder().url("test").create());
			dao.saveOrUpdate(term);
			Term termFound = dao.findOne(term);
			Assert.assertEquals("Term wasn't updated.", term.getDocuments(), termFound.getDocuments());
		} catch (NoSuchElementException e) {
			Assert.fail("Updated term wasn't found.");
		}

		// Delete
		try {
			dao.delete(term);
			dao.findOne(term);
			Assert.fail("Term wasn't removed.");
		} catch (NoSuchElementException e) { }
	}

	/**
	 * Tests bulk operations : create, read, update & delete.
	 */
	@Test
	public void bulkTest() {
		int entitiesToInsert = 2000;
		int initialCount = dao.countAll();

		// Bulk insert.
		List<Term> terms = new ArrayList<Term>();
		for (int i = 0; i < entitiesToInsert; i++) {
			Term term = new TermBuilder().word("Unit Test " + i).create();
			terms.add(term);
		}
		dao.saveOrUpdate(terms);
		int endCount = dao.countAll();
		Assert.assertEquals("The bulk insert has failed.", initialCount + entitiesToInsert, endCount);

		// Bulk update.
		for (Term term : terms) {
			term.getDocuments().add(new SourceDocBuilder().url("test").create());
		}
		dao.saveOrUpdate(terms);
		endCount = dao.countAll();
		Assert.assertEquals("The bulk insert has failed.", initialCount + entitiesToInsert, endCount);

		try {
			Term term = dao.findOne(terms.get(0));
			Assert.assertNotNull("The found term was null.", term);
			Assert.assertEquals("The year wasn't updated.", new SourceDocBuilder().url("test").create(), term.getDocuments().get(0));
		} catch (NoSuchElementException e) {
			Assert.fail("One inserted term was not found.");
		}

		// Bulk read
		int readCount = 0;
		for (Term term : dao.findAll()) {
			term.getId();
			readCount++;
		}
		Assert.assertEquals(initialCount + entitiesToInsert, readCount);

		// Bulk delete.
		dao.delete(terms);
		Assert.assertEquals("The terms weren't removed.", initialCount, dao.countAll());
	}
}
