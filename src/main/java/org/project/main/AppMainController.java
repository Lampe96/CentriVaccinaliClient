package org.project.main;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class AppMainController {

    @FXML
    public ImageView BT_minimize;

    @FXML
    public ImageView BT_quit;

    @FXML
    public AnchorPane AP_ext;

    @FXML
    public MFXButton BT_login;

    @FXML
    public Label BT_signUp;

    @FXML
    public Label BT_login_guest;

    @FXML
    private void minimize() {
        Stage stage = (Stage) AP_ext.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void quit() {
        System.exit(0);
    }

    @FXML
    private void darkStyleMinimize() {
        setDarkHover(BT_minimize);
    }

    @FXML
    private void darkStyleQuit() {
        setDarkHover(BT_quit);
    }

    private void setDarkHover(ImageView iv) {
        iv.setEffect(new InnerShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, 0.5), 10, 0, 5, 5));
    }

    @FXML
    private void restoreStyleMinimize() {
        resetDarkExit(BT_minimize);
    }

    @FXML
    private void restoreStyleQuit() {
        resetDarkExit(BT_quit);
    }

    private void resetDarkExit(ImageView iv) {
        iv.setEffect(null);
    }

    @FXML
    public void login(MouseEvent mouseEvent) {
    }

    @FXML
    public void signUp(MouseEvent mouseEvent) {
    }

    @FXML
    public void loginGuest(MouseEvent mouseEvent) {
    }
}
