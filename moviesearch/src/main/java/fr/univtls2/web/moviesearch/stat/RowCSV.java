package fr.univtls2.web.moviesearch.stat;

public class RowCSV {

	private String[] columns;

	private static final String SEPARATOR = ";";

	/**
	 * It's a row to build a CSV.
	 * @param columns one data by columns
	 */
	public RowCSV(String... columns) {
		super();
		this.columns = columns;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (String data : columns) {
			builder.append(data);
			builder.append(SEPARATOR);
		}
		return builder.replace(builder.length() - 1, builder.length(), "").toString();
	}
}
