
package com.github.attatrol.preprocessing.ui;

import com.github.attatrol.preprocessing.datasource.syntax.SyntaxRegister;
import com.github.attatrol.preprocessing.ui.TokenDataSourceDialog.TokenDataSourceDialogState;
import com.github.attatrol.preprocessing.ui.i18n.ToStringMappingI18nComboBox;
import com.github.attatrol.preprocessing.ui.i18n.UiI18nComboBox;
import com.github.attatrol.preprocessing.ui.i18n.UiI18nProvider;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;

/**
 * Combo box that allows choosing of the data source syntax
 * 
 * @author atta_troll
 *
 */
class DataSourceSyntaxComboBox extends ToStringMappingI18nComboBox<SyntaxRegister> {

    public DataSourceSyntaxComboBox(TokenDataSourceDialog form) {
        super(UiI18nProvider.INSTANCE);
        setOnHidden(new EventHandler<Event>() {

            @Override
            public void handle(Event event) {
                if (!getSelectionModel().isEmpty()) {
                    final TokenDataSourceEnitities entities = form.getTokenDataSourceEntities();
                    final SyntaxRegister syntaxType = getSelectionModel().getSelectedItem();
                    entities.setDataSourceSyntax(syntaxType.getSyntax());
                    form.setState(TokenDataSourceDialogState.SOURCE_SYNTAX_CHOSEN_3);
                }
                else {
                    form.setState(TokenDataSourceDialogState.SOURCE_FILE_SET_2);
                }
            }

        });
    }

}
