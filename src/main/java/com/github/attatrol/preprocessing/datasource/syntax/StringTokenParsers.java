package com.github.attatrol.preprocessing.datasource.syntax;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.github.attatrol.preprocessing.datasource.parsing.TokenType;
import com.github.attatrol.preprocessing.datasource.parsing.token.StubParser;
import com.github.attatrol.preprocessing.datasource.parsing.token.TextBinaryDigitalParser;
import com.github.attatrol.preprocessing.datasource.parsing.token.TextBinaryParser;
import com.github.attatrol.preprocessing.datasource.parsing.token.TextCategoricalParser;
import com.github.attatrol.preprocessing.datasource.parsing.token.TextFloatParser;
import com.github.attatrol.preprocessing.datasource.parsing.token.TextIntegerParser;
import com.github.attatrol.preprocessing.datasource.parsing.token.TokenParser;

final class StringTokenParsers {

	private StringTokenParsers() { };

	public static final Map<TokenType, TokenParser<? super String, ?>> STRING_TOKEN_PARSERS;
	static {
		HashMap<TokenType, TokenParser<? super String, ?>> stringTokenParsers = new HashMap<>();
		stringTokenParsers.put(TokenType.CATEGORICAL_STRING, new TextCategoricalParser());
		stringTokenParsers.put(TokenType.INTEGER, new TextIntegerParser());
		stringTokenParsers.put(TokenType.FLOAT, new TextFloatParser());
		stringTokenParsers.put(TokenType.BINARY_DIGITAL, new TextBinaryDigitalParser());
		stringTokenParsers.put(TokenType.BINARY, new TextBinaryParser());
		stringTokenParsers.put(TokenType.UNKNOWN, new StubParser<Object>());
		STRING_TOKEN_PARSERS = Collections.unmodifiableMap(stringTokenParsers);
	}

	/**
	 * Pattern matching integers.
	 */
	private static final Pattern INTEGER_PATTERN = Pattern.compile("[+-]?\\d+");

	/**
	 * Pattern matching floats.
	 */
	private static final Pattern REAL_PATTERN = Pattern.compile("[+-]?((\\d*\\.?\\d+)|(\\d+\\.?\\d*))");

	/**
	 * Pattern matching booleans.
	 */
	private static final Pattern BINARY_DIGITAL_PATTERN = Pattern.compile("0|1");
	/**
	 * Pattern matching booleans.
	 */
	private static final Pattern BINARY_PATTERN = Pattern.compile("t|f|true|false|T|F|True|False");

	/**
	 * Detects token type. 
	 * @param token some token of a record from data set.
	 * @return token type.
	 */
	public static TokenType detectStringTokenType(Object token) {
		if (token == null) {
			return TokenType.MISSING;
		}
		if (token instanceof String) {
			String string = (String) token;
			if (BINARY_DIGITAL_PATTERN.matcher(string).matches()) {
				return TokenType.BINARY_DIGITAL;
			}
			else if (INTEGER_PATTERN.matcher(string).matches()) {
				return TokenType.INTEGER;
			}
			else if (REAL_PATTERN.matcher(string).matches()) {
				return TokenType.FLOAT;
			}
			else if (BINARY_PATTERN.matcher(string).matches()) {
				return TokenType.BINARY;
			}
			else {
				return TokenType.CATEGORICAL_STRING;
			}
		}
		return TokenType.UNKNOWN;
	}
}
