package com.github.attatrol.preprocessing.datasource;

/**
 * This data source contains some additional text info about itself,
 * generally it is titles of tokens, but may be anything.
 * @author atta_troll
 *
 */
public interface TitledDataSource<V> extends DataSource<V> {

	/**
	 * It is okay to return {@code null}.
	 * @return title string
	 */
	String getTitles();

}
