package com.github.attatrol.preprocessing.ui;

import com.github.attatrol.preprocessing.datasource.AbstractTokenDataSource;
import com.github.attatrol.preprocessing.datasource.parsing.TokenType;

/**
 * POJO, result of {@link TokenDataSourceDialog}.
 * @author atta_troll
 *
 */
public class TokenDataSourceAndMisc {
    /**
     * The token data source.
     */
    private final AbstractTokenDataSource<?> tokenDataSource;

    /**
     * Titles for each token column.
     */
    private final String[] titles;

    /**
     * Token types associated with the token data source.
     */
    private final TokenType[] tokenTypes;

    public TokenDataSourceAndMisc(AbstractTokenDataSource<?> tokenDataSource, String[] titles,
            TokenType[] tokenTypes) {
        this.tokenDataSource = tokenDataSource;
        this.titles = titles;
        this.tokenTypes = tokenTypes;
    }

    public AbstractTokenDataSource<?> getTokenDataSource() {
        return tokenDataSource;
    }

    public String[] getTitles() {
        return titles;
    }

    public TokenType[] getTokenTypes() {
        return tokenTypes;
    }
}
