package org.project.hub;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
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
import org.project.server.ServerReference;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class HubHomeSettingsDeleteController implements Initializable {

    @FXML
    private AnchorPane AP_ext;

    @FXML
    private ImageView BT_quit;

    @FXML
    private MFXPasswordField PF_pwd;

    @FXML
    private Label LB_error_pwd;

    @FXML
    private MFXButton BT_confirmed;

    private Stage stage;
    private String hubName;
    private boolean deleteAcc = false;

    void setHubName(String hubName) {
        this.hubName = hubName;
    }

    boolean getDeleteAccPopUp() {
        return deleteAcc;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> stage = (Stage) AP_ext.getScene().getWindow());
    }

    private void setDarkHover(@NotNull ImageView iv) {
        iv.setEffect(new InnerShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, 0.5), 10, 0, 5, 5));
    }

    private void resetDarkExit(@NotNull ImageView iv) {
        iv.setEffect(null);
    }

    @FXML
    void confirmed() {
        String pwd = PF_pwd.getPassword().strip();
        try {
            if ((ServerReference.getServer().checkPasswordHub(hubName, pwd))) {
                LB_error_pwd.setVisible(false);
                deleteAcc = true;
                stage.close();
            } else {
                LB_error_pwd.setVisible(true);
            }
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void darkStyleQuit() {
        setDarkHover(BT_quit);
    }

    @FXML
    void quit() {
        deleteAcc = false;
        stage.close();
    }

    @FXML
    void restoreStyleQuit() {
        resetDarkExit(BT_quit);
    }
}
