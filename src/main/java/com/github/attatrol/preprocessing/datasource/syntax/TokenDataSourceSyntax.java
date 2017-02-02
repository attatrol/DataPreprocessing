package com.github.attatrol.preprocessing.datasource.syntax;

import java.io.IOException;
import java.util.Map;

import com.github.attatrol.preprocessing.datasource.DataSource;
import com.github.attatrol.preprocessing.datasource.parsing.TokenType;
import com.github.attatrol.preprocessing.datasource.parsing.TokenTypeIdentifier;
import com.github.attatrol.preprocessing.datasource.parsing.record.RecordTokenizer;
import com.github.attatrol.preprocessing.datasource.parsing.token.TokenParser;

/**
 * Defines all possible syntaxes of some {@link #DefaultTokenDataSource}:<br/>
 * 1. Creates basic data source from some eternal object source with {@link #getBasicDataSource(Object)};<br/>
 * 2. Contains unique stateless {@link #tokenizer} which can be used for splitting raw records into tokens;<br/>
 * 3. Is an implementatin of {link TokenTypeIdentifier} so it does raw token type identification;<br/>
 * 4. Contains {@link #detectTokenType(Object)} for detecting token type;<br/>
 * 5. Provides a unique stateless token parser for each token type with {@link #tokenParsers} map.<br/>
 * <p/>
 * One should create his own instance of this class in every time they need to add custom data source.
 * If it should appear in ui it should be registered in {@link TokenDataSourceSyntax} and {@ExternalSourceTypeRegister}.
 * @author atta_troll
 *
 * @param <V> raw record type
 * @param <T> raw token type, usually Object or String
 */
public abstract class TokenDataSourceSyntax<V, T> implements TokenTypeIdentifier {

    /**
     * Record tokenizer used for splitting raw record into raw tokens.
     */
    private final RecordTokenizer<? super V, ? extends T> tokenizer;

    /**
     * Maps token type into recommended token parser.
     */
    private final Map<TokenType, TokenParser<? super T, ?>> tokenParsers;

    /**
     * Default ctor
     * @param tokenizer raw record tokenizer instance
     * @param tokenParsers token type into token parsers map
     */
    public TokenDataSourceSyntax(RecordTokenizer<? super V, ? extends T> tokenizer,
            Map<TokenType, TokenParser<? super T, ?>> tokenParsers) {
        this.tokenizer = tokenizer;
        this.tokenParsers = tokenParsers;
    }

    /**
     * @return raw reord tokenizer instance
     */
    public RecordTokenizer<? super V, ? extends T> getTokenizer() {
        return tokenizer;
    }

    /**
     * Maps token type into proper token parser
     * @param tokenType token type
     * @return token parser
     */
    public TokenParser<? super T, ?> getTokenParser(TokenType tokenType) {
        return tokenParsers.get(tokenType);
    }

    /**
     * Creates basic data source from some external source object
     * @param externalSource file, JDBC connection, socket, etc.
     * @return basic data source instance
     * @throws IOException on i/o error
     */
    public abstract DataSource<V> getBasicDataSource(Object externalSource) throws IOException;

}
