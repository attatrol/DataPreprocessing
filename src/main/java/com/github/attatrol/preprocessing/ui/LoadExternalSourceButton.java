package com.github.attatrol.preprocessing.ui;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import com.github.attatrol.preprocessing.ui.TokenDataSourceDialog.TokenDataSourceDialogState;
import com.github.attatrol.preprocessing.ui.misc.UiUtils;
import com.github.attatrol.preprocessing.ui.uimodel.ExternalSourceTypeRegister;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

/**
 * Shows dialog of loading of external data sources.
 * @author atta_troll
 *
 */
class LoadExternalSourceButton extends Button {

	public LoadExternalSourceButton(TokenDataSourceDialog form) {
		super("Choose source");
		setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Optional<DataSourceTypeAndObject> typeAndFile = (new FileChooserDialog()).showAndWait();
				if (typeAndFile.isPresent()) {
					form.setState(TokenDataSourceDialogState.SOURCE_FILE_LOADING_1);
					final TokenDataSourceEnitities state = form.getTokenDataSourceEntities();
					final Object externalSource = typeAndFile.get().getFile();
					state.setExternalSource(externalSource);
					state.setExternalSourceType(typeAndFile.get().getType());
					try {
						form.setAdditionalContent(typeAndFile.get().getType().createPreviewPane(externalSource));
					} catch (IOException e) {
						UiUtils.showExceptionMessage(e);
						form.setState(TokenDataSourceDialogState.FILE_READING_ERROR);
					}
					form.setState(TokenDataSourceDialogState.SOURCE_FILE_SET_2);
				}
			}

		});
	}

	/**
	 * Locally used bean.
	 * 
	 * @author atta_troll
	 *
	 */
	private static class DataSourceTypeAndObject {

		private final Object externalSource;

		private final ExternalSourceTypeRegister type;

		public DataSourceTypeAndObject(Object externalSource, ExternalSourceTypeRegister type) {
			this.externalSource = externalSource;
			this.type = type;
		}

		public Object getFile() {
			return externalSource;
		}

		public ExternalSourceTypeRegister getType() {
			return type;
		}
	}

	/**
	 * Source chooser dialog.
	 * @author atta_troll
	 *
	 */
	private static class FileChooserDialog extends Dialog<DataSourceTypeAndObject> {

		private static File lastDirectory;

		private ObjectProperty<File> chosenFile = new SimpleObjectProperty<>();

		private FileChooser fileChooser = new FileChooser();
		private DirectoryChooser dirChooser = new DirectoryChooser();
		{
			if (lastDirectory != null) {
				fileChooser.setInitialDirectory(lastDirectory);
				dirChooser.setInitialDirectory(lastDirectory);
			}
		}

		private Button fileChooserButton = new Button("Choose a file");

		private ComboBox<ExternalSourceTypeRegister> dataSourceTypeComboBox = new ComboBox<ExternalSourceTypeRegister>();
		{
			dataSourceTypeComboBox.getItems().addAll(ExternalSourceTypeRegister.values());
			dataSourceTypeComboBox.setOnAction(e -> chosenFile.set(null));
		}

		public FileChooserDialog() {
			setTitle("Choose external data source");
			fileChooserButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
				    ExternalSourceTypeRegister selected = dataSourceTypeComboBox.getSelectionModel().getSelectedItem();
					if (selected == ExternalSourceTypeRegister.DIRECTORY) {
						chosenFile.set(dirChooser.showDialog(null));
					}
					else if (selected == ExternalSourceTypeRegister.SINGLE_FILE) {
						chosenFile.set(fileChooser.showOpenDialog(null));
					}
					else {
						UiUtils.showInfoMessage("Choose data source option from combo box");
					}

				}

			});
			getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
			Node okButton = getDialogPane().lookupButton(ButtonType.OK);
			okButton.setDisable(true);
			chosenFile.addListener((observable, oldValue, newValue) -> okButton.setDisable(newValue == null));
			setResultConverter(dialogButton -> {
				if (dialogButton == ButtonType.OK) {
					return new DataSourceTypeAndObject(chosenFile.get(),
							dataSourceTypeComboBox.valueProperty().get());
				}
				return null;
			});
			GridPane grid = UiUtils.getGridPane();
			grid.add(new Label("Choose data source type"), 0, 0);
			grid.add(dataSourceTypeComboBox, 0, 1);
			grid.add(fileChooserButton, 0, 2);
			getDialogPane().setContent(grid);
		}

	}

}
