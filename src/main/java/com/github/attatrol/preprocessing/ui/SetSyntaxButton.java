
package com.github.attatrol.preprocessing.ui;

import java.io.IOException;

import com.github.attatrol.preprocessing.datasource.DataSource;
import com.github.attatrol.preprocessing.datasource.TitledDataSource;
import com.github.attatrol.preprocessing.datasource.parsing.TokenFeatures;
import com.github.attatrol.preprocessing.datasource.parsing.TokenTypeIdentifier;
import com.github.attatrol.preprocessing.datasource.parsing.record.RecordTokenizer;
import com.github.attatrol.preprocessing.datasource.syntax.TitledTokenDataSourceSyntax;
import com.github.attatrol.preprocessing.datasource.syntax.TokenDataSourceSyntax;
import com.github.attatrol.preprocessing.ui.TokenDataSourceDialog.TokenDataSourceDialogState;
import com.github.attatrol.preprocessing.ui.i18n.UiI18nProvider;
import com.github.attatrol.preprocessing.ui.misc.UiUtils;

import javafx.scene.control.Button;

/**
 * Confirms {@link TokenDataSourceSyntax} choice in {@link DataSourceSyntaxComboBox}, then creates
 * and registers in entities basic data source and token features.
 * 
 * @author atta_troll
 *
 */
class SetSyntaxButton extends Button {

    public SetSyntaxButton(TokenDataSourceDialog form) {
        super(UiI18nProvider.INSTANCE.getValue("data.source.dialog.button.set.syntax"));
        setOnAction(ev -> {
            form.setState(TokenDataSourceDialogState.SOURCE_SYNTAX_LOADING_4);
            try {
                final TokenDataSourceEnitities entities = form.getTokenDataSourceEntities();
                final TokenDataSourceSyntax<?, ?> syntax = entities.getDataSourceSyntax();
                final DataSource<?> basicDataSource =
                        syntax.getBasicDataSource(entities.getExternalSource());
                entities.setBasicDataSource(basicDataSource);
                final TokenFeatures[] features = produceTokenFeatures(basicDataSource, syntax);
                entities.setTokenFeatures(features);
                form.setAdditionalContent(TokenParserSetupPane.createTokenSetupPane(form));
            }
            catch (IOException ex) {
                UiUtils.showExceptionMessage(ex);
                form.setState(TokenDataSourceDialogState.DATA_SOURCE_SYNTAX_ERROR);
            }
            form.setState(TokenDataSourceDialogState.SOURCE_SYNTAX_SET_5);
        });
    }

    /**
     * Creates array of token features for current data source.
     * 
     * @param basicDataSource
     *        current basic data source
     * @param syntax
     *        syntax for current data source
     * @return token features that describe tokens of current data source
     * @throws IOException
     *         on data source i/o exception
     */
    @SuppressWarnings({
        "unchecked",
        "rawtypes"
    })
    private static TokenFeatures[] produceTokenFeatures(DataSource<?> basicDataSource,
            TokenDataSourceSyntax<?, ?> syntax) throws IOException {
        TokenFeatures[] features = null;
        if (basicDataSource instanceof TitledDataSource
                && ((TitledDataSource<?>) basicDataSource).getTitles() != null) {
            features = TokenTypeIdentifier.defineTokenTypes((TitledDataSource) basicDataSource,
                    (RecordTokenizer) syntax.getTokenizer(), syntax,
                    ((TitledTokenDataSourceSyntax) syntax).getTitleTokenizer());
        }
        else {
            features = TokenTypeIdentifier.defineTokenTypes((DataSource) basicDataSource,
                    (RecordTokenizer) syntax.getTokenizer(), syntax);
        }
        basicDataSource.reset();
        return features;
    }

}
