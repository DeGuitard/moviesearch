package fr.univtls2.web.moviesearch.stat;

public class RowCSV {

	private String name;
	private String[] columns;

	private static final String SEPARATOR = ",";

	/**
	 * It's a row to build a CSV.
	 * @param columns one data by columns
	 */
	public RowCSV(String name, String... columns) {
		super();
		this.columns = columns;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(name);
		for (String data : columns) {
			builder.append(data);
			builder.append(SEPARATOR);
		}
		builder.append("\n");
		return builder.toString();
	}
}
