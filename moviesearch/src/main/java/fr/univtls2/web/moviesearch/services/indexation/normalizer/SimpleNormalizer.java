package fr.univtls2.web.moviesearch.services.indexation.normalizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.ObjectUtils;

import com.google.inject.Inject;

import fr.univtls2.web.moviesearch.model.SourceDoc;
import fr.univtls2.web.moviesearch.model.Term;
import fr.univtls2.web.moviesearch.services.indexation.normalizer.rules.TransformationRule;

/**
 * <p>Simple implementation of {@link Normalizer}.</p>
 * <p>Uses all available transform rules.</p>
 *
 * @author Vianney Dupoy de Guitard
 */
public class SimpleNormalizer implements Normalizer {

	/** Transformations rules to apply. */
	@Inject
	private Set<TransformationRule> transformRules;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Term> normalize(List<Term> terms) {
		List<Term> normalizedList = new ArrayList<>();

		// Iterates through all terms.
		for (Term term : terms) {

			// Applies transformation rules.
			Term newTerm = (Term) ObjectUtils.cloneIfPossible(term);
			for (TransformationRule transformRule : transformRules) {
				newTerm = transformRule.transform(newTerm);
			}

			int termIndex = normalizedList.indexOf(newTerm);
			if (termIndex == -1) {
				// New term to add.
				normalizedList.add(newTerm);
			} else {
				// Term to update.
				Term termToUpdate = normalizedList.get(termIndex);
				updateTerm(termToUpdate, newTerm);
			}
		}
		return normalizedList;
	}

	/**
	 * Updates a term.
	 * @param termToUpdate : the term to update.
	 * @param newTerm : the new term to merge with the previous one.
	 */
	private void updateTerm(Term termToUpdate, Term newTerm) {
		// Merge the source documents.
		for (SourceDoc newDoc : newTerm.getDocuments()) {
			int docIndex = termToUpdate.getDocuments().indexOf(newDoc);

			if (docIndex == -1) {
				// New document to add.
				termToUpdate.getDocuments().add(newDoc);
			} else {
				// Old document to update.
				SourceDoc docToUpdate = termToUpdate.getDocuments().get(docIndex);
				int oldOccurrences = docToUpdate.getOccurrences();
				docToUpdate.setOccurrences(newDoc.getOccurrences() + oldOccurrences);
				docToUpdate.getTags().addAll(newDoc.getTags());
				docToUpdate.getPositions().addAll(newDoc.getPositions());
			}
		}
	}
}
