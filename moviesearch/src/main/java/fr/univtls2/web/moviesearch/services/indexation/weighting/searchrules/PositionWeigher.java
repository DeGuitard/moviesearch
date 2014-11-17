package fr.univtls2.web.moviesearch.services.indexation.weighting.searchrules;

import java.util.List;

import fr.univtls2.web.moviesearch.model.SourceDoc;
import fr.univtls2.web.moviesearch.services.indexation.weighting.SearchWeigher;

public class PositionWeigher implements SearchWeigher {

	private static int MAX_DISTANCE = 20;
	private static int WEIGHT = 1;

	@Override
	public void weight(SourceDoc doc) {
		Integer min = null;
		int index = 0;
		for (List<Integer> posList : doc.getPositionsList()) {
			for (Integer pos : posList) {
				min = findMin(doc, pos, index, min);
			}
			index++;
		}

		if (min < MAX_DISTANCE) {
			doc.setWeight(doc.getWeight() + WEIGHT);
		}
	}

	private Integer findMin(SourceDoc doc, Integer pos, int index, Integer min) {
		int indexBis = 0;
		for (List<Integer> posListBis : doc.getPositionsList()) {
			if (indexBis == index) {
				continue;
			}
			for (Integer posBis : posListBis) {
				int distance = Math.abs(pos - posBis);
				if (min == null || distance < min ) {
					min = distance;
				}
			}
			indexBis++;
		}
		return min;
	}

}
