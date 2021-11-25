package org.project.hub;

import com.jfoenix.controls.JFXComboBox;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.project.UserType;
import org.project.login.LoginMainController;
import org.project.models.User;
import org.project.server.ServerReference;
import org.project.utils.RegistrationUtil;
import org.project.utils.UIdGenerator;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class HubHomeRegistrationNewVaccinatedController implements Initializable {

    private static final String[] VACCINETYPE = {"Pfizer", "Moderna", "AstraZeneca", "J&J"};
    private final User vaccinatedUser = new User();

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
    private ImageView IV_calculator_fiscal_code;

    @FXML
    private JFXComboBox<String> CB_vaccine;

    @FXML
    private MFXTextField TF_date;

    @FXML
    private MFXButton BT_sing_up_new_vaccinated;

    @FXML
    private MFXProgressSpinner PS_spinner;

    private Stage stage;
    private String hubName;

    void setHubName(String hubName) {
        this.hubName = hubName;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        Platform.runLater(() -> stage = (Stage) AP_ext.getScene().getWindow());
        TF_date.setText(LocalDate.now().format(formatter));
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
        stage.close();
    }

    @FXML
    private void restoreStyleCalculator() {
        resetDarkExit(IV_calculator_fiscal_code);
    }

    @FXML
    private void registerNewVaccinated() {
        String name = StringUtils.capitalize(TF_name.getText().strip());
        String surname = StringUtils.capitalize(TF_surname.getText().strip());
        String fiscalCode = TF_fiscal_code.getText().strip().toUpperCase(Locale.ROOT);
        String vaccineType = CB_vaccine.getValue();
        if (RegistrationUtil.checkName(name) && RegistrationUtil.checkName(surname) && RegistrationUtil.checkFiscalCode(fiscalCode) && vaccineType != null) {
            try {
                if (ServerReference.getServer().checkIfFirstDose(fiscalCode) == 0) {
                    vaccinatedUser.setId(UIdGenerator.generateUId(fiscalCode));
                    vaccinatedUser.setName(name);
                    vaccinatedUser.setSurname(surname);
                    vaccinatedUser.setFiscalCode(fiscalCode);
                    vaccinatedUser.setHubName(hubName);
                    vaccinatedUser.setVaccineDate(Date.valueOf(LocalDate.now()));
                    vaccinatedUser.setVaccineType(vaccineType);
                    vaccinatedUser.setDose((short) 1);
                    ServerReference.getServer().insertNewVaccinated(vaccinatedUser);
                    stage.close();
                } else {
                    errorAlert(1);
                }
            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
            }
        } else {
            errorAlert(2);
        }
    }

    private void errorAlert(int typeError) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Errore");
        if (typeError == 1) {
            alert.setContentText("Questo utente ha già ricevuto la prima dose.");
        } else {
            alert.setContentText("Uno o più campi non sono compilati correttamente.");
        }

        alert.initStyle(StageStyle.TRANSPARENT);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);
        ButtonType buttonTypeCancel = new ButtonType("Annulla", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeCancel);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setPrefSize(300, 0);
        dialogPane.lookupButton(buttonTypeCancel).setId("btnCancel");
        dialogPane.getStylesheets().add(Objects.requireNonNull(LoginMainController.class.getResource("alert_choice.css")).toExternalForm());
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

        alert.showAndWait();
    }
}
