package org.project.hub;

import com.jfoenix.controls.JFXComboBox;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.project.models.VaccinatedUser;
import org.project.server.ServerReference;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.Date;
import java.text.DateFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class HubHomeInfoUserController implements Initializable {

    private static final String[] VACCINETYPE = {"Pfizer", "Moderna", "AstraZeneca", "J&J"};

    @FXML
    private AnchorPane AP_ext;

    @FXML
    private ImageView BT_quit;

    @FXML
    private MFXTextField TF_name;

    @FXML
    private MFXTextField TF_surname;

    @FXML
    private MFXTextField TF_fiscal_code;

    @FXML
    private MFXTextField TF_date;

    @FXML
    private JFXComboBox<String> CB_vaccine;

    @FXML
    private MFXTextField TF_name_hub;

    @FXML
    private MFXButton BT_update_vaccinated;

    @FXML
    private MFXProgressSpinner PS_spinner;

    private Stage stage;
    private VaccinatedUser vu;

    void setVaccinatedUserInfo(VaccinatedUser vu) {
        this.vu = vu;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DateFormat formatter = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALY);

        Platform.runLater(() -> {
            stage = (Stage) AP_ext.getScene().getWindow();
            TF_name.setText(vu.getName());
            TF_surname.setText(vu.getSurname());
            TF_fiscal_code.setText(vu.getFiscalCode());
            TF_date.setText(formatter.format(vu.getVaccineDate()));
            CB_vaccine.setValue(vu.getVaccineType());
            TF_name_hub.setText(vu.getHubName());
        });

        CB_vaccine.getItems().addAll(VACCINETYPE);
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
    private void updateVaccinated() {
        //todo if data giusta
        //Date date = TF_date.getText();
        String type = CB_vaccine.getValue();
        String nameHub = TF_name_hub.getText().strip();
        //todo id univoco
        try {
            ServerReference.getServer().updateVaccinatedUser((short) 55, nameHub, type, new Date(System.currentTimeMillis()), vu.getFiscalCode());
            stage.close();
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }
}