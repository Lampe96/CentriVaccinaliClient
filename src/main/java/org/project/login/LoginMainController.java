package org.project.login;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.project.UserType;
import org.project.guest.GuestHomeController;
import org.project.hub.HubSignUpController;
import org.project.user.UserSignUpController;
import org.project.utils.WindowUtil;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class LoginMainController implements Initializable {

    @FXML
    public AnchorPane AP_ext;

    @FXML
    public ImageView BT_minimize;

    @FXML
    public ImageView BT_quit;

    @FXML
    public MFXTextField TF_email;

    @FXML
    public Label LB_error_email;

    @FXML
    public MFXPasswordField PF_password;

    @FXML
    public Label LB_error_password;

    @FXML
    public MFXButton BT_login;

    @FXML
    public Label BT_login_guest;

    @FXML
    public Label BT_signUp;

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

        File rememberMe = new File(getPathRememberMe());
        if (rememberMe.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(rememberMe));
                TF_email.setText(br.readLine());
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
    private void login() {
        String email = TF_email.getText().strip();
        String pwd = PF_password.getPassword().strip();

        System.out.println(email + " ///// " + pwd);

        LB_error_email.setVisible(true);
        LB_error_password.setVisible(true);

        //TODO controllare nel database se hub o cittadino e poi fare login

        //if (Db.existAccount(email, pwd)) {
        File rememberMe = new File(getPathRememberMe());
        if (!rememberMe.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                rememberMe.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(rememberMe));
            bw.write(email);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //}

        /*try {
            if (1 == 0) {
                WindowUtil.setRoot(UserHomeController.class.getResource("fxml/user_home.fxml"), AP_ext.getScene());
            } else {
                WindowUtil.setRoot(HubHomeController.class.getResource("fxml/hub_home.fxml"), AP_ext.getScene());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    @NotNull
    private String getPathRememberMe() {
        return System.getProperty("user.dir") + File.separator + "Remember_me";
    }

    @FXML
    private void loginGuest() {
        try {
            WindowUtil.setRoot(GuestHomeController.class.getResource("fxml/guest_home.fxml"), AP_ext.getScene());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void signUp() {
        UserType userType = choiceAlert();
        if (userType == UserType.HUB) {
            try {
                WindowUtil.setRoot(HubSignUpController.class.getResource("fxml/hub_sign_up.fxml"), AP_ext.getScene());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (userType == UserType.USER) {
            try {
                WindowUtil.setRoot(UserSignUpController.class.getResource("fxml/user_sign_up.fxml"), AP_ext.getScene());
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
        alert.initOwner(stage);

        ButtonType buttonTypeHub = new ButtonType("Hub");
        ButtonType buttonTypeUser = new ButtonType("Cittadino");
        ButtonType buttonTypeCancel = new ButtonType("Annulla", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeHub, buttonTypeUser, buttonTypeCancel);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setMaxSize(2, 2);
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
