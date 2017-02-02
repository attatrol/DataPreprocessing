package com.github.attatrol.preprocessing.datasource.parsing.record;

/**
 * General interface for some parser that splits incoming record
 * into parts which are to be used in some ways.<br/>
 * Must return {@code null} for any omitted piece.<br/>
 * Must be stateless.
 * @author atta_troll
 *
 * @param <V> type of a record 
 * @param <T> type of produced pieces
 */
public interface RecordTokenizer<V, T> {

	/**
	 * Parses a record from data source into separate pieces of data
	 * with respect to their order.
	 * @param record a record
	 * @return produced pieces of data
	 */
	T[] tokenize(V record) throws IllegalArgumentException;

}
