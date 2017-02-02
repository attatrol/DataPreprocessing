package com.github.attatrol.preprocessing.ui;

import java.util.Optional;

import com.github.attatrol.preprocessing.datasource.AbstractTokenDataSource;
import com.github.attatrol.preprocessing.distance.DistanceFunction;
import com.github.attatrol.preprocessing.ui.misc.UiUtils;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class TestApplication extends Application {

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new TestScene());
        primaryStage.show();
        primaryStage.setTitle("Test");
    }

    /**
     * Entry point
     * @param strings not in use
     */
    public static final void main(String...strings) {
        launch(strings);
    }

    private static class TestScene extends Scene {

        TestScene() {
            super(new HBox());
            HBox hBox = (HBox) getRoot();
            Button callDataSourceDialogButton = new Button("Call data source dialog");
            callDataSourceDialogButton.setOnAction(ev-> {
                Optional<TokenDataSourceAndMisc> result =
                        (new TokenDataSourceDialog()).showAndWait();
                if (result.isPresent()) {
                    AbstractTokenDataSource<?> dataSource = result.get().getTokenDataSource();
                    UiUtils.showInfoMessage(String.format("Hey, we have a data source\n%s", dataSource));
                    Optional<DistanceFunction> result1 = (new DistanceFunctionDialog(result.get())).showAndWait();
                    if (result1.isPresent()) {
                        UiUtils.showInfoMessage(String.format("Hey, we have a distance function\n%s", result1.get()));
                    }
                    else {
                        UiUtils.showInfoMessage("Oh no, got no distance function from dialog!");
                    }
                }
                else {
                    UiUtils.showInfoMessage("Oh no, got no data source from dialog!");
                }
            });
            hBox.getChildren().addAll(callDataSourceDialogButton);
        }
    }

}
