package com.github.attatrol.preprocessing.ui.uimodel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

/**
 * Preview external source pane for
 * {@link ExternalSourceTypeRegister#SINGLE_FILE}
 * and {@link ExternalSourceTypeRegister#DIRECTORY}.
 * @author atta_troll
 *
 */
class PreviewFilePane extends VBox {

	public static final int LINE_LIMIT = 100;

	public PreviewFilePane(Label label, TextArea textBox) {
		super();
		textBox.prefHeightProperty().bind(heightProperty());
		getChildren().addAll(label, textBox);
	}

	public static PreviewFilePane createPreviewFilePane(File file) throws IOException {
		Label label = new Label();
		TextArea textBox =new TextArea();
		List<String> lines = new ArrayList<>();
		if (file.isDirectory()) {
			label.setText("Chosen file is a directory");
			getAllFiles(file, lines);
		}
		else {
			label.setText(String.format("Content of a chosen file (first %d strings):", LINE_LIMIT));
			getLines(file, lines);
		}
		lines.forEach(line -> {
		    textBox.appendText(line);
		    textBox.appendText(System.getProperty("line.separator"));});
		textBox.positionCaret(0);
		return new PreviewFilePane(label, textBox);
	}

	private static void getAllFiles(File directory, List<String> fileNames) {
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				getAllFiles(file, fileNames);
			}
			else {
				fileNames.add(file.getAbsolutePath().toString());
			}
		}
	}

    private static void getLines(File file, List<String> lines)
            throws IOException, FileNotFoundException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            int counter = 0;
            String line;
            while (counter++ < LINE_LIMIT && (line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
    }
}
