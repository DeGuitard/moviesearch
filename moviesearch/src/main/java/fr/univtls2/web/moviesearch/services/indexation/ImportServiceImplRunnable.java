package fr.univtls2.web.moviesearch.services.indexation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import fr.univtls2.web.moviesearch.MovieSearch;
import fr.univtls2.web.moviesearch.model.SourceDoc;
import fr.univtls2.web.moviesearch.model.Term;
import fr.univtls2.web.moviesearch.services.indexation.extraction.Extractor;
import fr.univtls2.web.moviesearch.services.indexation.normalization.Normalizer;
import fr.univtls2.web.moviesearch.services.indexation.weighting.Weigher;
import fr.univtls2.web.moviesearch.services.persistence.dao.TermDao;

public class ImportServiceImplRunnable implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImportServiceImplRunnable.class);

	@Inject private Extractor extractor;
	@Inject private Normalizer normalizer;
	@Inject private Weigher weighter;
	@Inject private TermDao termDao;

	private List<File> fileList;

	public ImportServiceImplRunnable(List<File> fileList) {
		this.fileList = fileList;
		MovieSearch.getInjector().injectMembers(this);
	}

	@Override
	public void run() {
		for (File file : fileList) {
			importFile(file);
		}
	}

	private void importFile(final File file) {
		try {
			Document doc = Jsoup.parse(file, "UTF-8");
			doc.setBaseUri(file.getAbsolutePath());
			List<Term> terms = extractor.extract(doc);
			terms = normalizer.normalize(terms);
			for (Term term : terms) {
				for (SourceDoc srcDoc : term.getDocuments()) {
					weighter.weight(srcDoc, term);
				}
			}
			terms = mergeWithDatabase(terms);
			termDao.saveOrUpdate(terms);
			LOGGER.info("File {} imported.", file.getAbsolutePath());
		} catch (IOException e) {
			LOGGER.error("I/O error during import.", e);
		}
	}

	/**
	 * <p>Merge the collected data with the data from database.</p>
	 * @param terms : the terms to merge with the data from database.
	 */
	private List<Term> mergeWithDatabase(List<Term> terms) {
		// List of updated terms.
		List<Term> updatedTerms = new ArrayList<Term>();

		// Looks for all the terms already in the database.
		Deque<Term> dbTerms = termDao.findByWords(terms);
		for (Term dbTerm : dbTerms) {
			int termIndex = terms.indexOf(dbTerm);
			Term termToMerge = terms.get(termIndex);

			// Updates the document list (remove/add to be sure there's no duplicate).
			dbTerm.getDocuments().removeAll(termToMerge.getDocuments());
			dbTerm.getDocuments().addAll(termToMerge.getDocuments());
			updatedTerms.add(dbTerm);
		}

		// Save the updated terms and remove them from the list of new terms.
		if (updatedTerms.size() > 0) {
			termDao.saveOrUpdate(updatedTerms);
			terms.removeAll(updatedTerms);
		}
		return terms;
	}
}
