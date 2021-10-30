package org.project.hub;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.project.guest.GuestHomeController;
import org.project.utils.WindowUtil;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HubHomeController implements Initializable {

    @FXML
    public AnchorPane AP_ext;

    @FXML
    public ImageView BT_minimize;

    @FXML
    public ImageView BT_quit;

    @FXML
    public MFXButton BT_setting;

    @FXML
    public MFXButton BT_logout;

    @FXML
    public MFXButton BT_about;

    private Stage stage;
    private double xPos = 0;
    private double yPos = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startUpLocation(AP_ext.getPrefWidth(), AP_ext.getPrefHeight());
        Platform.runLater(() -> {
            stage = (Stage) AP_ext.getScene().getWindow();
            if (xPos == 0.0 && yPos == 0.0) {
                stage.centerOnScreen();
            } else {
                stage.setX(xPos);
                stage.setY(yPos);
            }
        });
    }

    private void startUpLocation(double windowWidth, double windowHeight) {
        Point p = MouseInfo.getPointerInfo().getLocation();
        List<Screen> screens = Screen.getScreens();
        if (p != null && screens != null && screens.size() > 1) {
            Rectangle2D screenBounds;
            for (Screen screen : screens) {
                screenBounds = screen.getVisualBounds();
                if (screenBounds.contains(p.x, p.y)) {
                    xPos = screenBounds.getMinX() + ((screenBounds.getMaxX() - screenBounds.getMinX() - windowWidth) / 2);
                    yPos = screenBounds.getMinY() + ((screenBounds.getMaxY() - screenBounds.getMinY() - windowHeight) / 2);
                }
            }
        }
    }

    @FXML
    private void minimize() {
        stage.setIconified(true);
    }

    @FXML
    private void darkStyleMinimize() {
        setDarkHover(BT_minimize);
    }

    @FXML
    private void restoreStyleMinimize() {
        resetDarkExit(BT_minimize);
    }

    @FXML
    private void quit() {
        System.exit(0);
    }

    @FXML
    private void darkStyleQuit() {
        setDarkHover(BT_quit);
    }

    @FXML
    private void restoreStyleQuit() {
        resetDarkExit(BT_quit);
    }

    private void setDarkHover(@NotNull ImageView iv) {
        iv.setEffect(new InnerShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, 0.5), 10, 0, 5, 5));
    }

    private void resetDarkExit(@NotNull ImageView iv) {
        iv.setEffect(null);
    }

    @FXML
    public void openSetting() {
        try {
            startSetting();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startSetting() throws IOException {
        Scene scene = new Scene(WindowUtil.newScene(GuestHomeController.class.getResource("fxml/guest_home.fxml")));
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("My modal window");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(this.stage);
        stage.show();
    }

    @FXML
    public void logout() {

    }

    @FXML
    public void openAbout() {

    }
}
