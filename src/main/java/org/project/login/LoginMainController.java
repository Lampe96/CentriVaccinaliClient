package org.project.login;

import com.jfoenix.controls.JFXCheckBox;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.project.UserType;
import org.project.guest.GuestHomeController;
import org.project.hub.HubHomeController;
import org.project.hub.HubSignUpController;
import org.project.user.UserHomeController;
import org.project.user.UserSignUpController;
import org.project.windowUtility.WindowUtility;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class LoginMainController {

    @FXML
    public AnchorPane AP_ext;

    @FXML
    public ImageView BT_minimize;

    @FXML
    public ImageView BT_quit;

    @FXML
    public MFXTextField TF_email;

    @FXML
    public MFXPasswordField TF_password;

    @FXML
    public JFXCheckBox CB_remember_me;

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
        System.out.println(TF_email.getText());
        //controllare nel database se hub o cittadino
        if (CB_remember_me.isSelected()) {
            System.out.println(TF_password.getPassword());
            TF_password.getPassword();
        }

        /*try {
            if (1 == 0) {
                WindowUtility.setRoot(UserHomeController.class.getResource("fxml/user_home.fxml"), AP_ext.getScene());
            } else {
                WindowUtility.setRoot(HubHomeController.class.getResource("fxml/hub_home.fxml"), AP_ext.getScene());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
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
        UserType userType = choiceAlert();
        if (userType == UserType.HUB) {
            try {
                WindowUtility.setRoot(HubSignUpController.class.getResource("fxml/hub_sign_up.fxml"), AP_ext.getScene());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (userType == UserType.USER) {
            try {
                WindowUtility.setRoot(UserSignUpController.class.getResource("fxml/user_sign_up.fxml"), AP_ext.getScene());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Nullable
    private UserType choiceAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registrazione");
        alert.setHeaderText("Scegli come registrarti:");
        alert.initStyle(StageStyle.TRANSPARENT);
        alert.initModality(Modality.APPLICATION_MODAL);

        ButtonType buttonTypeHub = new ButtonType("Hub");
        ButtonType buttonTypeUser = new ButtonType("Cittadino");
        ButtonType buttonTypeCancel = new ButtonType("Annulla", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeHub, buttonTypeUser, buttonTypeCancel);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setPrefSize(0, 0);
        dialogPane.lookupButton(buttonTypeCancel).setId("btnCancel");
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("alert.css")).toExternalForm());
        dialogPane.getStyleClass().add("alert");

        Scene dialogScene = dialogPane.getScene();
        dialogScene.setFill(Color.TRANSPARENT);

        AtomicReference<Double> xOffset = new AtomicReference<>((double) 0);
        AtomicReference<Double> yOffset = new AtomicReference<>((double) 0);

        dialogScene.setOnMousePressed(mouseEvent -> {
            xOffset.set(mouseEvent.getSceneX());
            yOffset.set(mouseEvent.getSceneY());
        });

        dialogScene.setOnMouseDragged(mouseEvent -> {
            dialogScene.getWindow().setX(mouseEvent.getScreenX() - xOffset.get());
            dialogScene.getWindow().setY(mouseEvent.getScreenY() - yOffset.get());
        });

        Stage dialogStage = (Stage) dialogScene.getWindow();
        dialogStage.getIcons().add(new Image(Objects.requireNonNull(UserType.class.getResourceAsStream("drawable/primula.png"))));

        Optional<ButtonType> result = alert.showAndWait();

        if (result.orElse(null) == buttonTypeHub) {
            return UserType.HUB;
        } else if (result.orElse(null) == buttonTypeUser) {
            return UserType.USER;
        } else {
            return null;
        }
    }
}
