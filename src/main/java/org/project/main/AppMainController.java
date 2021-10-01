package org.project.main;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;
import org.project.guest.GuestHomeController;
import org.project.homeUser.UserHomeController;
import org.project.hub.HubHomeController;
import org.project.signUp.SignUpController;
import org.project.windowUtility.WindowUtility;

import java.io.IOException;
import java.util.Optional;

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
    public Label BT_login_guest;

    @FXML
    public Label BT_signUp;

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

    private void setDarkHover(@NotNull ImageView iv) {
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

    private void resetDarkExit(@NotNull ImageView iv) {
        iv.setEffect(null);
    }

    @FXML
    private void login() {
        //controllare nel database se hub o cittadino
        try {
            if (1 == 0) {
                WindowUtility.setRoot(UserHomeController.class.getResource("fxml/user_home.fxml"), AP_ext.getScene());
            } else {
                WindowUtility.setRoot(HubHomeController.class.getResource("fxml/hub_home.fxml"), AP_ext.getScene());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void loginGuest() {
        try {
            WindowUtility.setRoot(GuestHomeController.class.getResource("fxml/guest_home.fxml"), AP_ext.getScene());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void signUp() {

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Confirmation Dialog with Custom Actions");
        alert.setHeaderText("Look, a Confirmation Dialog with Custom Actions");
        alert.initStyle(StageStyle.UTILITY);

        ButtonType buttonTypeOne = new ButtonType("hub");
        ButtonType buttonTypeTwo = new ButtonType("Two");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == buttonTypeOne) {
            try {
                WindowUtility.setRoot(SignUpController.class.getResource("fxml/sign_up.fxml"), AP_ext.getScene());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                WindowUtility.setRoot(SignUpController.class.getResource("fxml/sign_up.fxml"), AP_ext.getScene());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
