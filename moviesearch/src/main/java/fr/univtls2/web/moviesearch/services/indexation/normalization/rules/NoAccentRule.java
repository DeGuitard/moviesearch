package fr.univtls2.web.moviesearch.services.indexation.normalization.rules;

import fr.univtls2.web.moviesearch.model.Term;
import fr.univtls2.web.moviesearch.model.builders.TermBuilder;

/**
 * <p>Simple rule that removes all accents.</p>
 * <p>For instance: 'récupéré' => 'recupere'.</p>
 *
 * @author Vianney Dupoy de Guitard
 *
 */
public class NoAccentRule implements TransformationRule {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Term transform(Term termToTransform) {
		// Removes the accents.
		String noAccentWord = termToTransform.getWord().replaceAll("[éëèê]", "e");
		noAccentWord = noAccentWord.replaceAll("[àâáä]", "a");
		noAccentWord = noAccentWord.replaceAll("[íìîï]", "i");
		noAccentWord = noAccentWord.replaceAll("[óòôö]", "o");
		noAccentWord = noAccentWord.replaceAll("[úùûü]", "u");
		Term normalized = new TermBuilder().word(noAccentWord).sourceDocs(termToTransform.getDocuments()).create();

		return normalized;
	}
}
