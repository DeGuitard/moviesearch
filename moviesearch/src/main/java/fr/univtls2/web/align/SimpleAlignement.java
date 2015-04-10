package fr.univtls2.web.align;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.semanticweb.owl.align.Alignment;
import org.semanticweb.owl.align.AlignmentException;
import org.semanticweb.owl.align.AlignmentProcess;
import org.semanticweb.owl.align.AlignmentVisitor;
import org.semanticweb.owl.align.Cell;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import fr.inrialpes.exmo.align.impl.URIAlignment;
import fr.inrialpes.exmo.align.impl.method.ClassStructAlignment;
import fr.inrialpes.exmo.align.impl.renderer.RDFRendererVisitor;
import fr.inrialpes.exmo.align.parser.AlignmentParser;
import fr.inrialpes.exmo.ontowrap.OntowrapException;

public class SimpleAlignement {

	public static void main(String[] args) {
		try {
			Set<Alignment> alignments = new HashSet<>();
			alignments.add(logMapAlign());
			alignments.add(classStructAlign());
			alignments.add(customAlign());

			URIAlignment fusion = new URIAlignment();
			URI onto1 = new File("./src/main/resources/owl/FilmographieV1Inf.owl").toURI();
			URI onto2 = new File("./src/main/resources/owl/dbpedia_2014.owl").toURI();
			fusion.init(onto1, onto2);

			int count = 0;
			Map<URI, List<URI>> pairs = new HashMap<>();
			for (Alignment align : alignments) {
				Iterator<Cell> cellIterator = align.iterator();
				while (cellIterator.hasNext()) {
					Cell c = cellIterator.next();
					URI uri1 = c.getObject1AsURI();
					URI uri2 = c.getObject2AsURI();
					if (pairs.get(uri1) == null) {
						pairs.put(uri1, new ArrayList<URI>());
					}
					if (!pairs.get(uri1).contains(uri2)) {
						System.out.println(c.getObject1AsURI() + " => " + c.getObject2AsURI());
						fusion.addAlignCell(c.getObject1AsURI(), c.getObject2AsURI());
						count++;
					}
				}
			}
			
			System.out.println("Alignments count: " + count);

			render(fusion, "fusion.rdf");
		} catch (Exception e) {
			e.printStackTrace();
			// We don't care :).
		}
	}
	
	public static Alignment logMapAlign() throws AlignmentException {
		URI onto = new File("src/main/resources/owl/logmap2_mappings.rdf").toURI();
		AlignmentParser parser = new AlignmentParser();
		return parser.parse(onto);
	}

	public static AlignmentProcess classStructAlign() throws URISyntaxException , AlignmentException, FileNotFoundException, UnsupportedEncodingException {
		URI onto1 = new File("./src/main/resources/owl/FilmographieV1Inf.owl").toURI();
		URI onto2 = new File("./src/main/resources/owl/dbpedia_2014.owl").toURI();
		AlignmentProcess alignment = new ClassStructAlignment();
		alignment.init (onto1, onto2);
		alignment.align(null, new Properties());
		return alignment;
	}

	public static SimpleMatcher customAlign() throws URISyntaxException , AlignmentException, FileNotFoundException, UnsupportedEncodingException, OWLOntologyCreationException, OntowrapException {
		URI onto1 = new File("./src/main/resources/owl/FilmographieV1Inf.owl").toURI();
		URI onto2 = new File("./src/main/resources/owl/dbpedia_2014.owl").toURI();
		SimpleMatcher alignment = new SimpleMatcher();
		alignment.init (onto1, onto2);
		alignment.align(null, new Properties());
		return alignment;
	}

	public static void render(Alignment alignment, String filename) throws FileNotFoundException , UnsupportedEncodingException, AlignmentException {
		PrintWriter writer;
		FileOutputStream f = new FileOutputStream(new File("/Users/niyaven/ownCloud/Documents/Cours/ICE M2/Web Sémantique/Alignement/" + filename));
		writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(f,"UTF-8")), true);
		AlignmentVisitor renderer = new RDFRendererVisitor(writer);
		alignment.render(renderer);
		writer.flush();
		writer.close();
	}
}
