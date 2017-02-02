package com.github.attatrol.preprocessing.datasource.parsing.token;

import java.util.regex.Pattern;

public class TextBinaryParser implements TokenParser<String, Boolean> {

	public static final Pattern TRUE_PARSER = Pattern.compile("t|T|True|true");

	@Override
	public Boolean parseValue(String rawValue) throws IllegalArgumentException {
		return TRUE_PARSER.matcher(rawValue).matches() ? Boolean.TRUE : Boolean.FALSE;
	}

}
