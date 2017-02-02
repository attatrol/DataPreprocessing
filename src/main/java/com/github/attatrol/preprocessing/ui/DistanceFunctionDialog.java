
package com.github.attatrol.preprocessing.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.github.attatrol.preprocessing.datasource.AbstractTokenDataSource;
import com.github.attatrol.preprocessing.datasource.Record;
import com.github.attatrol.preprocessing.datasource.parsing.TokenType;
import com.github.attatrol.preprocessing.distance.DistanceFunction;
import com.github.attatrol.preprocessing.distance.MaskedDistanceFunction;
import com.github.attatrol.preprocessing.distance.Registers;
import com.github.attatrol.preprocessing.distance.metric.Metric;
import com.github.attatrol.preprocessing.distance.metric.NormalizedMetric;
import com.github.attatrol.preprocessing.distance.metric.PNorm;
import com.github.attatrol.preprocessing.distance.nonmetric.gower.GowerDistance;
import com.github.attatrol.preprocessing.distance.nonmetric.gower.GowerTokenSimilarityIndexFactory;
import com.github.attatrol.preprocessing.distance.nonmetric.similarity.DissimilarityFunction;
import com.github.attatrol.preprocessing.distance.nonmetric.similarity.SimilarityIndexFactory;
import com.github.attatrol.preprocessing.ui.misc.GenericValueReturnDialog;
import com.github.attatrol.preprocessing.ui.misc.PositiveDoubleTextField;
import com.github.attatrol.preprocessing.ui.misc.UiUtils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

/**
 * Dialog for interactive creation of {@link DistanceFunction} for some
 * {@link AbstractTokenDataSource}.
 * 
 * @author atta_troll
 *
 */
public class DistanceFunctionDialog extends GenericValueReturnDialog<DistanceFunction> {

    /*
     * Ui controls.
     */
    private GridPane gridPane = UiUtils.getGridPane();
    {
        gridPane.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth() - 15.);
    }

    private Label statusLabel = new Label();
    {
        statusLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        statusLabel.setWrapText(true);
        statusLabel.setMaxWidth(400);
    }

    private ToggleGroup group = new ToggleGroup();

    private RadioButton classicMetricRadioButton = new RadioButton("Classic metric function");

    private RadioButton normalizedMetricRadioButton = new RadioButton("Normalized metric function");

    private RadioButton syntheticRadioButton = new RadioButton("Gower's distance function");

    private RadioButton categoricalRadioButton =
            new RadioButton("Dissimilarity categorical distance");

    private Button showNextButton = new Button(String.format("Show %d next records",
            TokenDataSourceTableView.DEFAULT_SHOWN_RECORD_NUMBER));

    private Button showFromBeginningButton = new Button("Show from beginning");

    private Button okButton;

    /*
     * Volatile ui controls
     */
    private MetricComboBox metricComboBox = new MetricComboBox();

    private ComboBox<SimilarityIndexFactory<?>> sifFactoryComboBox = new ComboBox<>();
    {
        sifFactoryComboBox.getItems().addAll(Registers.SIMILARITY_INDEX_FACTORY_REGISTER);
    }

    private TokenDataSourceTableView tableView;

    /*
     * Imported state, not subject to change.
     */
    /**
     * Token data source.
     */
    private final AbstractTokenDataSource<?> dataSource;

    /**
     * Types for each token in record.
     */
    private final TokenType[] tokenTypes;

    /*
     * Internal state, mutable, subject to change by ui controls.
     */
    /**
     * Array of flags which define whether tokens will be used in metric calculation.
     */
    private boolean[] inUse;

    /**
     * Counter of tokens which are in use, must never reach zero.
     */
    private int inUseCounter;

    /**
     * Array of token difference calculator factories for all tokens, used for {@link GowerDistance}
     * generation.
     */
    private GowerTokenSimilarityIndexFactory<?>[] gowerTokenSimilarityIndexesFactories;

    /**
     * Weights used for {@link GowerDistance} generation.
     */
    private double[] weights;

    /**
     * Current data processing type.
     */
    private DistanceIncomingDataType initialDataProcessingType;

    /**
     * Current distance function type chosen to be created.
     */
    private DistanceFunctionType distanceFunctionType;

    /**
     * Distance function to be generated.
     */
    private DistanceFunction distanceFunction;

    /**
     * Default ctor.
     * 
     * @param tdsm
     */
    public DistanceFunctionDialog(TokenDataSourceAndMisc tdsm) {
        this(tdsm.getTokenDataSource(), tdsm.getTokenTypes(), tdsm.getTitles());
    }

    /**
     * Verbose ctor.
     * 
     * @param dataSource
     * @param tokenTypes
     * @param titles
     */
    public DistanceFunctionDialog(AbstractTokenDataSource<?> dataSource, TokenType[] tokenTypes,
            String[] titles) {
        // window size management
        this.setResizable(true);
        // set internal state
        this.dataSource = dataSource;
        this.tokenTypes = tokenTypes;
        // this.titles = titles;
        final int recordLength = dataSource.getRecordLength();
        inUseCounter = recordLength;
        inUse = new boolean[recordLength];
        weights = new double[recordLength];
        for (int i = 0; i < recordLength; i++) {
            if (TokenType.isSupportedTokenType(tokenTypes[i])) {
                inUse[i] = true;
            }
            else {
                inUseCounter--;
            }
            weights[i] = 1.;
        }
        gowerTokenSimilarityIndexesFactories =
                new GowerTokenSimilarityIndexFactory<?>[recordLength];
        initialDataProcessingType = DistanceIncomingDataType.detectInitialDataType(this);
        // visual components setup
        // getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        okButton = (Button) getDialogPane().lookupButton(GenericValueReturnDialog.OK_BUTTON_TYPE);
        okButton.setDisable(true);
        tableView = new TokenDataSourceTableView(dataSource, titles);
        tableView.reloadView();
        tableView.loadNext();
        setupToggleGroup();
        showFromBeginningButton.setOnAction(ev -> {
            tableView.reloadView();
            tableView.loadNext();
        });
        showNextButton.setOnAction(ev -> tableView.loadNext());
        statusLabel.setText(initialDataProcessingType.getStatusString());
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(25);
        gridPane.getColumnConstraints().add(col1);
        gridPane.add(statusLabel, 0, 0);
        gridPane.add(classicMetricRadioButton, 0, 1);
        gridPane.add(normalizedMetricRadioButton, 0, 2);
        gridPane.add(syntheticRadioButton, 0, 3);
        gridPane.add(categoricalRadioButton, 0, 4);
        gridPane.add(showFromBeginningButton, 1, 0);
        gridPane.add(showNextButton, 2, 0);
        gridPane.add(tableView, 1, 1, 3, 6);
        getDialogPane().setContent(gridPane);
        initialDataProcessingType.enablePossibleChoices(this);
    }

    /**
     * {@inheritDoc}<br/>
     * Simply returns generated value.
     */
    @Override
    protected DistanceFunction createResult() {
        return distanceFunction;
    }

    /**
     * {@inheritDoc}<br/>
     * Generates distance function.
     */
    @Override
    protected void validate() throws Exception {
        if (!hasUnusedTokens()) {
            distanceFunction = distanceFunctionType.createDistanceFunction(this, dataSource);
        }
        else {
            final int[] mask = produceMask();
            distanceFunction = new MaskedDistanceFunction(distanceFunctionType
                    .createDistanceFunction(this, dataSource),
                    mask);
        }
    }

    /**
     * Sets up all radio buttons for current window.
     */
    private void setupToggleGroup() {
        classicMetricRadioButton.setToggleGroup(group);
        classicMetricRadioButton.setUserData(DistanceFunctionType.CLASSIC_METRIC);
        classicMetricRadioButton.setDisable(true);
        normalizedMetricRadioButton.setToggleGroup(group);
        normalizedMetricRadioButton.setUserData(DistanceFunctionType.NORMALIZED_METRIC);
        normalizedMetricRadioButton.setDisable(true);
        syntheticRadioButton.setToggleGroup(group);
        syntheticRadioButton.setUserData(DistanceFunctionType.GOWER_DISTANCE);
        syntheticRadioButton.setDisable(true);
        categoricalRadioButton.setToggleGroup(group);
        categoricalRadioButton.setUserData(DistanceFunctionType.DISSIMILARITY_FUNCTION);
        categoricalRadioButton.setDisable(true);
        group.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
            if (old_toggle != new_toggle && new_toggle != null) {
                final DistanceFunctionType distanceFunctionType =
                        ((DistanceFunctionType) new_toggle.getUserData());
                clearVolatileUi();
                distanceFunctionType.setUi(this);
                this.distanceFunctionType = distanceFunctionType;
            }
        });
    }

    /**
     * Sets up panes for table column headers
     * 
     * @param tableView
     * @param nodes
     */
    private void setupTableHeaders(Pane[] nodes) {
        ObservableList<TableColumn<Record<Object[]>, ?>> columns = tableView.getColumns();
        for (int i = 1; i < columns.size(); i++) {
            columns.get(i).setGraphic(nodes[i - 1]);
            columns.get(i).setMinWidth(nodes[i - 1].getPrefWidth());
        }
    }

    /**
     * Cleans option-unique controls from grid pane and table view.
     */
    private void clearVolatileUi() {
        ObservableList<TableColumn<Record<Object[]>, ?>> columns = tableView.getColumns();
        for (int i = 1; i < columns.size(); i++) {
            columns.get(i).setGraphic(null);
        }
        gridPane.getChildren().remove(metricComboBox);
        gridPane.getChildren().remove(sifFactoryComboBox);
    }

    /**
     * Checks if there are any of unused tokens (tokens that were omitted from distance calculation
     * by user or program).
     * 
     * @return true, if there are unused tokens
     */
    private boolean hasUnusedTokens() {
        boolean hasUnusedTokens = false;
        for (boolean value : inUse) {
            if (!value) {
                hasUnusedTokens = true;
                break;
            }
        }
        return hasUnusedTokens;
    }

    /**
     * @return current mask for tokens in use.
     */
    private int[] produceMask() {
        int maskSize = 0;
        for (boolean value : inUse) {
            if (value) {
                maskSize++;
            }
        }
        int[] mask = new int[maskSize];
        int counter = 0;
        for (int i = 0; i < inUse.length; i++) {
            if (inUse[i]) {
                mask[counter] = i;
                counter++;
            }
        }
        return mask;
    }

    /**
     * Internal enum that defines types of possible
     */
    private enum DistanceIncomingDataType {
        UNSUPPORTED_DATA(
                "Incoming data is completely unsupported, you can not create distance function") {
            @Override
            public void enablePossibleChoices(DistanceFunctionDialog form) {
                form.classicMetricRadioButton.setDisable(true);
                form.normalizedMetricRadioButton.setDisable(true);
                form.syntheticRadioButton.setDisable(true);
                form.categoricalRadioButton.setDisable(true);
            }
        },
        PURE_NUMERIC_DATA(
                "Incoming data is purely numeric, therefore you can use either classic or normalized metrics") {
            @Override
            public void enablePossibleChoices(DistanceFunctionDialog form) {
                form.classicMetricRadioButton.setDisable(false);
                form.normalizedMetricRadioButton.setDisable(false);
                form.syntheticRadioButton.setDisable(false);
                form.categoricalRadioButton.setDisable(true);
            }
        },
        MIXED_DATA(
                "With mixed data you have a single option: use synthetic Gower's distance function") {
            @Override
            public void enablePossibleChoices(DistanceFunctionDialog form) {
                form.classicMetricRadioButton.setDisable(true);
                form.normalizedMetricRadioButton.setDisable(true);
                form.syntheticRadioButton.setDisable(false);
                form.categoricalRadioButton.setDisable(true);
            }
        },
        PURE_CATEGORICAL_DATA(
                "Incoming data is purely categorical, therefore use either dissimilarity function"
                        + " for pure categorical data or token by token define Gower's distance") {
            @Override
            public void enablePossibleChoices(DistanceFunctionDialog form) {
                form.classicMetricRadioButton.setDisable(true);
                form.normalizedMetricRadioButton.setDisable(true);
                form.syntheticRadioButton.setDisable(false);
                form.categoricalRadioButton.setDisable(false);
            }
        };

        private String statusString;

        DistanceIncomingDataType(String statusString) {
            this.statusString = statusString;
        }

        public String getStatusString() {
            return statusString;
        }

        /**
         * Enables right set of possible distance function choices mapped into radio buttons.
         * 
         * @param form
         */
        public abstract void enablePossibleChoices(DistanceFunctionDialog form);

        /**
         * Detects initial data type, for single use in cotor of the main form.
         * @param form main form
         * @return initial data type
         */
        public static DistanceIncomingDataType detectInitialDataType(DistanceFunctionDialog form) {
            List<TokenType> types = new ArrayList<>();
            for (int i = 0; i < form.inUse.length; i++) {
                if (TokenType.isSupportedTokenType(form.tokenTypes[i])) {
                    types.add(form.tokenTypes[i]);
                }
            }
            return types.size() == 0 ? DistanceIncomingDataType.UNSUPPORTED_DATA
                    : detectDataType(types.toArray(new TokenType[types.size()]));
        }

        private static DistanceIncomingDataType detectDataType(TokenType[] tokenTypes) {
            DistanceIncomingDataType type = singleTokenDataType(tokenTypes[0]);
            for (int i = 1; i < tokenTypes.length; i++) {
                if (type != singleTokenDataType(tokenTypes[i])) {
                    type = MIXED_DATA;
                    break;
                }
            }
            return type;
        }

        private static DistanceIncomingDataType singleTokenDataType(TokenType tokenType) {
            return tokenType == TokenType.INTEGER || tokenType == TokenType.FLOAT
                    ? PURE_NUMERIC_DATA : PURE_CATEGORICAL_DATA;
        }
    }

    /**
     * Maps chosen distance function type with certain ui controls to show.
     * 
     * @author atta_troll
     *
     */
    private enum DistanceFunctionType {
        CLASSIC_METRIC("Choose metric from combo box.") {

            @Override
            protected TokenSettingsView getTokenSettingsView(DistanceFunctionDialog form,
                    int index) {
                return form.new TokenSettingsView(index);
            }

            @Override
            public void setUi(DistanceFunctionDialog form) {
                super.setUi(form);
                form.gridPane.add(form.metricComboBox, 0, 6);
            }

            @Override
            public DistanceFunction createDistanceFunction(DistanceFunctionDialog form,
                    AbstractTokenDataSource<?> tokenDataSource)
                    throws IOException {
                return form.metricComboBox.getChosenMetric();
            }
        },
        NORMALIZED_METRIC("Choose metric from the combo box, metric will be normalized.") {

            @Override
            protected TokenSettingsView getTokenSettingsView(DistanceFunctionDialog form,
                    int index) {
                return form.new TokenSettingsView(index);
            }

            @Override
            public void setUi(DistanceFunctionDialog form) {
                CLASSIC_METRIC.setUi(form);
            }

            @Override
            public DistanceFunction createDistanceFunction(DistanceFunctionDialog form,
                    AbstractTokenDataSource<?> tokenDataSource)
                    throws IOException {
                return NormalizedMetric.getNormalizedMetric(form.dataSource,
                        form.metricComboBox.getChosenMetric());
            }

        },
        DISSIMILARITY_FUNCTION("Choose similarity coefficient from the list") {

            @Override
            protected TokenSettingsView getTokenSettingsView(DistanceFunctionDialog form,
                    int index) {
                return form.new TokenSettingsView(index);
            }

            @Override
            public void setUi(DistanceFunctionDialog form) {
                super.setUi(form);
                form.gridPane.add(form.sifFactoryComboBox, 0, 6);
            }

            @Override
            public DistanceFunction createDistanceFunction(DistanceFunctionDialog form,
                    AbstractTokenDataSource<?> tokenDataSource)
                    throws IOException {
                    return DissimilarityFunction.produceDissimilarityFunction(tokenDataSource,
                            form.sifFactoryComboBox.getValue());
            }

        },
        GOWER_DISTANCE("Chooser similarity indexes for each token to form Gower's distance") {

            @Override
            protected TokenSettingsView getTokenSettingsView(DistanceFunctionDialog form,
                    int index) {
                return form.new SyntheticTokenSettingsView(index);
            }

            @Override
            public DistanceFunction createDistanceFunction(DistanceFunctionDialog form,
                    AbstractTokenDataSource<?> tokenDataSource)
                    throws IOException {
                    return GowerDistance.produceGowerDistance(
                            form.gowerTokenSimilarityIndexesFactories, form.weights,
                            form.dataSource);
            }
        };

        private String statusString;

        DistanceFunctionType(String statusString) {
            this.statusString = statusString;
        }

        protected abstract TokenSettingsView getTokenSettingsView(DistanceFunctionDialog form,
                int index);

        public abstract DistanceFunction createDistanceFunction(DistanceFunctionDialog form,
                AbstractTokenDataSource<?> tokenDataSource)
                throws IOException;

        /**
         * Shows option-specific ui on the main form.
         * 
         * @param form
         *        main form
         */
        public void setUi(DistanceFunctionDialog form) {
            form.gridPane.getChildren().remove(form.metricComboBox);
            form.statusLabel.setText(statusString);
            form.okButton.setDisable(form.inUseCounter < 1);
            final int tokenNumber = form.dataSource.getRecordLength();
            TokenSettingsView[] views = new TokenSettingsView[tokenNumber];
            for (int i = 0; i < tokenNumber; i++) {
                views[i] = getTokenSettingsView(form, i);
            }
            form.setupTableHeaders(views);
        }
    }

    private class TokenSettingsView extends VBox {

        protected int index;

        public TokenSettingsView(int index) {
            this.index = index;
            CheckBox inUseCheckBox = new CheckBox("Check if current token is in use");
            inUseCheckBox.setSelected(DistanceFunctionDialog.this.inUse[index]);
            inUseCheckBox.selectedProperty().addListener((ov, old_val, new_val) -> {
                DistanceFunctionDialog.this.inUse[index] = new_val;
                if (new_val != old_val) {
                    if (new_val) {
                        inUseCounter++;
                    }
                    else {
                        inUseCounter--;
                        okButton.setDisable(inUseCounter < 1);
                    }
                }
            });
            if (!TokenType.isSupportedTokenType(DistanceFunctionDialog.this.tokenTypes[index])) {
                inUseCheckBox.setDisable(true);
                //inUseCheckBox.setSelected(false);
            }
            getChildren().addAll(new Label(tableView.getColumns().get(index + 1).getText()),
                    new Label(tokenTypes[index].toString()), inUseCheckBox);
            setPrefWidth(270); // TODO ugly, but i don't know how to do better
            // also see setupTableHeaders(Pane[]) line 4
        }
    }

    private class SyntheticTokenSettingsView extends TokenSettingsView {

        public SyntheticTokenSettingsView(int index) {
            super(index);
            ComboBox<GowerTokenSimilarityIndexFactory<?>> tdcComboBox = new ComboBox<>();
            if (TokenType.isSupportedTokenType(DistanceFunctionDialog.this.tokenTypes[index])) {
                final GowerTokenSimilarityIndexFactory<?>[] tdcFactories =
                        Registers.GOWER_TOKEN_SIMILARITY_INDEX_FACTORY_REGISTER
                                .get(tokenTypes[index]);
                tdcComboBox.getItems().addAll(tdcFactories);
                tdcComboBox.valueProperty().addListener((ov, old_val,
                        new_val) -> gowerTokenSimilarityIndexesFactories[index] = new_val);
                tdcComboBox.getSelectionModel().select(0);
                TextField weightTextField = new PositiveDoubleTextField();
                weightTextField.setText(Double.toString(weights[index]));
                weightTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {

                    private String oldText = weightTextField.getText();

                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable,
                            Boolean oldValue, Boolean newValue) {
                        if (!newValue && oldValue) {
                            double value = 0.;
                            try {
                                value = Double.parseDouble(weightTextField.getText());
                                weights[index] = value;
                                oldText = weightTextField.getText();
                            }
                            catch (NumberFormatException | NullPointerException ex) {
                                weightTextField.setText(oldText);
                            }
                        }
                    }
                });
                getChildren().addAll(new Label("Choose proper token diff calc:"), tdcComboBox,
                        new Label("Set weight for current token:"), weightTextField);

            }
        }
    }

    private class MetricComboBox extends ComboBox<Metric> {

        private Metric chosenMetric;

        public MetricComboBox() {
            super();
            getItems().addAll(Registers.SIMPLE_METRIC_REGISTER);
            getItems().add(PNorm.getPNorm(1));
            // ugly and breaks OOP rules but i don't want to implement another layer of
            // factories just for a single PNorm
            valueProperty().addListener((ov, oldValue, newValue) -> {
                if (newValue instanceof PNorm) {
                    Optional<Double> optional = (new PNormCoeffReturnDialog()).showAndWait();
                    if (optional.isPresent()) {
                        chosenMetric = PNorm.getPNorm(optional.get());
                    }
                    else {
                        if (!(oldValue instanceof PNorm)) {
                            getSelectionModel().select(oldValue);
                        }
                    }
                }
                else {
                    chosenMetric = newValue;
                }
            });
        }

        public Metric getChosenMetric() {
            return chosenMetric;
        }
    }

}
