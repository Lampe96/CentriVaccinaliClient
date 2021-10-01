package org.project.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;


import java.io.IOException;
import java.net.URL;

/**
 * JavaFX AppMain
 */
public class AppMain extends Application {

    private double xOffset, yOffset;

    public static void main(String[] args) {
        launch();
    }

    private static Parent loadFXML(URL fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(fxml);
        return fxmlLoader.load();
    }

    @Override
    public void start(@NotNull Stage stage) throws IOException {
        Scene scene = new Scene(loadFXML(AppMain.class.getResource("fxml/app_main.fxml")));
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        scene.setOnMousePressed(mouseEvent -> {
            xOffset = mouseEvent.getSceneX();
            yOffset = mouseEvent.getSceneY();
        });

        scene.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() - xOffset);
            stage.setY(mouseEvent.getScreenY() - yOffset);
        });
    }

}