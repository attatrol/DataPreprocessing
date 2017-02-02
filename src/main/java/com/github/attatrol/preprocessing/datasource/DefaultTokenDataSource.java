package com.github.attatrol.preprocessing.datasource;

import java.util.Arrays;
import java.util.IllegalFormatException;

import com.github.attatrol.preprocessing.datasource.parsing.missing.MissingTokenSubstitutor;
import com.github.attatrol.preprocessing.datasource.parsing.record.RecordTokenizer;
import com.github.attatrol.preprocessing.datasource.parsing.token.TokenParser;

/**
 * This is a default token data source realization used in the library.
 * It uses {@link RecordTokenizer} for splitting incoming raw record into tokens,
 * then tokens are parsed by {@link TokenParser}s, and {@link MissingTokenSubstitutor}s
 * are used in case of omitted values of some tokens.
 * @author atta_troll
 *
 */
public class DefaultTokenDataSource<V> extends AbstractTokenDataSource<V>{

	private final RecordTokenizer<? super V, ?> tokenizer;

	private final TokenParser<Object, ?>[] parsers;

	private final MissingTokenSubstitutor<?>[] substitutors;

	public DefaultTokenDataSource(DataSource<V> internalDataSource, 
			RecordTokenizer<? super V, ?> tokenizer, int recordLength,
			TokenParser<Object, ?>[] parsers,
			MissingTokenSubstitutor<?>[] substitutors) {
		super(internalDataSource, recordLength);
		this.tokenizer = tokenizer;
		this.parsers = parsers;
		this.substitutors = substitutors;
	}

    @Override
	protected Record<Object[]> parseRecord(Record<? extends V> record) throws IllegalFormatException {
		final long index = record.getIndex();
		Object[] rawTokens = tokenizer.tokenize(record.getData());
		if (rawTokens.length < getRecordLength()) {
			rawTokens = Arrays.copyOf(rawTokens, getRecordLength());
		}
		final Object[] result = new Object[getRecordLength()];
		boolean hasMissingValues = false;
		for (int i = 0; i < getRecordLength(); i++) {
			if (rawTokens[i] != null) {
				result[i] = parsers[i].parseValue(rawTokens[i]);
			}
			else {
				hasMissingValues = true;
			}
		}
		final Record<Object[]> resultRecord = new Record<>(result, index);
		if (hasMissingValues) {
			for (int i = 0; i < getRecordLength(); i++) {
				if (result[i] == null && substitutors[i] != null) {
					result[i] = substitutors[i].substitute(resultRecord);
				}
			}
		}
		return resultRecord;
	}

}
