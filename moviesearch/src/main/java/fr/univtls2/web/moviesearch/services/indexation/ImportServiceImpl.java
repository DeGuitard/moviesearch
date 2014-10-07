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

import fr.univtls2.web.moviesearch.model.SourceDoc;
import fr.univtls2.web.moviesearch.model.Term;
import fr.univtls2.web.moviesearch.services.indexation.extraction.Extractor;
import fr.univtls2.web.moviesearch.services.indexation.normalization.Normalizer;
import fr.univtls2.web.moviesearch.services.indexation.weighting.Weigher;
import fr.univtls2.web.moviesearch.services.persistence.dao.TermDao;

public class ImportServiceImpl implements ImportService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImportServiceImpl.class);

	@Inject	private Extractor extractor;
	@Inject private Normalizer normalizer;
	@Inject private Weigher weighter;
	@Inject private TermDao dao;

	@Override
	public void start(final File directory) {
		if (!directory.isDirectory()) {
			LOGGER.error("No directory supplied.");
			throw new IllegalArgumentException("No directory supplied.");
		}

		LOGGER.info("Full of '{}' import starting.", directory.getAbsolutePath());
		float progress = 0;
		float filesCount = directory.listFiles().length;
		for (File file : directory.listFiles()) {
			try {
				Document doc = Jsoup.parse(file, "UTF-8");
				List<Term> terms = extractor.extract(doc);
				terms = normalizer.normalize(terms);
				for (Term term : terms) {
					for (SourceDoc srcDoc : term.getDocuments()) {
						weighter.weight(srcDoc, term);
					}
				}
				terms = mergeWithDatabase(terms);
				dao.saveOrUpdate(terms);
			} catch (IOException e) {
				LOGGER.error("I/O error during import.", e);
			}
			progress++;
			LOGGER.info("{}% done.", (progress / filesCount) * 100f);
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
		Deque<Term> dbTerms = dao.findByWords(terms);
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
			dao.saveOrUpdate(updatedTerms);
			terms.removeAll(updatedTerms);
		}
		return terms;
	}
}
