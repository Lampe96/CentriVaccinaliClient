package org.project.hub;

import com.jfoenix.controls.JFXComboBox;
import io.github.palexdev.materialfx.controls.MFXButton;
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
import org.project.user.UserHomeController;
import org.project.windowUtility.WindowUtility;

import java.io.IOException;

public class HubSignUpController {

    @FXML
    public MFXButton BT_sing_up;

    @FXML
    public ImageView BT_minimize;

    @FXML
    public ImageView BT_quit;

    @FXML
    public MFXTextField TF_name_hub;

    @FXML
    public MFXTextField TF_address;

    @FXML
    public JFXComboBox CB_typology;

    @FXML
    public ImageView BT_back;

    @FXML
    public AnchorPane AP_ext;

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

    @FXML
    public void sign_up() {

    }
}
