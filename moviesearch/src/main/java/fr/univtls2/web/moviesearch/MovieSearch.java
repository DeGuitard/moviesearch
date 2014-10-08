package fr.univtls2.web.moviesearch;

import java.io.File;
import java.util.NoSuchElementException;

import com.google.inject.Guice;
import com.google.inject.Injector;

import fr.univtls2.web.moviesearch.services.EvaluationGenerator;
import fr.univtls2.web.moviesearch.services.MovieSearchModule;
import fr.univtls2.web.moviesearch.services.indexation.ImportService;

/**
 * <p>Main entry point of the application. It takes two arguments, action and directory.</p>
 * <p>There are two distinct actions:</p>
 * <ul><li>evaluate: evaluates the indexation with a QRel list.</li>
 * <li>index: index a folder content.</li></ul>
 *
 * @author Vianney Dupoy de Guitard
 */
public class MovieSearch {

	/** Usage message displayed when wrong arguments are supplied. */
	private static final String USAGE = "Usage: moviesearch.jar <evaluate|index> <directory>";

	/**
	 * <p>Main entry point of the application. If wrong arguments are supplied, the usage message will be displayed.</p>
	 * @param args : the supplied arguments.
	 */
	public static void main(String[] args) {
		if (args.length != 2) {
			throw new IllegalArgumentException(USAGE);
		}
		Action action = getAction(args[0]);
		File directory = new File(args[1]);

		switch (action) {
		case EVALUATE:
			evaluate(directory);
			break;
		case INDEX:
			index(directory);
			break;
		}
	}

	/**
	 * <p>Starts the indexation of a directory.</p>
	 * @param directory : the directory to index.
	 */
	private static void index(File directory) {
		Injector injector = Guice.createInjector(new MovieSearchModule());
		ImportService importService = injector.getInstance(ImportService.class);
		importService.start(directory);
	}

	/**
	 * <p>Starts the evaluation of the indexation.</p>
	 * @param directory : the directory containing the QRel files.
	 */
	private static void evaluate(File directory) {
		Injector injector = Guice.createInjector(new MovieSearchModule());
		EvaluationGenerator evaluator = injector.getInstance(EvaluationGenerator.class);
		evaluator.printStats(directory);
	}

	/**
	 * @param strAction : the string action.
	 * @return the enum action.
	 */
	public static Action getAction(String strAction) {
		strAction = strAction.toUpperCase();
		try {
			return Action.valueOf(strAction);
		} catch (NoSuchElementException e) {
			throw new IllegalArgumentException(USAGE);
		}
	}
}
