package fr.univtls2.web.moviesearch.services.indexation.extraction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.univtls2.web.moviesearch.model.SourceDoc;
import fr.univtls2.web.moviesearch.model.Term;
import fr.univtls2.web.moviesearch.model.builders.SourceDocBuilder;
import fr.univtls2.web.moviesearch.model.builders.TermBuilder;

/**
 * <p>Simple implementation of {@link Extractor}.</p>
 * <p>Works with a stop list.</p>
 * TODO: store text position.
 * TODO: analyze data from meta tags.
 *
 * @author Vianney Dupoy de Guitard
 */
public class SimpleExtractor implements Extractor {

	/** Applicative logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleExtractor.class);

	/** List of words to ignore. */
	private static final Set<String> STOP_LIST = new HashSet<>(Arrays.asList("a","à","afin","ai","aie","aient","aient","ainsi","ais","ait","alors","as","assez","au","auquel","auquelle","aussi","aux","auxquelles","auxquels","avaient","avais","avait","avant","avec","avoir","beaucoup","ca","ça","car","ce","cela","celle","celles","celui","certain","certaine","certaines","certains","ces","cet","cette","ceux","chacun","chacune","chaque","chez","ci","comme","comment","concern","concernant","connait","connaît","conseil","contre","d","dans","de","des","desquelles","desquels","differe","different","différent","differente","différente","differentes","différentes","differents","différents","dois","doit","doivent","donc","dont","du","dû","duquel","dus","e","elle","elles","en","encore","ensuite","entre","es","est","et","etai","etaient","étaient","etais","étais","etait","était","etant","étant","etc","ete","été","etiez","étiez","etion","etions","étions","etre","être","eu","eux","evidenc","evidence","évidence","expliqu","explique","fai","faire","fais","fait","faite","faites","faits","fera","feras","fini","finie","finies","finis","finit","font","grace","grâce","ici","il","ils","intere","interessant","intéressant","interesse","intéressé","j","jamais","je","l","la","laquell","laquelle","le","lequel","les","lesquelles","lesquels","leur","leurs","lors","lorsque","lui","m","ma","mainten","maintenant","mais","mal","me","meme","même","memes","mêmes","mes","mettre","moi","moins","mon","n","ne","ni","no","non","nos","notre","nôtre","notres","nôtres","nou","nous","obtenu","obtenue","obtenues","obtenus","on","ont","or","ou","où","par","parfois","parle","pars","part","pas","permet","peu","peut","peuvent","peux","plus","pour","pourquo","pourquoi","pouvez","pouvons","prendre","pres","près","princip","principal","principaux","qu","quand","que","quel","quelle","quelles","quelques","quels","qui","quoi","sa","savoir","se","seront","ses","seul","seuls","si","soient","soit","son","sont","sous","souvent","sui","suis","sur","t","ta","te","tel","telle","telleme","tellement","telles","tels","tes","ton","toujour","toujours","tous","tout","toute","toutes","traite","tres","très","trop","tu","unv","une","unes","uns","utilise","utilisé","utilisee","utilisée","utilisées","utilisees","uilisés","utilises","va","venir","vers","veut","veux","vont","voulez","voulu","vous"));

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Term> extract(Document doc) {
		if (doc == null) {
			throw new IllegalArgumentException("No document supplied.");
		}
		List<Term> terms = new ArrayList<Term>();

		// Looks for all html nodes.
		for (Element el : doc.getAllElements()) {
			String text = cleanText(el.ownText());

			// Looks for each word of this node.
			for (String word : text.split(" ")) {
				if (isWordToIgnore(word)) {
					continue;
				}

				// Creates or updates an exiting node.
				Term term = new TermBuilder().word(word).create();
				int termIndex = terms.indexOf(term);
				if (termIndex == -1) {
					LOGGER.debug("New term extracted: '{}'", term.getWord());
					addNewTerm(doc, terms, el.tagName(), term);
				} else {
					updateTerm(terms.get(termIndex), el.tagName());
				}
			}
		}
		return terms;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Term> extract(String query) {
		// Checks the supplied query.
		if (query == null) {
			throw new IllegalArgumentException("No document supplied.");
		}

		List<Term> terms = new ArrayList<>();
	
		// Splits the query into words.
		query = cleanText(query);
		for (String word : query.split(" ")) {
			word = cleanText(word);

			// Makes sure this is not a word to ignore. 
			if (isWordToIgnore(word)) {
				continue;
			}

			Term term = new TermBuilder().word(word).create();
			terms.add(term);
		}
		return terms;
	}

	/**
	 * Updates an existing term.
	 * @param term : the term to update.
	 * @param tagName : the tag that contained the term.
	 */
	private void updateTerm(Term term, String tagName) {
		SourceDoc srcDoc = term.getDocuments().get(0);
		srcDoc.incrementOccurrences();
		srcDoc.getTags().add(tagName);
	}

	/**
	 * Adds a new term.
	 * @param doc : the document containing the term.
	 * @param terms : the list of found terms.
	 * @param tagName : the tag that contained the term.
	 * @param term
	 */
	private void addNewTerm(Document doc, List<Term> terms, String tagName, Term term) {
		SourceDoc srcDoc = generateSourceDoc(doc, tagName);
		term.getDocuments().add(srcDoc);
		terms.add(term);
	}

	/**
	 * @param text : the text to analyze.
	 * @return true if the world should be ignored.
	 */
	private boolean isWordToIgnore(String text) {
		boolean toIgnore = false;
		if (StringUtils.isBlank(text) || STOP_LIST.contains(text)) {
			toIgnore = true;
		}
		return toIgnore;
	}

	/**
	 * @param doc : the associated document.
	 * @param tag : the first tag associated with this document.
	 * @return the corresponding pre-filled sourceDoc.
	 */
	private SourceDoc generateSourceDoc(Document doc, String tag) {
		int size = doc.text().length();
		// Only consider the filename.
		String[] splittedUri = doc.baseUri().split("/");
		String url = splittedUri[splittedUri.length - 1];
		SourceDoc sourceDoc = new SourceDocBuilder().size(size).url(url).tags(tag).occurrences(1).create();
		return sourceDoc;
	}

	/**
	 * @param text : the text to clean.
	 * @return the cleaned text.
	 */
	private String cleanText(String text) {
		text = text.replaceAll("[\\']", " ");
		text = text.replaceAll("[^\\p{L}\\d.,\\-\\s]", "");
		while (text.endsWith(".") || text.endsWith(",")) {
			text = text.substring(0, text.length() - 1);
		}
		text = text.toLowerCase();
		text = text.trim();
		return text;
	}
}
