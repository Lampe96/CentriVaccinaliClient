package org.project.user;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.project.login.LoginMainController;
import org.project.windowUtility.WindowUtility;

import java.io.IOException;

public class UserSignUpController {

    @FXML
    public MFXButton BT_sing_up;

    @FXML
    public AnchorPane AP_ext;

    @FXML
    public ImageView BT_minimize;

    @FXML
    public ImageView BT_quit;

    @FXML
    public MFXPasswordField PF_password;

    @FXML
    public MFXPasswordField PF_confirm_pwd;

    @FXML
    public MFXTextField TF_name;

    @FXML
    public MFXTextField TF_nickname;

    @FXML
    public MFXTextField TF_surname;

    @FXML
    public MFXTextField TF_fiscal_code;

    @FXML
    public MFXTextField TF_email;

    @FXML
    public ImageView BT_back;

    public void sign_up(MouseEvent mouseEvent) {
    }

    public void back() {
        try {
            WindowUtility.setRoot(LoginMainController.class.getResource("fxml/login.fxml"), AP_ext.getScene());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void darkStyleBack() {setDarkHover(BT_back); }

    public void restoreStyleBack() {resetDarkExit(BT_back);}

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

}
