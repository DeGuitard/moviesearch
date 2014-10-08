package fr.univtls2.web.moviesearch.services.indexation.normalization.rules;

import org.tartarus.snowball.ext.frenchStemmer;

import fr.univtls2.web.moviesearch.model.Term;
import fr.univtls2.web.moviesearch.model.builders.TermBuilder;

/**
 * <p>Simple rule that execute a french stemming.</p>
 * <p>For instance: 'continuation' => 'continu'.</p>
 *
 * @author Vianney Dupoy de Guitard
 *
 */
public class StemmingRule implements TransformationRule {

	/** The stemming implementation to use. **/
	private frenchStemmer stemmer = new frenchStemmer();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Term transform(Term termToTransform) {
		// Truncates the word.
		stemmer.setCurrent(termToTransform.getWord());
		stemmer.stem();
		Term normalized = new TermBuilder().word(stemmer.getCurrent()).sourceDocs(termToTransform.getDocuments()).create();

		return normalized;
	}
}
