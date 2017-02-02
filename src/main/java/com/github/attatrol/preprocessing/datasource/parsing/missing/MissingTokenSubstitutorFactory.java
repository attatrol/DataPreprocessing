package com.github.attatrol.preprocessing.datasource.parsing.missing;

import java.io.IOException;

import com.github.attatrol.preprocessing.datasource.AbstractTokenDataSource;

/**
 * Factory method must be implemented for any 
 * {@link MissingTokenSubstitutor} for being used in
 * {@link SyntaxRegister}.
 * @author atta_troll
 *
 * @param <V>
 */
@FunctionalInterface
public interface MissingTokenSubstitutorFactory<V> {

	/**
	 * Produces some {@link MissingCodeSubstitutor}.
	 * @param dataSource token data source in use
	 * @param index index of the token to substitute
	 * @return substitutor instance
	 * @throws IOException on i/o error
	 * @throws IllegalArgumentException on failure to process token properly.
	 */
	MissingTokenSubstitutor<V> produceSubstitutor(AbstractTokenDataSource<?> dataSource, int index)
			throws IOException, IllegalArgumentException;

}
