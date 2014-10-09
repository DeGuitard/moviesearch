package fr.univtls2.web.moviesearch.services.indexation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import fr.univtls2.web.moviesearch.model.GlobalStat;
import fr.univtls2.web.moviesearch.services.persistence.dao.GlobalStatDao;

/**
 * <p>Start a multi-threaded indexation service, that will import a folder.</p>
 *
 * @author Vianney Dupoy de Guitard
 */
public class ImportServiceImpl implements ImportService {

	/** Applicative logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ImportServiceImpl.class);

	/** Amount of threads to start. */
	private static final int THREAD_COUNT = 4;

	/** The global statistics dao. */
	@Inject private GlobalStatDao statDao;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start(final File directory) {
		if (!directory.isDirectory()) {
			LOGGER.error("No directory supplied.");
			throw new IllegalArgumentException("No directory supplied.");
		}

		LOGGER.info("Full import of '{}' starting.", directory.getAbsolutePath());
		int filesCount = directory.listFiles().length;
		GlobalStat filesCountStat = statDao.findByKey(GlobalStat.KEY_ALL_DOCS_COUNT);
		filesCountStat.setValue(filesCount);
		statDao.saveOrUpdate(filesCountStat);

		long start = System.currentTimeMillis();

		// Splits the list of files into several packs, one for each thread.
		List<List<File>> fileListLists = split(directory.listFiles(), THREAD_COUNT);

		// Start the threads.
		ExecutorService es = Executors.newCachedThreadPool();
		for (List<File> fileList : fileListLists) {
			Runnable runnable = new ImportServiceImplRunnable(fileList);
			es.execute(runnable);
		}
		es.shutdown();
		try {
			es.awaitTermination(60, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			LOGGER.error("Thread aborption.", e);
		}
		long end = System.currentTimeMillis();
		LOGGER.info("Duration: {}s.", (end - start) / 1000);
	}

	/**
	 * Splits a list into a subset of lists.
	 * @param list : the list to split.
	 * @param size : the amount of lists to return.
	 * @return the split list.
	 */
	public <T> List<List<T>> split(T[] list, int size) {
	    List<List<T>> result = new ArrayList<List<T>>(size);

	    for (int i = 0; i < size; i++) {
	        result.add(new ArrayList<T>());
	    }

	    int index = 0;
	    for (T t : list) {
	        result.get(index).add(t);
	        index = (index + 1) % size;
	    }

	    return result;
	}
}
