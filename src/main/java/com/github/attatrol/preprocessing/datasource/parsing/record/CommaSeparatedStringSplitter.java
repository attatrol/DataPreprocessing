package com.github.attatrol.preprocessing.datasource.parsing.record;

/**
 * Recovers comma separated substrings from some strings.<br/>
 * Returns {@code null} if token string is empty
 * @author atta_troll
 *
 */
public class CommaSeparatedStringSplitter implements RecordTokenizer<String, String> {

	@Override
	public String[] tokenize(String record) throws IllegalArgumentException {
		String[] strings  = record.split(",");
		for(int i = 0; i < strings.length; i++) {
			strings[i] = strings[i].trim();
			if ("".equals(strings[i])) {
				strings[i] = null;
			}
		}
		return strings;
	}

}
