
package com.github.attatrol.preprocessing.datasource.syntax;

import java.util.Map;

import com.github.attatrol.preprocessing.datasource.parsing.TokenType;
import com.github.attatrol.preprocessing.datasource.parsing.record.RecordTokenizer;
import com.github.attatrol.preprocessing.datasource.parsing.token.TokenParser;

/**
 * The same as {@link TokenDataSourceSyntax}, but contains title tokenizer for
 * parsing of titles of some data source.
 * @author atta_troll
 *
 * @param <V> raw record type
 * @param <T> raw token type, usually Object
 */
public abstract class TitledTokenDataSourceSyntax<V, T> extends TokenDataSourceSyntax<V, T> {

    private RecordTokenizer<? super V, String> titleTokenizer;

    public TitledTokenDataSourceSyntax(RecordTokenizer<? super V, ? extends T> tokenizer,
            RecordTokenizer<? super V, String> titleTokenizer,
            Map<TokenType, TokenParser<? super T, ?>> tokenParsers) {
        super(tokenizer, tokenParsers);
        this.titleTokenizer = titleTokenizer;
    }

    public RecordTokenizer<? super V, String> getTitleTokenizer() {
        return titleTokenizer;
    }
}
