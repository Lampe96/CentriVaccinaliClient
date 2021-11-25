package org.project.user;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.project.models.User;
import org.project.server.ServerReference;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;


public class UserAlreadyVaccinatedController implements Initializable {

    @FXML
    private AnchorPane AP_ext;

    @FXML
    private ImageView BT_quit;

    @FXML
    private MFXTextField TF_fiscal_code;

    @FXML
    private MFXTextField TF_hub_name;

    @FXML
    private MFXButton BT_confirmed;

    @FXML
    private Label LB_error_fiscal_code;

    @FXML
    private Label LB_error_hub_name;

    private Stage stage;
    private User vu;

    User getVaccinatedUser() {
        return vu;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> stage = (Stage) AP_ext.getScene().getWindow());
    }

    @FXML
    private void quit() {
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
    private void confirmed(MouseEvent event) {
        String fiscalCode = TF_fiscal_code.getText().strip().toUpperCase(Locale.ROOT);
        String hubName = TF_hub_name.getText().strip();

        try {
            //todo MODIFICARE STO CAZZO DI OBJVACCINERED

            Object[] objVaccinated = Arrays.copyOf(ServerReference.getServer().checkIfUserIsVaccinated(hubName, fiscalCode),2);
            System.out.println(Arrays.toString(objVaccinated));
            if (objVaccinated[0].equals(1)) {
                System.out.println("AAAAAAAAAAAAAAAAA");
                vu = (User) objVaccinated[1];
                vu.setFiscalCode(fiscalCode);
                stage.close();
            } else if (objVaccinated[0].equals(2)) {
                LB_error_hub_name.setVisible(true);
            } else {
                LB_error_fiscal_code.setVisible(true);
            }
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }
}
