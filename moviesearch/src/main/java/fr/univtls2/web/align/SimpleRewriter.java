package fr.univtls2.web.align;

import java.io.File;
import java.net.URI;
import java.util.Iterator;

import org.semanticweb.owl.align.AlignmentException;
import org.semanticweb.owl.align.Cell;

import fr.inrialpes.exmo.align.impl.BasicAlignment;
import fr.inrialpes.exmo.align.impl.URIAlignment;
import fr.inrialpes.exmo.align.parser.AlignmentParser;

public class SimpleRewriter {

	private BasicAlignment alignment = new URIAlignment();

	public SimpleRewriter() {
		try {
			URI onto = new File("src/main/resources/owl/fusion-clean.rdf").toURI();
			AlignmentParser parser = new AlignmentParser();
			alignment = (BasicAlignment) parser.parse(onto);
		} catch (AlignmentException e) {
			e.printStackTrace();
		}
	}

	public String rewrite(String query) throws AlignmentException {
		String newQuery = "PREFIX dbpedia2: <http://dbpedia.org/property/> " + query;
		newQuery = "PREFIX inst: <http://dbpedia.org/resource/> " + newQuery;
		String[] words = query.split(" ");
		for (String word : words) {
			if (word.contains(":")) {
				String cleanWord = word.substring(word.lastIndexOf(":") + 1, word.length());
				Iterator<Cell> cells = alignment.iterator();
				String alignedWord = null;
				while (cells.hasNext()) {
					Cell cell = cells.next();
					String obj = cell.getObject1().toString();
					if (obj.endsWith(cleanWord)) {
						alignedWord = cell.getObject2().toString();
						alignedWord = alignedWord.substring(alignedWord.lastIndexOf("/") + 1, alignedWord.length());
					}
				}
				String prefix = word.substring(0, word.lastIndexOf(":") + 1);
				if (alignedWord == null) {
					alignedWord = word.replace(prefix, "inst:");
				} else {
					alignedWord = "dbpedia2:" + word;
				}
				newQuery = newQuery.replace(word, alignedWord);
			}
		}
		return newQuery;
	}

	public static void main(String[] args) throws AlignmentException {
		SimpleRewriter rew = new SimpleRewriter();
		String toRewriteA = "SELECT * WHERE { ?a ?b :Omar_Sy }";
		String toRewriteB = "SELECT * WHERE { ?a :aPourProducteur ?c }";

		String queryA = rew.rewrite(toRewriteA);
		String queryB = rew.rewrite(toRewriteB);

		System.out.println("QUERY A : " + toRewriteA);
		System.out.println("=> " + queryA);
		System.out.println("QUERY B : " + toRewriteB);
		System.out.println("=> " + queryB);
	}
}
