package com.github.attatrol.preprocessing.datasource.parsing.token;

import java.util.IllegalFormatException;

/**
 * Parses raw value into a format understandable by internal algorithms.<br/>
 * NOTE: must be a stateless object!.
 * @author atta_troll
 *
 * @param <V> raw value type
 * @param <T> internal value type
 */
public interface TokenParser<V, T> {

	/**
	 * Parses raw value into a format understandable by internal algorithms.
	 * @param rawValue raw token
	 * @return parsed token
	 * @throws IllegalFormatException on failure to parse
	 */
	T parseValue(V rawValue) throws IllegalArgumentException;

}
