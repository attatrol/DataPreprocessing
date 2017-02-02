package com.github.attatrol.preprocessing.ui;

import com.github.attatrol.preprocessing.datasource.syntax.SyntaxRegister;
import com.github.attatrol.preprocessing.ui.TokenDataSourceDialog.TokenDataSourceDialogState;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;

class DataSourceSyntaxComboBox extends ComboBox<SyntaxRegister> {

	public DataSourceSyntaxComboBox(TokenDataSourceDialog form) {

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
