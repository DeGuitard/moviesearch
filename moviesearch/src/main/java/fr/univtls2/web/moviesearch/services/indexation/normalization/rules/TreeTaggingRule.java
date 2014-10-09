package fr.univtls2.web.moviesearch.services.indexation.normalization.rules;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

import org.annolab.tt4j.TokenHandler;
import org.annolab.tt4j.TreeTaggerException;
import org.annolab.tt4j.TreeTaggerWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.univtls2.web.moviesearch.model.Term;
import fr.univtls2.web.moviesearch.model.builders.TermBuilder;

/**
 * <p>Simple rule that execute a french stemming with TreeTagger.</p>
 * <p>For instance: 'continuation' => 'continu'.</p>
 *
 * @author Vianney Dupoy de Guitard
 *
 */
public class TreeTaggingRule implements TransformationRule, TokenHandler<String> {

	/** Applicative logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(TreeTaggingRule.class);

	/** The stemming implementation to use. **/
	private TreeTaggerWrapper<String> treeTagger = new TreeTaggerWrapper<>();

	/** The lemma found. */
	private String lemma;

	private CountDownLatch latch;

	/**
	 * Default constructor.
	 */
	public TreeTaggingRule() {
		System.setProperty("treetagger.home", "src/main/resources/tree-tagger/");
		try {
			treeTagger.setModel(new File("src/main/resources/tree-tagger/models/french-utf8.bin").getAbsolutePath());
			treeTagger.setHandler(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Term transform(Term termToTransform) {
		latch = new CountDownLatch(1);
		try {
			treeTagger.process(Arrays.asList(termToTransform.getWord()));
			latch.await();
		} catch (IOException | InterruptedException | TreeTaggerException e) {
			LOGGER.error("Could not execute TreeTagger.", e);
		}
		Term normalized = new TermBuilder().word(lemma).sourceDocs(termToTransform.getDocuments()).create();
		return normalized;
	}

	@Override
	public void token(String token, String pos, String lemma) {
		this.lemma = lemma;
		latch.countDown();
	}
}
