package com.github.attatrol.preprocessing.ui.uimodel;

import java.util.Arrays;

import com.github.attatrol.preprocessing.datasource.AbstractTokenDataSource;
import com.github.attatrol.preprocessing.datasource.DataSource;
import com.github.attatrol.preprocessing.datasource.Record;
import com.github.attatrol.preprocessing.datasource.parsing.record.RecordTokenizer;

/**
 * This is a partially formed token data source used during interactive
 * {@link #DefaultTokenDataSource} creation by the user when record parser is set but no token
 * parsers are set. {@link #TokenParser}
 * 
 * @author atta_troll
 *
 */
public class UnprocessedTokenDataSource extends AbstractTokenDataSource<Object> {

    @SuppressWarnings("rawtypes")
    private RecordTokenizer tokenizer;

    /**
     * Default ctor.
     * @param internalDataSource internal data source
     * @param recordLength number of tokens in every record
     * @param tokenizer tokenizer used to split raw record into tokens
     */
    public UnprocessedTokenDataSource(DataSource<?> internalDataSource, int recordLength,
            RecordTokenizer<?, ?> tokenizer) {
        super(internalDataSource, recordLength);
        this.tokenizer = tokenizer;
    }

    @Override
    protected Record<Object[]> parseRecord(Record<?> record) throws IllegalArgumentException {
        @SuppressWarnings("unchecked")
        Object[] result = tokenizer.tokenize(record.getData());
        if (result.length < getRecordLength()) {
            result = Arrays.copyOf(result, getRecordLength());
        }
        return new Record<>(result, record.getIndex());
    }

}
