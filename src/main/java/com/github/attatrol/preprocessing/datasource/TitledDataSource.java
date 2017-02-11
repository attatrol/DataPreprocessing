package com.github.attatrol.preprocessing.datasource;

import java.io.IOException;

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
	 * @throws IOException on internal i/o error
	 */
	String getTitles() throws IOException;

}
