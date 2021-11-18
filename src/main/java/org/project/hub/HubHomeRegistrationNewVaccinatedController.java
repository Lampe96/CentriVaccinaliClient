package org.project.hub;

import com.jfoenix.controls.JFXComboBox;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import io.github.palexdev.materialfx.controls.MFXTextField;
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

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class HubHomeRegistrationNewVaccinatedController implements Initializable {

    private static final String[] VACCINETYPE = {"Pfizer", "Moderna", "AstraZeneca", "J&J"};

    @FXML
    private AnchorPane AP_ext;

    @FXML
    private ImageView BT_quit;

    @FXML
    private MFXTextField TF_name;

    @FXML
    private Label LB_error_name;

    @FXML
    private MFXTextField TF_surname;

    @FXML
    private Label LB_error_surname;

    @FXML
    private MFXTextField TF_fiscal_code;

    @FXML
    private ImageView IV_calculator_fiscal_code;

    @FXML
    private Label LB_error_code;

    @FXML
    private MFXDatePicker DP_dateDosingVaccine;

    @FXML
    private Label LB_error_email;

    @FXML
    private JFXComboBox<String> CB_vaccine;

    @FXML
    private Label LB_error_nickname;

    @FXML
    private MFXButton BT_sing_up;

    @FXML
    private MFXProgressSpinner PS_spinner;

    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            stage = (Stage) AP_ext.getScene().getWindow();
        });
        CB_vaccine.getItems().addAll(VACCINETYPE);
    }

    @FXML
    private void calculateWebFiscalCode() {
        try {
            browseCalcFiscalCode();
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    private void browseCalcFiscalCode() throws URISyntaxException, IOException {
        URI uri = new URI("https://www.codicefiscaleonline.com/");
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            desktop.browse(uri);
        }
    }

    @FXML
    private void darkStyleCalculator() {
        setDarkHover(IV_calculator_fiscal_code);
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
    private void quit() {
        stage.hide();
    }

    @FXML
    private void restoreStyleCalculator() {
        resetDarkExit(IV_calculator_fiscal_code);
    }

    @FXML
    private void signUp() {

    }

}
