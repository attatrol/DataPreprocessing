package com.github.attatrol.preprocessing.datasource.parsing.record;

/**
 * Splits string into tokens separated by any space characters
 * @author atta_troll
 *
 */
public class TabulationSeparatedStringSplitter implements RecordTokenizer<String, String> {

	@Override
	public String[] tokenize(String record) throws IllegalArgumentException {
		String[] strings  = record.split("\\s+");
		for(int i = 0; i < strings.length; i++) {
			strings[i] = strings[i].trim();
			if ("".equals(strings[i])) {
				strings[i] = null;
			}
		}
		return strings;
	}

}
