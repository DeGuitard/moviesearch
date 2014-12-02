package fr.univtls2.web.moviesearch.stat;

public class RowCSV {

	private String[] columns;

	private static final String SEPARATOR = ";";
	private static final String DECIMAL_SEPARATOR_EN = ".";
	private static final String DECIMAL_SEPARATOR_FR = ".";

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
			builder.append(data.replace(DECIMAL_SEPARATOR_EN, DECIMAL_SEPARATOR_FR));
			builder.append(SEPARATOR);
		}
		return builder.replace(builder.length() - 1, builder.length(), "").toString();
	}
}
