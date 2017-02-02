package com.github.attatrol.preprocessing.datasource.parsing.missing;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import com.github.attatrol.preprocessing.datasource.Record;
import com.github.attatrol.preprocessing.datasource.parsing.TokenType;

/**
 * Abstract class, base for all functional classes that substitute
 * missing values.
 * All instances must contain 
 * @author atta_troll
 *
 * @param <V>
 */
public abstract class MissingTokenSubstitutor<V> {

	/**
	 * Map containing all known substitutors with regard to their token type.
	 */
	public static final Map<TokenType, MissingTokenSubstitutorFactory<?>[]> SUBSTITUTOR_REGISTER;
	static {
		Map<TokenType, MissingTokenSubstitutorFactory<?>[]> register = new EnumMap<>(TokenType.class);
		register.put(TokenType.FLOAT, new MissingTokenSubstitutorFactory<?>[]{
			new ExpectedValueFloatSubstitutor.Factory(),
			new MostCommonValueSubstitutor.Factory<Float>(),
		});
		register.put(TokenType.INTEGER, new MissingTokenSubstitutorFactory<?>[]{
			new ExpectedValueIntegerSubstitutor.Factory(),
			new MostCommonValueSubstitutor.Factory<Integer>(),
		});
		register.put(TokenType.BINARY, new MissingTokenSubstitutorFactory<?>[]{
		    new MostCommonValueSubstitutor.Factory<Boolean>(),
		});
		register.put(TokenType.BINARY_DIGITAL, new MissingTokenSubstitutorFactory<?>[]{
		    new MostCommonValueSubstitutor.Factory<Boolean>(),
		});
		register.put(TokenType.CATEGORICAL_STRING, new MissingTokenSubstitutorFactory<?>[]{
		    new MostCommonValueSubstitutor.Factory<String>(),
		});
		SUBSTITUTOR_REGISTER = Collections.unmodifiableMap(register);
	}

	/**
	 * Index of the missing token in a record.
	 */
	protected int index;

	/**
	 * Default ctor.
	 * @param index index of the missing token in a record.
	 */
	protected MissingTokenSubstitutor(int index) {
		this.index = index;
	}

	/**
	 * Finds a proper substitution for an omitted token.
	 * @param tokens a record consisted of tokens
	 * @return substitution value
	 */
	public abstract V substitute(Record<? extends Object[]> tokens);
}
