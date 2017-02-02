
package com.github.attatrol.preprocessing.ui;

import java.io.IOException;

import com.github.attatrol.preprocessing.datasource.DataSource;
import com.github.attatrol.preprocessing.datasource.DefaultTokenDataSource;
import com.github.attatrol.preprocessing.datasource.parsing.TokenFeatures;
import com.github.attatrol.preprocessing.datasource.parsing.missing.MissingTokenSubstitutor;
import com.github.attatrol.preprocessing.datasource.parsing.missing.MissingTokenSubstitutorFactory;
import com.github.attatrol.preprocessing.datasource.parsing.record.RecordTokenizer;
import com.github.attatrol.preprocessing.datasource.parsing.token.TokenParser;
import com.github.attatrol.preprocessing.ui.TokenDataSourceDialog.TokenDataSourceDialogState;
import com.github.attatrol.preprocessing.ui.misc.UiUtils;
import com.github.attatrol.preprocessing.ui.uimodel.UnprocessedTokenDataSource;

import javafx.scene.control.Button;

/**
 * Button that creates default token data source.
 * @author atta_troll
 *
 */
class CreateDefaultTokenDataSourceButton extends Button {

    @SuppressWarnings({
        "unchecked",
        "rawtypes"
    })
    public CreateDefaultTokenDataSourceButton(TokenDataSourceDialog form) {
        super("Generate token parsers");
        setOnAction(ev -> {
            form.setState(TokenDataSourceDialogState.TOKEN_SOURCE_IN_GENERATION_6);
            final TokenDataSourceEnitities entities = form.getTokenDataSourceEntities();
            final TokenFeatures[] features = entities.getTokenFeatures();
            final int numberOfTokens = features.length;
            final TokenParser<?, ?>[] tokenParsers = entities.getTokenParsers();
            MissingTokenSubstitutor<?>[] substitutors = null;
            try {
                substitutors = produceSubstitutors(entities);
                entities.setTokenDataSource(new DefaultTokenDataSource(entities.getBasicDataSource(),
                        entities.getDataSourceSyntax().getTokenizer(), numberOfTokens, tokenParsers,
                        substitutors));
                form.setState(TokenDataSourceDialogState.TOKEN_SOURCE_SET_7);
            }
            catch (IllegalArgumentException | IOException ex) {
                UiUtils.showExceptionMessage(ex);
                form.setState(TokenDataSourceDialogState.MISSING_SUBSTITUTOR_GENERATION_ERROR);
            }
        });
    }

    private static MissingTokenSubstitutor<?>[] produceSubstitutors(
            TokenDataSourceEnitities entities) throws IOException, IllegalArgumentException {
        final TokenFeatures[] features = entities.getTokenFeatures();
        final int numberOfTokens = features.length;
        final MissingTokenSubstitutor<?>[] substitutors =
                new MissingTokenSubstitutor<?>[numberOfTokens];
        final DataSource<?> basicDataSource = entities.getBasicDataSource();
        final RecordTokenizer<?, ?> tokenizer = entities.getDataSourceSyntax().getTokenizer();
        final UnprocessedTokenDataSource rawDataSource =
                new UnprocessedTokenDataSource(basicDataSource, numberOfTokens, tokenizer);
        for (int i = 0; i < numberOfTokens; i++) {
            final MissingTokenSubstitutorFactory<?> factory =
                    features[i].getMissingTokenSubstitutor();
            if (factory != null) {
                substitutors[i] = factory.produceSubstitutor(rawDataSource, i);
            }
        }
        return substitutors;
    }

}
