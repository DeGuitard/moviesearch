package fr.univtls2.web.moviesearch.services.indexation.weighting;

import java.util.List;

import fr.univtls2.web.moviesearch.model.SourceDoc;

/**
 * Compute the weigh of the document found.
 * @author Guillaume Ruscassie
 */
public class PositionGrusWeigher implements SearchWeigher {

	private static int MAX_DISTANCE = 20;
	private static int WEIGHT = 1;

	@Override
	public void weight(final SourceDoc doc) {

		// lists of positions list
		final List<List<Integer>> positionsList = doc.getPositionsList();
		int found = 0;
		for (int id = 0; id < positionsList.size() - 1; id++) {
			// We compare with the next lists
			final List<List<Integer>> listToTest = positionsList.subList(id + 1, positionsList.size());
			found = found + searchNumberTerm(positionsList.get(id), listToTest);
		}

		doc.setWeight(doc.getWeight() + (WEIGHT * found));
	}

	/**
	 * Search term in a "first" list with the next list.
	 * @param first list of position in the list of the lists
	 * @param listToTest next lists
	 * @return number of term found
	 */
	private int searchNumberTerm(final List<Integer> first, final List<List<Integer>> listToTest) {
		int found = 0;
		for (Integer position : first) {
			for (List<Integer> underList : listToTest) {
				found = found + searchTerms(position, underList);
			}
		}
		return found;

	}

	/**
	 * Compare a term position with the term position of the next lists.
	 * @param position
	 * @param positions
	 * @return the number of the term found
	 */
	private int searchTerms(Integer position, List<Integer> positions) {
		// number of term found
		int found = 0;
		// number of the turn in the loop after to have found one term
		int turnAfterFound = 0;

		for (Integer termP : positions) {
			// We check to know if the positions are close
			if ((termP - MAX_DISTANCE) < position && (termP + MAX_DISTANCE) > position) {
				found++;
				// As the list of positions is ordered, the follow term must be
				// also close. Then it's useless to check the rest of the list
			} else if (found > 0 && turnAfterFound < 4) {
				turnAfterFound++;
			} else {
				break;
			}
		}
		return found;
	}

}
