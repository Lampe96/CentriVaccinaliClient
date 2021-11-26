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
import org.jetbrains.annotations.NotNull;
import org.project.UserType;
import org.project.login.LoginMainController;
import org.project.models.User;
import org.project.server.ServerReference;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

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
    private User vu;

    void setVaccinatedUserInfo(User vu) {
        this.vu = vu;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        Platform.runLater(() -> {
            stage = (Stage) AP_ext.getScene().getWindow();

            TF_name.setText(vu.getName());
            TF_surname.setText(vu.getSurname());
            TF_fiscal_code.setText(vu.getFiscalCode());
            TF_date.setText(vu.getVaccineDate().toLocalDate().format(formatter));
            CB_vaccine.setValue(vu.getVaccineType());
            TF_name_hub.setText(vu.getHubName());

            if (vu.getDose() > 1) {
                CB_vaccine.disableProperty().setValue(true);
                TF_name_hub.disableProperty().setValue(true);
                BT_update_vaccinated.disableProperty().setValue(true);
            }
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
        String type = CB_vaccine.getValue();
        String hubName = TF_name_hub.getText().strip();
        String name = TF_name.getText().strip();
        String surname = TF_surname.getText().strip();

        vu.setDose((short) 2);
        vu.setVaccineDate(new Date(System.currentTimeMillis()));
        vu.setVaccineType(type);
        if (hubName.equals(vu.getHubName())) {
            try {
                vu.setHubName(hubName);
                ServerReference.getServer().updateVaccinatedUser(vu);
                stage.close();
            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
            }
        } else {
            try {
                if (ServerReference.getServer().checkIfHubExist(hubName)) {
                    vu.setHubName(hubName);
                    vu.setName(name);
                    vu.setSurname(surname);
                    ServerReference.getServer().insertVaccinatedUserInNewHub(vu);
                    stage.close();
                } else {
                    errorAlert();
                }
            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void errorAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Errore");
        alert.setContentText("Questo centro vaccinale non esiste.");

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