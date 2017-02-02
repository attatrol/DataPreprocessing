package com.github.attatrol.preprocessing.datasource.parsing.token;

public class TextCategoricalParser implements TokenParser<String, String> {

	@Override
	public String parseValue(String rawValue) throws IllegalArgumentException {
		return rawValue;
	}

}
