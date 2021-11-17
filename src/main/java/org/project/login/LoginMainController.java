package org.project.login;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
import org.project.hub.HubHomeController;
import org.project.hub.HubSignUpController;
import org.project.server.ServerReference;
import org.project.user.UserHomeController;
import org.project.user.UserSignUpController;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class LoginMainController implements Initializable {

    @FXML
    private AnchorPane AP_ext;

    @FXML
    private ImageView BT_minimize;

    @FXML
    private ImageView BT_quit;

    @FXML
    private MFXTextField TF_email;

    @FXML
    private Label LB_error_email;

    @FXML
    private MFXPasswordField PF_password;

    @FXML
    private Label LB_error_password;

    @FXML
    private MFXButton BT_login;

    @FXML
    private Label BT_login_guest;

    @FXML
    private Label BT_signUp;

    private Stage stage;
    private Scene scene;
    private double xPos = 0;
    private double yPos = 0;
    private double xOffset, yOffset;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startUpLocation(AP_ext.getPrefWidth(), AP_ext.getPrefHeight());
        Platform.runLater(() -> {
            stage = (Stage) AP_ext.getScene().getWindow();
            scene = AP_ext.getScene();
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

        TF_email.textProperty().addListener((observable, oldValue, newValue) -> {
            LB_error_email.setVisible(false);
            LB_error_password.setVisible(false);
        });

        PF_password.textProperty().addListener((observable, oldValue, newValue) -> {
            LB_error_email.setVisible(false);
            LB_error_password.setVisible(false);
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
    private void pressEnter(@NotNull KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            login();
        }
    }

    @FXML
    private void login() {
        String email = TF_email.getText().strip();
        String pwd = PF_password.getPassword().strip();

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

        UserType userHub = null;
        try {
            userHub = ServerReference.getServer().checkCredential(email, pwd);
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }

        if (userHub == null) {
            LB_error_email.setVisible(true);
            LB_error_password.setVisible(true);
        } else if (userHub == UserType.HUB) {
            try {
                startRightHomeStage(UserType.HUB);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                startRightHomeStage(UserType.USER);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void startRightHomeStage(UserType userType) throws IOException {
        Scene scene;
        if (userType == UserType.HUB) {
            FXMLLoader loader = new FXMLLoader(HubHomeController.class.getResource("fxml/hub_home.fxml"));
            Parent root = loader.load();
            HubHomeController hubHomeController = loader.getController();
            hubHomeController.setNameHub(TF_email.getText().strip());
            scene = new Scene(root);
        } else {
            FXMLLoader loader = new FXMLLoader(UserHomeController.class.getResource("fxml/user_home.fxml"));
            Parent root = loader.load();
            scene = new Scene(root);
        }

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        stage.setResizable(false);
        stage.setTitle("CVI");
        stage.getIcons().add(new Image(Objects.requireNonNull(UserType.class.getResourceAsStream("drawable/primula.png"))));
        this.stage.hide();
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

    @NotNull
    private String getPathRememberMe() {
        return System.getProperty("user.dir") + File.separator + "Remember_me";
    }

    @FXML
    private void loginGuest() {
        try {
            scene.setRoot(FXMLLoader.load(Objects.requireNonNull(GuestHomeController.class.getResource("fxml/guest_home.fxml"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void signUp() {
        UserType userType = choiceAlert();
        if (userType == UserType.HUB) {
            try {
                scene.setRoot(FXMLLoader.load(Objects.requireNonNull(HubSignUpController.class.getResource("fxml/hub_sign_up.fxml"))));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (userType == UserType.USER) {
            try {
                scene.setRoot(FXMLLoader.load(Objects.requireNonNull(UserSignUpController.class.getResource("fxml/user_sign_up.fxml"))));
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
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("alert_choice.css")).toExternalForm());
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
