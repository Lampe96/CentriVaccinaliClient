package org.project.hub;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class HubHomeAboutController implements Initializable {

    @FXML
    private AnchorPane AP_ext;

    @FXML
    private ImageView BT_quit;

    @FXML
    private Label BT_git_fede;

    @FXML
    private Label BT_git_enri;

    @FXML
    private Label BT_git_marc;

    @FXML
    private Label BT_git_gian;

    private Stage stage;
    private static final String[] LINKGIT = {"https://github.com/Fede22dev", "https://github.com/Lampe96",
            "https://github.com/Marcalexorlando", "https://github.com/GianlucaLa"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> stage = (Stage) AP_ext.getScene().getWindow());
    }

    @FXML
    private void quit() {
        stage.hide();
    }

    @FXML
    private void darkStyleQuit() {
        setDarkHover(BT_quit);
    }

    @FXML
    private void restoreStyleQuit() {
        resetDarkExit(BT_quit);
    }

    private void setDarkHover(@NotNull javafx.scene.image.ImageView iv) {
        iv.setEffect(new InnerShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, 0.5), 10, 0, 5, 5));
    }

    private void resetDarkExit(@NotNull javafx.scene.image.ImageView iv) {
        iv.setEffect(null);
    }

    @FXML
    void gitFede() {
        try {
            browse(LINKGIT[0]);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void gitEnri() {
        try {
            browse(LINKGIT[1]);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void gitMarc() {
        try {
            browse(LINKGIT[2]);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void gitGian() {
        try {
            browse(LINKGIT[3]);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void browse(String link) throws URISyntaxException {
        URI uri = new URI(link);
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

