package com.github.attatrol.preprocessing.datasource.parsing.token;

public class TextBinaryDigitalParser implements TokenParser<String, Boolean> {

	@Override
	public Boolean parseValue(String rawValue) throws IllegalArgumentException {
		return rawValue.equals("1") ? Boolean.TRUE : Boolean.FALSE;
	}

}
