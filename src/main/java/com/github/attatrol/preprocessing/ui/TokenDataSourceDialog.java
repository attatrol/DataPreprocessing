
package com.github.attatrol.preprocessing.ui;

import com.github.attatrol.preprocessing.datasource.DefaultTokenDataSource;
import com.github.attatrol.preprocessing.datasource.syntax.SyntaxRegister;
import com.github.attatrol.preprocessing.ui.i18n.UiI18nProvider;
import com.github.attatrol.preprocessing.ui.misc.UiUtils;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;

/**
 * Dialog for interactive creation of {@link DefaultTokenDataSource}.
 * @author atta_troll
 *
 */
public class TokenDataSourceDialog extends Dialog<TokenDataSourceAndMisc> {

    /**
     * Pane that actually contains most of controls.
     */
    BorderPane contentPane = new BorderPane();
    {
        contentPane.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth() - 15.);
    }

    /**
     * Red label that shows current status of the process of creation of {@link DefaultTokenDataSource}
     */
    private Label statusLabel = new Label();
    {
        statusLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold; -fx-font-size: 19;");
        statusLabel.setWrapText(true);
    }

    private ProgressBar progressBar = new ProgressBar();
    {
        // no progress
        progressBar.setProgress(0.f);
    }

    private Button loadButton = new LoadExternalSourceButton(this);

    private ComboBox<SyntaxRegister> setupSyntaxComboBox = new DataSourceSyntaxComboBox(this);

    private Button setSyntaxButton = new SetSyntaxButton(this);

    private Button createDataSourceButton = new CreateDefaultTokenDataSourceButton(this);

    private Button okButton;

    /**
     * Dialog automata state
     */
    @SuppressWarnings("unused")
    private TokenDataSourceDialogState state;

    /**
     * Complete model state, all entities used for creation of a {@link DefaultTokenDataSource}.
     */
    private TokenDataSourceEnitities entities;

    public TokenDataSourceDialog() {
        setResizable(true);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return new TokenDataSourceAndMisc(entities.getTokenDataSource(),
                        entities.getTitles(), entities.getTokenTypes());
            }
            else {
                // cancel button shall close basic data source
                entities.erase();
                return null;
            }
        });
        fillContentPane();
        this.getDialogPane().setContent(contentPane);
        entities = new TokenDataSourceEnitities();
        setState(TokenDataSourceDialogState.INITIAL_0);
        setTitle(UiI18nProvider.INSTANCE.getValue("data.source.dialog.title"));
    }

    public void setAdditionalContent(Node node) {
        contentPane.setCenter(node);
    }

    public void setState(TokenDataSourceDialogState newState) {
        state = newState;
        statusLabel.setText(newState.getStatusText());
        newState.applyState(this);
    }

    public TokenDataSourceEnitities getTokenDataSourceEntities() {
        return entities;
    }

    private void fillContentPane() {
        GridPane buttonGrid = UiUtils.getGridPane();
        buttonGrid.setMaxWidth(400);
        buttonGrid.add(statusLabel, 0, 0, 2, 1);
        buttonGrid.add(progressBar, 0, 1);
        buttonGrid.add(loadButton, 0, 2);
        buttonGrid.add(new Label(UiI18nProvider
                .INSTANCE.getValue("data.source.dialog.label.set.syntax")), 0, 3);
        setupSyntaxComboBox.setMinWidth(100);
        buttonGrid.add(setupSyntaxComboBox, 0, 4);
        buttonGrid.add(new Label(UiI18nProvider
                .INSTANCE.getValue("data.source.dialog.label.confirm.syntax")), 0, 5);
        buttonGrid.add(setSyntaxButton, 0, 6);
        buttonGrid.add(new Label(UiI18nProvider
                .INSTANCE.getValue("data.source.dialog.label.setup.parsing")), 0, 7);
        buttonGrid.add(createDataSourceButton, 0, 8);
        // border pane
        contentPane.setMinWidth(1000);
        contentPane.setMinHeight(500);
        contentPane.setLeft(buttonGrid);
    }

    public enum TokenDataSourceDialogState {
        INITIAL_0(UiI18nProvider.INSTANCE.getValue("data.source.dialog.state.0")) {

            @Override
            public void applyState(TokenDataSourceDialog form) {
                form.setAdditionalContent(null);
                form.progressBar.setProgress(0.f);
                form.setupSyntaxComboBox.getSelectionModel().clearSelection();
                form.setupSyntaxComboBox.getItems().clear();
                setControlsDisabled(form, false, true, true, true, true);
            }

        },
        SOURCE_FILE_LOADING_1(UiI18nProvider.INSTANCE.getValue("data.source.dialog.state.1")) {

            @Override
            public void applyState(TokenDataSourceDialog form) {
                form.getTokenDataSourceEntities().erase();
                form.progressBar.setProgress(-1.f);
                form.setupSyntaxComboBox.getSelectionModel().clearSelection();
                form.setupSyntaxComboBox.getItems().clear();
                setControlsDisabled(form, true, true, true, true, true);
            }

        },
        SOURCE_FILE_SET_2(UiI18nProvider.INSTANCE.getValue("data.source.dialog.state.2")) {

            @Override
            public void applyState(TokenDataSourceDialog form) {
                form.progressBar.setProgress(0.f);
                form.setupSyntaxComboBox.getItems().clear();
                form.setupSyntaxComboBox.getItems()
                        .addAll(form.entities.getExternalSourceType().getPossibleSyntaxes());
                setControlsDisabled(form, false, false, true, true, true);
            }

        },
        SOURCE_SYNTAX_CHOSEN_3(String.format(UiI18nProvider
                .INSTANCE.getValue("data.source.dialog.state.3"),
                UiI18nProvider
                .INSTANCE.getValue("data.source.dialog.button.set.syntax"))) {

            @Override
            public void applyState(TokenDataSourceDialog form) {
                form.progressBar.setProgress(0.f);
                setControlsDisabled(form, false, false, false, true, true);
            }

        },
        SOURCE_SYNTAX_LOADING_4(UiI18nProvider.INSTANCE.getValue("data.source.dialog.state.4")) {

            @Override
            public void applyState(TokenDataSourceDialog form) {
                form.progressBar.setProgress(-1.f);
                setControlsDisabled(form, true, true, true, true, true);
            }

        },
        SOURCE_SYNTAX_SET_5(UiI18nProvider.INSTANCE.getValue("data.source.dialog.state.5")) {

            @Override
            public void applyState(TokenDataSourceDialog form) {
                form.progressBar.setProgress(0.f);
                setControlsDisabled(form, false, false, false, false, true);
            }

        },
        TOKEN_SOURCE_IN_GENERATION_6(UiI18nProvider
                .INSTANCE.getValue("data.source.dialog.state.6")) {

            @Override
            public void applyState(TokenDataSourceDialog form) {
                form.progressBar.setProgress(-1.f);
                setControlsDisabled(form, true, true, true, true, true);
            }

        },
        TOKEN_SOURCE_SET_7(UiI18nProvider.INSTANCE.getValue("data.source.dialog.state.7")) {

            @Override
            public void applyState(TokenDataSourceDialog form) {
                form.progressBar.setProgress(0.f);
                setControlsDisabled(form, false, false, false, false, false);
            }

        },
        FILE_READING_ERROR(UiI18nProvider.INSTANCE.getValue("data.source.dialog.state.error.io")) {

            @Override
            public void applyState(TokenDataSourceDialog form) {
                INITIAL_0.applyState(form);
            }

        },
        DATA_SOURCE_SYNTAX_ERROR(UiI18nProvider.INSTANCE.getValue(
                "data.source.dialog.state.error.syntax")) {

            @Override
            public void applyState(TokenDataSourceDialog form) {
                INITIAL_0.applyState(form);
            }

        },
        MISSING_SUBSTITUTOR_GENERATION_ERROR(UiI18nProvider
                .INSTANCE.getValue("data.source.dialog.state.error.substitutor.generation")) {

            @Override
            public void applyState(TokenDataSourceDialog form) {
                INITIAL_0.applyState(form);
            }

        };

        private String statusText;

        TokenDataSourceDialogState(String statusText) {
            this.statusText = statusText;
        }

        public String getStatusText() {
            return statusText;
        }

        public abstract void applyState(TokenDataSourceDialog form);

        private static void setControlsDisabled(TokenDataSourceDialog form, boolean isDisabledLoadButton,
                boolean isDisabledSyntaxSetupComboBox, boolean isDisabledSetSyntaxButton,
                boolean isDisabledCreateDataSourceButton, boolean isDisabledOkButton) {
            form.loadButton.setDisable(isDisabledLoadButton);
            form.setupSyntaxComboBox.setDisable(isDisabledSyntaxSetupComboBox);
            form.setSyntaxButton.setDisable(isDisabledSetSyntaxButton);
            form.createDataSourceButton.setDisable(isDisabledCreateDataSourceButton);
            form.okButton.setDisable(isDisabledOkButton);
        }

    }

}
