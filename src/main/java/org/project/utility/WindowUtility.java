package org.project.utility;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;

public class WindowUtility {

    public static Parent newScene(URL fxml) throws IOException {
        return loadFXML(fxml);
    }

    public static void setRoot(URL fxml, @NotNull Scene scene) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(URL fxml) throws IOException {
        return FXMLLoader.load(fxml);
    }
}
