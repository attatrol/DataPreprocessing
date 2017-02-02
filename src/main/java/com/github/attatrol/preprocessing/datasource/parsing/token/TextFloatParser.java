package com.github.attatrol.preprocessing.datasource.parsing.token;

public class TextFloatParser implements TokenParser<String, Double>{

	@Override
	public Double parseValue(String rawValue) throws IllegalArgumentException {
		return Double.parseDouble(rawValue);
	}

}
