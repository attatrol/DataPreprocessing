package com.github.attatrol.preprocessing.datasource.parsing;

import java.io.IOException;
import java.util.Arrays;

import com.github.attatrol.preprocessing.datasource.DataSource;
import com.github.attatrol.preprocessing.datasource.TitledDataSource;
import com.github.attatrol.preprocessing.datasource.TokenDataSourceUtils;
import com.github.attatrol.preprocessing.datasource.parsing.record.RecordTokenizer;

/**
 * Stateless functional object that figures out type of a token.
 * @author atta_troll
 *
 */
@FunctionalInterface
public interface TokenTypeIdentifier {

	/**
	 * Maps incoming token into some token type.
	 * 1. Must return {@link TokenType#MISSING} if incoming token is {@code null}.
	 * 2. Must return {@link TokenType#UNKNOWN} if fails to figure out token type.
	 * @param token some token
	 * @return token type for a token parameter
	 */
	TokenType detectTokenType(Object token);

    /**
     * Defines {@link #TokenType} for tokens of this data source.
     * @param source current raw data source
     * @param tokenizer {@link RecordTokenizer} used to split raw records
     * @param identifier {@link TokenTypeIdentifier} used to identify raw records
     * @return token features array
     * @throws IOException on i/o error of data source
     */
    static <V> TokenFeatures[] defineTokenTypes(DataSource<V> source,
            RecordTokenizer<? super V, ?> tokenizer, TokenTypeIdentifier identifier) throws IOException {
        final int maximalRecordLength = TokenDataSourceUtils.getRecordCardinality(source, tokenizer);
        TokenFeatures[] features = new TokenFeatures[maximalRecordLength];
        for (int i = 0; i < features.length; i++) {
            features[i] = new TokenFeatures();
        }
        source.reset();
        while (source.hasNext()) {
            Object[] tokens = tokenizer.tokenize(source.next().getData());
            if (tokens.length < maximalRecordLength) {
                tokens = Arrays.copyOf(tokens, maximalRecordLength);
            }
            for (int i = 0; i < tokens.length; i++) {
                if (tokens[i] == null) {
                    features[i].setHasOmissions(true);
                }
                else {
                    TokenType type = identifier.detectTokenType(tokens[i]);
                    if (features[i].getType() == null) {
                        features[i].setType(type);
                    }
                    else {
                        features[i].setType(TokenType.getClosestCommonAncestor(
                                features[i].getType(), type));
                    }
                }
            }
        }
        return features;
    }

    /**
     * Defines {@link TokenType} for tokens of this data source.<br/>
     * Use this instead of {@link #defineTokenTypes(DataSource, RecordTokenizer, TokenTypeIdentifier)
     * if your data source is a {@link TitledDataSource}.
     * @param source current raw data source
     * @param tokenizer {@link RecordTokenizer} used to split raw records
     * @param identifier {@link TokenTypeIdentifier} used to identify raw records
     * @param titleTokenizer used to parse raw title into token titles
     * @return token features array
     * @throws IOException on i/o error of data source
     */
    static <V> TokenFeatures[] defineTokenTypes(TitledDataSource<V> source,
            RecordTokenizer<? super V, ?> tokenizer,
            TokenTypeIdentifier identifier,
            RecordTokenizer<String, String> titleTokenizer) throws IOException {
        TokenFeatures[] features = defineTokenTypes(source, tokenizer, identifier);
        final String rawTitles = source.getTitles();
        if (rawTitles != null) {
            String[] titles = titleTokenizer.tokenize(rawTitles);
            for (int i = 0; i< features.length && i<titles.length; i++) {
                features[i].setTitle(titles[i]);
            }
        }
        return features;
    }
}
