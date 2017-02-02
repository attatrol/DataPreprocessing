
package com.github.attatrol.preprocessing.ui;

import java.io.IOException;

import com.github.attatrol.preprocessing.datasource.DataSource;
import com.github.attatrol.preprocessing.datasource.DefaultTokenDataSource;
import com.github.attatrol.preprocessing.datasource.parsing.TokenFeatures;
import com.github.attatrol.preprocessing.datasource.parsing.TokenType;
import com.github.attatrol.preprocessing.datasource.parsing.token.TokenParser;
import com.github.attatrol.preprocessing.datasource.syntax.TokenDataSourceSyntax;
import com.github.attatrol.preprocessing.ui.misc.UiUtils;
import com.github.attatrol.preprocessing.ui.uimodel.ExternalSourceTypeRegister;

/**
 * Simple POJO used to hold all model entities together.
 * 
 * @author atta_troll
 *
 */
class TokenDataSourceEnitities {

    /**
     * External source that used to produce basic data source.
     */
    private Object externalSource;

    /**
     * Data source type, describes structure of external source
     */
    private ExternalSourceTypeRegister externalSourceType;

    private TokenDataSourceSyntax<?, ?> dataSourceSyntax;

    private TokenFeatures[] tokenFeatures;

    private DataSource<?> basicDataSource;

    private DefaultTokenDataSource<?> tokenDataSource;

    public Object getExternalSource() {
        return externalSource;
    }

    public void setExternalSource(Object externalSource) {
        this.externalSource = externalSource;
    }

    public ExternalSourceTypeRegister getExternalSourceType() {
        return externalSourceType;
    }

    public void setExternalSourceType(ExternalSourceTypeRegister externalSourceType) {
        this.externalSourceType = externalSourceType;
    }

    public TokenDataSourceSyntax<?, ?> getDataSourceSyntax() {
        return dataSourceSyntax;
    }

    public void setDataSourceSyntax(TokenDataSourceSyntax<?, ?> dataSourceSyntax) {
        this.dataSourceSyntax = dataSourceSyntax;
    }

    public TokenFeatures[] getTokenFeatures() {
        return tokenFeatures;
    }

    public void setTokenFeatures(TokenFeatures[] tokenFeatures) {
        this.tokenFeatures = tokenFeatures;
    }

    public DefaultTokenDataSource<?> getTokenDataSource() {
        return tokenDataSource;
    }

    public void setTokenDataSource(DefaultTokenDataSource<?> tokenDataSource) {
        this.tokenDataSource = tokenDataSource;
    }

    public DataSource<?> getBasicDataSource() {
        return basicDataSource;
    }

    public void setBasicDataSource(DataSource<?> basicDataSource) {
        this.basicDataSource = basicDataSource;
    }

    public String[] getTitles() {
        String[] titles = null;
        if (tokenFeatures != null) {
            titles = new String[tokenFeatures.length];
            for (int i = 0; i < tokenFeatures.length; i++) {
                titles[i] = tokenFeatures[i].getTitle();
            }
        }
        return titles;
    }

    public TokenParser<?,?>[] getTokenParsers() {
        TokenParser<?, ?>[] tokenParsers = null;
        if (dataSourceSyntax != null && tokenFeatures != null) {
            tokenParsers = new TokenParser<?,?>[tokenFeatures.length];
            for (int i = 0; i < tokenFeatures.length; i++) {
                tokenParsers[i] = dataSourceSyntax.getTokenParser(tokenFeatures[i].getType());
            }
        }
        return tokenParsers;
    }

    public TokenType[] getTokenTypes() {
        TokenType[] tokenParsers = null;
        if (tokenFeatures != null) {
            tokenParsers = new TokenType[tokenFeatures.length];
            for (int i = 0; i < tokenFeatures.length; i++) {
                tokenParsers[i] = tokenFeatures[i].getType();
            }
        }
        return tokenParsers;
    }

    public void erase() {
        externalSource = null;
        externalSourceType = null;
        dataSourceSyntax = null;
        tokenFeatures = null;
        try {
            if (basicDataSource != null) {
                basicDataSource.close();
            }
        }
        catch (IOException ex) {
            UiUtils.showExceptionMessage(ex);
        }
        tokenDataSource = null;
        basicDataSource = null;
    }
}
