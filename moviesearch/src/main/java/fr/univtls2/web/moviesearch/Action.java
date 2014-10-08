package fr.univtls2.web.moviesearch;

/**
 * <p>List of available actions for the main application.</p>
 *
 * @author Vianney Dupoy de Guitard
 */
public enum Action {

	/** Evaluate the indexation using a set of QRels. */
	EVALUATE,

	/** Starts the indexation of a folder. */
	INDEX,

	/** Searches a term. */
	SEARCH;
}
