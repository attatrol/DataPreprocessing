
package com.github.attatrol.preprocessing.datasource.syntax;

import java.io.File;

import com.github.attatrol.preprocessing.datasource.DataSource;
import com.github.attatrol.preprocessing.datasource.TextFileDataSource;
import com.github.attatrol.preprocessing.datasource.parsing.TokenType;
import com.github.attatrol.preprocessing.datasource.parsing.record.CommaSeparatedStringSplitter;
import com.github.attatrol.preprocessing.datasource.parsing.record.TabulationSeparatedStringSplitter;

/**
 * This enum contains syntaxes for all known data sources.
 * 
 * @author atta_troll
 *
 */
public enum SyntaxRegister {

    COMMA_SEPARATED_LINES(new TokenDataSourceSyntax<String, String>(new CommaSeparatedStringSplitter(),
            StringTokenParsers.STRING_TOKEN_PARSERS) {

        @Override
        public DataSource<String> getBasicDataSource(Object file) {
            return new TextFileDataSource((File) file, false);
        }

        @Override
        public TokenType detectTokenType(Object token) {
            return StringTokenParsers.detectStringTokenType(token);
        }
    }),
    COMMA_SEPARATED_TITLED_LINES(
            new TitledTokenDataSourceSyntax<String, String>(new CommaSeparatedStringSplitter(),
                    new CommaSeparatedStringSplitter(), StringTokenParsers.STRING_TOKEN_PARSERS) {

                @Override
                public DataSource<String> getBasicDataSource(Object file) {
                    return new TextFileDataSource((File) file, true);
                }

                @Override
                public TokenType detectTokenType(Object token) {
                    return StringTokenParsers.detectStringTokenType(token);
                }
            }),
    TABULATION_SEPARATED_LINES(new TokenDataSourceSyntax<String, String>(new TabulationSeparatedStringSplitter(),
            StringTokenParsers.STRING_TOKEN_PARSERS) {

        @Override
        public DataSource<String> getBasicDataSource(Object file) {
            return new TextFileDataSource((File) file, false);
        }

        @Override
        public TokenType detectTokenType(Object token) {
            return StringTokenParsers.detectStringTokenType(token);
        }
    }),
    TABULATION_SEPARATED_TITLED_LINES(new TitledTokenDataSourceSyntax<String, String>(
            new TabulationSeparatedStringSplitter(), new TabulationSeparatedStringSplitter(),
            StringTokenParsers.STRING_TOKEN_PARSERS) {

        @Override
        public DataSource<String> getBasicDataSource(Object file) {
            return new TextFileDataSource((File) file, true);
        }

        @Override
        public TokenType detectTokenType(Object token) {
            return StringTokenParsers.detectStringTokenType(token);
        }
    });

    private TokenDataSourceSyntax<?, ?> syntax;

    SyntaxRegister(TokenDataSourceSyntax<?, ?> syntax) {
        this.syntax = syntax;
    }

    public TokenDataSourceSyntax<?, ?> getSyntax() {
        return syntax;
    }
}
