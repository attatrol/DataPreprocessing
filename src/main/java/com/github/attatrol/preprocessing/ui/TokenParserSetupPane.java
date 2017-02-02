
package com.github.attatrol.preprocessing.ui;

import java.io.IOException;

import com.github.attatrol.preprocessing.datasource.DataSource;
import com.github.attatrol.preprocessing.datasource.parsing.TokenFeatures;
import com.github.attatrol.preprocessing.datasource.parsing.TokenType;
import com.github.attatrol.preprocessing.datasource.parsing.missing.MissingTokenSubstitutor;
import com.github.attatrol.preprocessing.datasource.parsing.missing.MissingTokenSubstitutorFactory;
import com.github.attatrol.preprocessing.datasource.parsing.record.RecordTokenizer;
import com.github.attatrol.preprocessing.datasource.syntax.TokenDataSourceSyntax;
import com.github.attatrol.preprocessing.ui.TokenDataSourceDialog.TokenDataSourceDialogState;
import com.github.attatrol.preprocessing.ui.misc.UiUtils;
import com.github.attatrol.preprocessing.ui.uimodel.UnprocessedTokenDataSource;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

/**
 * Holds controls for interactive token setup.
 * @author atta_troll
 *
 */
class TokenParserSetupPane extends GridPane {

    /**
     * Number of records to be shown in token views.
     */
    public static final int RECORD_LIMIT = 100;

    /**
     * Button that loads next {@link #RECORD_LIMIT} records in token views.
     */
    private Button loadNextRecords =
            new Button(String.format("Load %d next records", RECORD_LIMIT));

    /**
     * Button that reloads data source to the beginning.
     */
    private Button reloadRecords = new Button("Reload records");

    /**
     * Private ctor. Used by {@link #createTokenSetupPane(TokenDataSourceDialog)}
     * exclusively.
     * @param featuresViews features views instances to be shown in this grid view.
     */
    private TokenParserSetupPane(TokenColumnView[] featuresViews) {
        setAlignment(Pos.CENTER);
        setHgap(10);
        setVgap(10);
        setPadding(new Insets(25, 25, 25, 25));
        add(loadNextRecords, 0, 0, 2, 1);
        add(reloadRecords, 2, 0, 2, 1);
        for (int i = 0; i < featuresViews.length; i++) {
            add(new Label("Enter title:"), i, 1);
            add(featuresViews[i].getTitleTextField(), i, 2);
            add(new Label("Choose token type:"), i, 3);
            add(featuresViews[i].getTokenTypeComboBox(), i, 4);
            if (featuresViews[i].hasOmissions()) {
                add(new Label("Choose substitutor for missing tokens:"), i, 5);
                add(featuresViews[i].getSubstitutorComboBox(), i, 6);
                add(featuresViews[i].getTokenPreview(), i, 7);
            }
            else {
                add(featuresViews[i].getTokenPreview(), i, 5, 1, 3);
            }

        }
        setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
    }

    /**
     * Creates wrapped into {@link ScrollPane} {@link #TokenParserSetupPane} instance.
     * @param form {@link #TokenDataSourceDialog} instance that uses this pane
     * @return wrapped into {@link ScrollPane} {@link #TokenParserSetupPane} instance
     * @throws IOException on i/o basic data source error.
     */
    public static ScrollPane createTokenSetupPane(TokenDataSourceDialog form) throws IOException {
        final TokenDataSourceEnitities entities = form.getTokenDataSourceEntities();
        final TokenDataSourceSyntax<?, ?> syntax = entities.getDataSourceSyntax();
        final DataSource<?> basicDataSource = entities.getBasicDataSource();
        TokenFeatures[] initialFeatures = entities.getTokenFeatures();
        TokenColumnView[] featuresViews = produceTokenColumnViews(initialFeatures);
        // should not be closed as wrapped basic data source will be used further
        final TokenSetupDataSource rawDataSource = new TokenSetupDataSource(basicDataSource,
                initialFeatures.length, syntax.getTokenizer(), featuresViews);
        final TokenParserSetupPane pane = new TokenParserSetupPane(featuresViews);
        rawDataSource.fillConsumers();
        pane.loadNextRecords.setOnAction(new LoadButtonClickHandler(rawDataSource, form));
        pane.reloadRecords.setOnAction(new ReloadButtonClickHandler(rawDataSource, form));
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(pane);
        return scrollPane;
    }

    /**
     * Produces {@link #TokenColumnView}s for this {@link #TokenFeatures}.
     * @param features
     * @return
     */
    private static TokenColumnView[] produceTokenColumnViews(TokenFeatures[] features) {
        TokenColumnView[] featuresViews = new TokenColumnView[features.length];
        for (int i = 0; i < features.length; i++) {
            featuresViews[i] = new TokenColumnView(features[i]);
        }
        return featuresViews;
    }

    private static class TokenColumnView {

        private final TokenFeatures features;

        private final ListView<Object> tokenPreview = new ListView<>();
        {
            tokenPreview.setCellFactory(param -> {

                ListCell<Object> cell = new ListCell<Object>() {
                    @Override
                    protected void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                            setGraphic(null);
                            setStyle(null);
                        }
                        else {
                            if (item == null) {
                                setText("NIL");
                                setStyle("-fx-background-color: red;");
                            }
                            else {
                                setStyle(null);
                                setText(item.toString());
                            }
                        }
                    }
                };
                return cell;
            });
        }

        private final ComboBox<TokenType> tokenTypeComboBox = new ComboBox<>();

        private final ComboBox<MissingTokenSubstitutorFactory<?>> substitutorComboBox =
                new ComboBox<>();

        private final TextField titleTextField = new TextField();

        public TokenColumnView(TokenFeatures features) {
            this.features = features;
            titleTextField.setText(features.getTitle());
            titleTextField.textProperty().addListener((observable, oldValue, newValue) -> 
                    features.setTitle(titleTextField.getText()));
            tokenTypeComboBox.getItems().add(features.getType());
            tokenTypeComboBox.getItems().addAll(TokenType.getGeneralTypes(features.getType()));
            tokenTypeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                features.setType(newValue);
                if (oldValue != newValue && hasOmissions()) {
                    substitutorComboBox.getItems().clear();
                    MissingTokenSubstitutorFactory<?>[] factories =
                            MissingTokenSubstitutor.SUBSTITUTOR_REGISTER.get(newValue);
                    substitutorComboBox.getItems().addAll(factories != null ? factories
                            : new MissingTokenSubstitutorFactory<?>[0]);
                    substitutorComboBox.getSelectionModel().select(0);
                }
            });
            substitutorComboBox.valueProperty().addListener((observable, oldValue,
                    newValue) -> features.setMissingTokenSubstitutor(newValue));
            tokenTypeComboBox.getSelectionModel().select(0);
        }

        public TextField getTitleTextField() {
            return titleTextField;
        }

        public ListView<Object> getTokenPreview() {
            return tokenPreview;
        }

        public ComboBox<TokenType> getTokenTypeComboBox() {
            return tokenTypeComboBox;
        }

        public ComboBox<MissingTokenSubstitutorFactory<?>> getSubstitutorComboBox() {
            return substitutorComboBox;
        }

        public boolean hasOmissions() {
            return features.hasOmissions();
        }

    }

    /**
     * This token data source don't parse individual tokens,
     * used as an incomplete step in
     * user controlled process of creation of a proper data source.<br/>
     * Its second task is to fill multiple GUI consumers
     * with raw tokens in a single traverse.
     * 
     * @author atta_troll
     *
     * @param <V>
     *        record type
     */
    @SuppressWarnings({
        "rawtypes",
        "unchecked"
    })
    private static class TokenSetupDataSource extends UnprocessedTokenDataSource {

        private ObservableList<Object>[] consumers;

        public TokenSetupDataSource(DataSource internalDataSource, int recordLength,
                RecordTokenizer tokenizer, TokenColumnView[] views) {
            super(internalDataSource, recordLength, tokenizer);
            consumers = new ObservableList[views.length];
            for (int i = 0; i < views.length; i++) {
                consumers[i] = views[i].getTokenPreview().getItems();
            }
        }

        public void fillConsumers() throws IllegalArgumentException, IOException {
            if (hasNext()) {
                for (ObservableList<Object> list : consumers) {
                    list.clear();
                }
            }
            for (int i = 0; i < RECORD_LIMIT && hasNext(); i++) {
                Object[] tokens = next().getData();
                for (int j = 0; j < tokens.length; j++) {
                    consumers[j].add(tokens[j]);
                }
            }
        }
    }

    private static class LoadButtonClickHandler implements EventHandler<ActionEvent> {

        private TokenSetupDataSource rawDataSource;

        private TokenDataSourceDialog form;

        public LoadButtonClickHandler(TokenSetupDataSource rawDataSource,
                TokenDataSourceDialog form) {
            this.rawDataSource = rawDataSource;
            this.form = form;
        }

        @Override
        public void handle(ActionEvent event) {
            try {
                rawDataSource.fillConsumers();
            }
            catch (IllegalArgumentException | IOException ex) {
                UiUtils.showExceptionMessage(ex);
                form.setState(TokenDataSourceDialogState.DATA_SOURCE_SYNTAX_ERROR);
            }
        }
        
    }

    private static class ReloadButtonClickHandler implements EventHandler<ActionEvent> {

        private TokenSetupDataSource rawDataSource;

        private TokenDataSourceDialog form;

        public ReloadButtonClickHandler(TokenSetupDataSource rawDataSource,
                TokenDataSourceDialog form) {
            this.rawDataSource = rawDataSource;
            this.form = form;
        }

        @Override
        public void handle(ActionEvent event) {
            try {
                rawDataSource.reset();
                rawDataSource.fillConsumers();
            }
            catch (IllegalArgumentException | IOException ex) {
                UiUtils.showExceptionMessage(ex);
                form.setState(TokenDataSourceDialogState.DATA_SOURCE_SYNTAX_ERROR);
            }
        }
        
    }
}
