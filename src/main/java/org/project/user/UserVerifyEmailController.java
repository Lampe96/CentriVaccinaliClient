package org.project.user;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

public class UserVerifyEmailController implements Initializable {

    @FXML
    public AnchorPane AP_ext;

    @FXML
    public ImageView BT_quit;

    @FXML
    public TextField TF_one;

    @FXML
    public TextField TF_two;

    @FXML
    public TextField TF_three;

    @FXML
    public TextField TF_four;

    @FXML
    public TextField TF_five;

    @FXML
    public TextField TF_six;

    @FXML
    public MFXButton BT_confirmed;

    @FXML
    public Label BT_new_code;

    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> stage = (Stage) AP_ext.getScene().getWindow());
    }

    @FXML
    private void quit() {
        stage.hide();
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
    public void confirmed() {
        int code = Integer.parseInt(TF_one.getText() + TF_two.getText() + TF_three.getText() + TF_four.getText() + TF_five.getText() + TF_six.getText());
        try {
            boolean verified = ServerReference.getServer().verifyEmail(TempUser.getEmail(), code);
            if (verified){
                stage.hide();
            }else{
                //rimanda codice
            }
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void newCode() {

    }
}
