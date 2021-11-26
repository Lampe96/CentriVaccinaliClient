package org.project.user;

import com.password4j.Password;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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
import org.project.UserType;
import org.project.server.ServerReference;
import org.project.utils.RegistrationUtil;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;

public class UserHomeSettingsController implements Initializable {

    private final Timer TIMER = new Timer();

    @FXML
    public AnchorPane AP_ext;
    boolean openDelete = false;
    @FXML
    private ImageView BT_quit;
    @FXML
    private ImageView IV_one;
    @FXML
    private ImageView IV_two;
    @FXML
    private ImageView IV_three;
    @FXML
    private ImageView IV_four;
    @FXML
    private ImageView IV_five;
    @FXML
    private ImageView IV_six;
    @FXML
    private ImageView IV_seven;
    @FXML
    private ImageView IV_eight;
    @FXML
    private ImageView IV_nine;
    @FXML
    private MFXButton BT_delete;
    @FXML
    private MFXPasswordField TF_old_password;
    @FXML
    private Label LB_error_password;
    @FXML
    private Label LB_success_change;
    @FXML
    private MFXPasswordField TF_new_password;
    @FXML
    private MFXPasswordField TF_confirm_password;
    @FXML
    private MFXButton BT_confirm_old_pwd;
    @FXML
    private MFXButton BT_confirm_new_pwd;
    private Stage stage;
    private int selectedImage;
    private String email;
    private UserHomeSettingsDeleteController userHomeSettingsDeleteController;

    void setEmail(String email) {
        this.email = email;
    }

    boolean getDeleteAccSettings() {
        if (openDelete) {
            return userHomeSettingsDeleteController.getDeleteAccPopUp();
        }
        return false;
    }

    private void setDarkStyle() {
        switch (selectedImage) {
            case 1:
                setDarkHover(IV_one);
                break;
            case 2:
                setDarkHover(IV_two);
                break;
            case 3:
                setDarkHover(IV_three);
                break;
            case 4:
                setDarkHover(IV_four);
                break;
            case 5:
                setDarkHover(IV_five);
                break;
            case 6:
                setDarkHover(IV_six);
                break;
            case 7:
                setDarkHover(IV_seven);
                break;
            case 8:
                setDarkHover(IV_eight);
                break;
            case 9:
                setDarkHover(IV_nine);
                break;
        }
    }

    int getSelectedImage() {
        return selectedImage;
    }

    void setSelectedImage(int selectedImage) {
        this.selectedImage = selectedImage;
        setDarkStyle();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> stage = (Stage) AP_ext.getScene().getWindow());
    }

    @FXML
    private void confirm_new_password() {
        String newPwd = TF_new_password.getPassword().strip();
        String confirmNewPwd = TF_confirm_password.getPassword().strip();
        String oldPwd = TF_old_password.getPassword().strip();

        if (RegistrationUtil.checkPassword(newPwd)) {
            if (RegistrationUtil.checkPasswordConfirmed(newPwd, confirmNewPwd)) {
                if (RegistrationUtil.checkChangePwd(newPwd, oldPwd)) {
                    String cryptPwd = Password.hash(newPwd).addRandomSalt().withArgon2().getResult();
                    try {
                        ServerReference.getServer().changePwd("", email, cryptPwd);
                        LB_error_password.setVisible(false);
                        TF_new_password.setVisible(false);
                        BT_confirm_new_pwd.setVisible(false);
                        TF_confirm_password.setVisible(false);
                        LB_success_change.setVisible(true);

                        TIMER.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                TF_old_password.setVisible(true);
                                BT_confirm_old_pwd.setVisible(true);
                                LB_success_change.setVisible(false);
                                //TODO settare vuoti text pwd
                            }
                        }, 5 * 1000);
                    } catch (RemoteException | NotBoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    LB_error_password.setText("La nuova password è uguale alla precedente");
                    LB_error_password.setVisible(true);
                }
            } else {
                LB_error_password.setText("Le password non coincidono");
                LB_error_password.setVisible(true);
            }
        } else {
            LB_error_password.setText("Minimo 6 caratteri, 1 speciale, 1 minuscolo, 1 maiuscolo, 1 numero");
            LB_error_password.setVisible(true);
        }
    }

    @FXML
    private void confirm_old_password() {
        String pwd = TF_old_password.getPassword().strip();
        try {
            boolean checkPwd = ServerReference.getServer().checkPassword("", email, pwd);
            if (checkPwd) {
                TF_old_password.setVisible(false);
                LB_error_password.setVisible(false);
                TF_new_password.setVisible(true);
                BT_confirm_old_pwd.setVisible(false);
                BT_confirm_new_pwd.setVisible(true);
                TF_confirm_password.setVisible(true);
            } else {
                LB_error_password.setText("Password non corretta");
                BT_confirm_old_pwd.setVisible(true);
                BT_confirm_new_pwd.setVisible(false);
                LB_error_password.setVisible(true);
            }
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void delete() {
        try {
            openDeletePopUp();
            openDelete = true;
            if (userHomeSettingsDeleteController.getDeleteAccPopUp()) {
                stage.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openDeletePopUp() throws IOException {
        FXMLLoader loader = new FXMLLoader(UserHomeSettingsDeleteController.class.getResource("fxml/user_settings_delete.fxml"));
        Parent root = loader.load();
        userHomeSettingsDeleteController = loader.getController();
        userHomeSettingsDeleteController.setEmail(email);

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        stage.setResizable(false);
        stage.setTitle("Cancella account");
        stage.getIcons().add(new Image(Objects.requireNonNull(UserType.class.getResourceAsStream("drawable/primula.png"))));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(this.stage);

        AtomicReference<Double> xOffset = new AtomicReference<>((double) 0);
        AtomicReference<Double> yOffset = new AtomicReference<>((double) 0);

        scene.setOnMousePressed(mouseEvent -> {
            xOffset.set(mouseEvent.getSceneX());
            yOffset.set(mouseEvent.getSceneY());
        });

        scene.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() - xOffset.get());
            stage.setY(mouseEvent.getScreenY() - yOffset.get());
        });
        stage.showAndWait();
    }

    @FXML
    private void quit() {
        TIMER.cancel();
        stage.close();
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
    private void darkStyleIVOne() {
        setDarkHover(IV_one);
    }

    @FXML
    private void restoreStyleIVOne() {
        if (selectedImage != 1) {
            resetDarkExit(IV_one);
        }
    }

    @FXML
    private void selectImageOne() {
        if (selectedImage != 1) {
            resetStyle();
            setDarkHover(IV_one);
            selectedImage = 1;
        }
    }

    @FXML
    private void darkStyleIVTwo() {
        setDarkHover(IV_two);
    }

    @FXML
    private void restoreStyleIVTwo() {
        if (selectedImage != 2) {
            resetDarkExit(IV_two);
        }
    }

    @FXML
    private void selectImageTwo() {
        if (selectedImage != 2) {
            resetStyle();
            setDarkHover(IV_two);
            selectedImage = 2;
        }
    }

    @FXML
    private void darkStyleIVThree() {
        setDarkHover(IV_three);
    }

    @FXML
    private void restoreStyleIVThree() {
        if (selectedImage != 3) {
            resetDarkExit(IV_three);
        }
    }

    @FXML
    private void selectImageThree() {
        if (selectedImage != 3) {
            resetStyle();
            setDarkHover(IV_three);
            selectedImage = 3;
        }
    }

    @FXML
    private void darkStyleIVFour() {
        setDarkHover(IV_four);
    }

    @FXML
    private void restoreStyleIVFour() {
        if (selectedImage != 4) {
            resetDarkExit(IV_four);
        }
    }

    @FXML
    private void selectImageFour() {
        if (selectedImage != 4) {
            resetStyle();
            setDarkHover(IV_four);
            selectedImage = 4;
        }
    }

    @FXML
    private void darkStyleIVFive() {
        setDarkHover(IV_five);
    }

    @FXML
    private void restoreStyleIVFive() {
        if (selectedImage != 5) {
            resetDarkExit(IV_five);
        }
    }

    @FXML
    private void selectImageFive() {
        if (selectedImage != 5) {
            resetStyle();
            setDarkHover(IV_five);
            selectedImage = 5;
        }
    }

    @FXML
    private void darkStyleIVSix() {
        setDarkHover(IV_six);
    }

    @FXML
    private void restoreStyleIVSix() {
        if (selectedImage != 6) {
            resetDarkExit(IV_six);
        }
    }

    @FXML
    private void selectImageSix() {
        if (selectedImage != 6) {
            resetStyle();
            setDarkHover(IV_six);
            selectedImage = 6;
        }
    }

    @FXML
    private void darkStyleIVSeven() {
        setDarkHover(IV_seven);
    }

    @FXML
    private void restoreStyleIVSeven() {
        if (selectedImage != 7) {
            resetDarkExit(IV_seven);
        }
    }

    @FXML
    private void selectImageSeven() {
        if (selectedImage != 7) {
            resetStyle();
            setDarkHover(IV_seven);
            selectedImage = 7;
        }
    }

    @FXML
    private void darkStyleIVEight() {
        setDarkHover(IV_eight);
    }

    @FXML
    private void restoreStyleIVEight() {
        if (selectedImage != 8) {
            resetDarkExit(IV_eight);
        }
    }

    @FXML
    private void selectImageEight() {
        if (selectedImage != 8) {
            resetStyle();
            setDarkHover(IV_eight);
            selectedImage = 8;
        }
    }

    @FXML
    private void darkStyleIVNine() {
        setDarkHover(IV_nine);
    }

    @FXML
    private void restoreStyleIVNine() {
        if (selectedImage != 9) {
            resetDarkExit(IV_nine);
        }
    }

    @FXML
    private void selectImageNine() {
        if (selectedImage != 9) {
            resetStyle();
            setDarkHover(IV_nine);
            selectedImage = 9;
        }
    }

    private void resetStyle() {
        switch (selectedImage) {
            case 1:
                resetDarkExit(IV_one);
                break;
            case 2:
                resetDarkExit(IV_two);
                break;
            case 3:
                resetDarkExit(IV_three);
                break;
            case 4:
                resetDarkExit(IV_four);
                break;
            case 5:
                resetDarkExit(IV_five);
                break;
            case 6:
                resetDarkExit(IV_six);
                break;
            case 7:
                resetDarkExit(IV_seven);
                break;
            case 8:
                resetDarkExit(IV_eight);
                break;
            case 9:
                resetDarkExit(IV_nine);
                break;
        }
    }
}
