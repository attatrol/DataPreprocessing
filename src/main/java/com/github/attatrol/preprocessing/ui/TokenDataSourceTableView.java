
package com.github.attatrol.preprocessing.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.attatrol.preprocessing.datasource.AbstractTokenDataSource;
import com.github.attatrol.preprocessing.datasource.Record;
import com.github.attatrol.preprocessing.ui.misc.UiUtils;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Shows tokens from data source in a table.
 * 
 * @author atta_troll
 *
 */
public class TokenDataSourceTableView extends TableView<Record<Object[]>> {

    /**
     * Default number of records shown simultaneously.
     */
    public static final int DEFAULT_SHOWN_RECORD_NUMBER = 1000;

    /**
     * Title for index column.
     */
    private static final String INDEX_COLUMN_HEADER = "Index";

    /**
     * Generic name for an unnamed token column.
     */
    private static final String STUB_COLUMN_HEADER = "Token #%d";

    /**
     * Data source used to fill table
     */
    private final AbstractTokenDataSource<?> tokenDataSource;

    /**
     * Default ctor.
     * @param dataSourceAndMisc token data source with some additional data
     */
    public TokenDataSourceTableView(TokenDataSourceAndMisc dataSourceAndMisc) {
        this(dataSourceAndMisc.getTokenDataSource(), dataSourceAndMisc.getTitles());
    }

    /**
     * Verbose ctor.
     * @param tokenDataSource token data source
     * @param titles titles for each column, may be null
     */
    public TokenDataSourceTableView(AbstractTokenDataSource<?> tokenDataSource, String[] titles) {
        this.tokenDataSource = tokenDataSource;
        final int tokenNumber = tokenDataSource.getRecordLength();
        List<TableColumn<Record<Object[]>, ?>> columns = new ArrayList<>(tokenNumber + 1);
        TableColumn<Record<Object[]>, Long> indexColumn = new TableColumn<>(INDEX_COLUMN_HEADER);
        indexColumn.setMinWidth(50);
        indexColumn.setCellValueFactory(new PropertyValueFactory<Record<Object[]>, Long>("index"));
        columns.add(indexColumn);
        for (int i = 0; i < tokenNumber; i++) {
            final String columnTitle;
            if (titles != null && titles.length > i && titles[i] != null) {
                columnTitle = titles[i];
            }
            else {
                columnTitle = getStubColumnName(i);
            }
            int finalI = i;
            TableColumn<Record<Object[]>, Object> tokenColumn = new TableColumn<>(columnTitle);
            tokenColumn.setMinWidth(100);
            tokenColumn.setCellValueFactory(
                    p -> new ReadOnlyObjectWrapper<Object>(p.getValue().getData()[finalI]));
            columns.add(tokenColumn);
        }
        this.getColumns().addAll(columns);
    }

    /**
     * Resets data source and cleans the table.
     */
    public void reloadView() {
        try {
            tokenDataSource.reset();
            getItems().clear();
        }
        catch (IOException ex) {
            UiUtils.showExceptionMessage(ex);
        }
    }

    /**
     * Cleans the table, then loads next {@link #DEFAULT_SHOWN_RECORD_NUMBER} of records into table.
     */
    public void loadNext() {
        loadNext(DEFAULT_SHOWN_RECORD_NUMBER);
    }

    /**
     * Cleans the table, then loads next records into table.
     * @param numberOfRecords number of records to be loaded
     */
    public void loadNext(int numberOfRecords) {
        getItems().clear();
        int i = 0;
        try {
            while (i++ < numberOfRecords && tokenDataSource.hasNext()) {
                getItems().add(tokenDataSource.next());
            }
        }
        catch (IOException ex) {
            UiUtils.showExceptionMessage(ex);
        }
    }

    /**
     * Generates stub column name for some column.
     * @param index index of a column
     * @return stub column name
     */
    public static String getStubColumnName(int index) {
        return String.format(STUB_COLUMN_HEADER, index);
    }

}
