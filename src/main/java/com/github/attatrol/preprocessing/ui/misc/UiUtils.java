package com.github.attatrol.preprocessing.ui.misc;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.github.attatrol.preprocessing.ui.i18n.UiI18nProvider;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * Ui utility methods
 * @author atta_troll
 *
 */
public final class UiUtils {

	private UiUtils() { }

	/**
	 * Creates a grid pane of a defined visual design
	 * @return grid pane distance
	 */
	public static GridPane getGridPane() {
		GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        return grid;
	}

    /**
     * Shows full exception message with a stack trace to user.
     * @param exception exception
     */
    public static void showExceptionMessage(Exception exception) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(UiI18nProvider.INSTANCE.getValue("ui.utils.alert.error.title"));
        alert.setHeaderText(UiI18nProvider.INSTANCE.getValue("ui.utils.alert.error.info"));
        alert.setContentText(exception.getLocalizedMessage());

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        TextArea textArea = new TextArea(sw.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);
        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);

        Label label = new Label(UiI18nProvider
                .INSTANCE.getValue("ui.utils.alert.error.stacktrace"));
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);
        alert.getDialogPane().setExpandableContent(expContent);
        alert.showAndWait();
    }
   
    /**
     * Shows some info message to user.
     * @param message message
     */
    public static void showInfoMessage(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(UiI18nProvider.INSTANCE.getValue("ui.utils.alert.info.title"));
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Shows some error message to user.
     * @param message message
     */
    public static void showErrorMessage(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(UiI18nProvider.INSTANCE.getValue("ui.utils.alert.error.title"));
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
