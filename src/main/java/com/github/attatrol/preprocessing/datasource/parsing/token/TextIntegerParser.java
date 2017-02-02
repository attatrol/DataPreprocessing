package com.github.attatrol.preprocessing.datasource.parsing.token;

public class TextIntegerParser implements TokenParser<String, Integer>{

	@Override
	public Integer parseValue(String rawValue) throws IllegalArgumentException {
		return Integer.parseInt(rawValue);
	}

}
