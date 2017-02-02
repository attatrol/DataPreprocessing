package com.github.attatrol.preprocessing.datasource.parsing.token;

/**
 * Stub parser actually don't parse anything.
 * @author atta_troll
 *
 */
public class StubParser<V> implements TokenParser<V, V> {

	@Override
	public V parseValue(V rawValue) throws IllegalArgumentException {
		return rawValue;
	}

}
