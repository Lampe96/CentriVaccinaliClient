package org.project.hub;

import com.jfoenix.controls.JFXComboBox;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
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

/**
 * Questa classe gestisce tutti i componenti presenti nella
 * visualizzazione delle info dell'utente e relativo aggiornamento
 * della dose
 *
 * @author Federico Mainini 740691 (VA)
 * @author Gianluca Latronico 739893 (VA)
 * @author Marc Alexander Orlando 741473 (VA)
 * @author Enrico Luigi Lamperti 740612 (VA)
 */
public class HubHomeInfoUserController implements Initializable {

    /**
     * Array utilizzato per riempire {@link #CB_vaccine}
     */
    private static final String[] VACCINETYPE = {"Pfizer", "Moderna", "AstraZeneca", "J&J"};

    /**
     * AnchorPane esterno
     */
    @FXML
    private AnchorPane AP_ext;

    /**
     * Immagine che funge da quit dallo stage
     */
    @FXML
    private ImageView BT_quit;

    /**
     * Field contente il nome del vaccinato
     */
    @FXML
    private MFXTextField TF_name;

    /**
     * Field contente il cognome del vaccinato
     */
    @FXML
    private MFXTextField TF_surname;

    /**
     * Field contente il codice fiscale del vaccinato
     */
    @FXML
    private MFXTextField TF_fiscal_code;

    /**
     * Field contente la data di vaccinazione
     */
    @FXML
    private MFXTextField TF_date;

    /**
     * ComboBox contente le diverse tipologie di vaccino
     */
    @FXML
    private JFXComboBox<String> CB_vaccine;

    /**
     * Field contente il nome del centro vaccinale
     * in cui viene vaccinato il cittadino
     */
    @FXML
    private MFXTextField TF_name_hub;

    /**
     * AnchorPane utilizzato per inserire un tooltip
     * sul bottone {@link #BT_update_vaccinated}
     */
    @FXML
    private AnchorPane AP_tooltip_btn;

    /**
     * Bottone per l'aggiornamento della dose somministrata
     */
    @FXML
    private MFXButton BT_update_vaccinated;

    /**
     * Stage riferito a questo controller
     */
    private Stage stage;

    /**
     * Informazioni dell'utente che sto visualizzando
     */
    private User vu;

    /**
     * Utilizzato per settare l'utente di cui si vuole vizualizzare
     * le informazioni
     *
     * @param vu User
     */
    void setVaccinatedUserInfo(User vu) {
        this.vu = vu;
    }

    /**
     * Serve per inizializzare l'interfaccia riempiendo
     * varie label tra cui: nome, cognome, codice fiscale, data,
     * tipologia di vaccino e nome hub.
     * Serve inoltre per riempire la combo box con le diverse tipologie
     *
     * @param url            url
     * @param resourceBundle resourceBundle
     */
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
                CB_vaccine.setDisable(true);
                TF_name_hub.setDisable(true);
                BT_update_vaccinated.setDisable(true);

                Tooltip tool = new Tooltip("Questo cittadino ha completato\nil ciclo vaccinale");
                tool.setShowDelay(new Duration(500));
                Tooltip.install(AP_tooltip_btn, tool);
            }
        });

        CB_vaccine.getItems().addAll(VACCINETYPE);
    }

    /**
     * Quando premuto, il tasto exit chiude lo stage
     */
    @FXML
    private void quit() {
        stage.close();
    }

    /**
     * Utilizzato per scurire l'icona quit
     * quando il cursore entra
     */
    @FXML
    private void darkStyleQuit() {
        setDarkHover(BT_quit);
    }

    /**
     * Utilizzato per riportare l'immagine alla normalità
     * una volta uscito il cursore
     */
    @FXML
    private void restoreStyleQuit() {
        resetDarkExit(BT_quit);
    }

    /**
     * Utilizzato da certe immagini per scurire l'interno
     *
     * @param iv ImageView che si vuole scurire
     */
    private void setDarkHover(@NotNull ImageView iv) {
        iv.setEffect(new InnerShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, 0.5), 10, 0, 5, 5));
    }

    /**
     * Utilizzato da certe immagini per portare alla normalità
     * l'effetto interno di scurimento
     *
     * @param iv ImageView che si vuole portare alla normalità
     */
    private void resetDarkExit(@NotNull ImageView iv) {
        iv.setEffect(null);
    }

    /**
     * Legato al bottone {@link #BT_update_vaccinated} che
     * effettua l'aggiornamento, quando consentito, dell'user settando
     * la dose ricevuta a 2
     */
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

    /**
     * Utilizzato per far apparire un pop-up di errore
     * nel caso in cui modificando le informazioni si
     * inserisca qualcosa di non valido
     */
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
