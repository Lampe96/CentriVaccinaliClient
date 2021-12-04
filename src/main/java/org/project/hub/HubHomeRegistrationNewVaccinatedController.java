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

/**
 * Questa classe gestisce tutti i componenti presenti nella
 * fase di creazione di un nuovo  cittadino vaccinato, quindi
 * attivabile dal bottone presente nella classe
 * {@link HubHomeController}
 *
 * @author Federico Mainini 740691 (VA)
 * @author Gianluca Latronico 739893 (VA)
 * @author Marc Alexander Orlando 741473 (VA)
 * @author Enrico Luigi Lamperti 740612 (VA)
 */
public class HubHomeRegistrationNewVaccinatedController implements Initializable {

    /**
     * Array utilizzato per riempire {@link #CB_vaccine}
     */
    private static final String[] VACCINETYPE = {"Pfizer", "Moderna", "AstraZeneca", "J&J"};
    /**
     * sUser vaccinato
     */
    private final User vaccinatedUser = new User();

    /**
     * Anchor pane esterno
     */
    @FXML
    private AnchorPane AP_ext;

    /**
     * Immagine che funge da quit dall'applicazione
     */
    @FXML
    private ImageView BT_quit;

    /**
     * TextField per indicare il nome del vaccinato
     */
    @FXML
    private MFXTextField TF_name;

    /**
     * TextField per indicare il cognome del vaccinato
     */
    @FXML
    private MFXTextField TF_surname;

    /**
     * TextField per indicare il codice fiscale del vaccinato
     */
    @FXML
    private MFXTextField TF_fiscal_code;

    /**
     * Immagine per aprire un collegamento internet per calcolare
     * il codice fiscale
     */
    @FXML
    private ImageView IV_calculator_fiscal_code;

    /**
     * ComboBox per selezionare la tipologi del vaccino
     */
    @FXML
    private JFXComboBox<String> CB_vaccine;

    /**
     * TextField per indicare la data della somministrazione
     * del vaccinato
     */
    @FXML
    private MFXTextField TF_date;

    /**
     * Bottone per registrare il nuovo vaccinato
     */
    @FXML
    private MFXButton BT_sing_up_new_vaccinated;

    /**
     * Stage riferito a questa classe
     */
    private Stage stage;

    /**
     * Nome del centro vaccinale
     */
    private String hubName;

    /**
     * Utilizzato per settare il nome del centro vaccinale
     *
     * @param hubName nome centro vacinale
     */
    void setHubName(String hubName) {
        this.hubName = hubName;
    }

    /**
     * Utilizzato per inizializzare l'interfaccia
     * prendendo la scena
     *
     * @param url            url
     * @param resourceBundle resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> stage = (Stage) AP_ext.getScene().getWindow());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        TF_date.setText(LocalDate.now().format(formatter));

        CB_vaccine.getItems().addAll(VACCINETYPE);

        BT_sing_up_new_vaccinated.requestFocus();

        Tooltip tool = new Tooltip("Calcola codice fiscale");
        tool.setShowDelay(new Duration(500));
        Tooltip.install(IV_calculator_fiscal_code, tool);
    }

    /**
     * Utilizzato per richiamare il metodo {@link #browseCalcFiscalCode()}
     * che porta al calcolatore del codice fiscale online
     */
    @FXML
    private void calculateWebFiscalCode() {
        try {
            browseCalcFiscalCode();
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Apre connessione alla pagina del calcolatore del codice fiscale sul web
     *
     * @throws URISyntaxException URISyntaxException
     * @throws IOException        IOException
     */
    private void browseCalcFiscalCode() throws URISyntaxException, IOException {
        URI uri = new URI("https://www.codicefiscaleonline.com/");
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            desktop.browse(uri);
        }
    }

    /**
     * Utilizzato per scurire l'icona della calcolatrice
     * quando il cursore entra
     */
    @FXML
    private void darkStyleCalculator() {
        setDarkHover(IV_calculator_fiscal_code);
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
     * Quando premuto, il tasto exit chiude lo stage
     */
    @FXML
    private void quit() {
        stage.close();
    }

    /**
     * Utilizzato per riportare alla normalità l'effeto applicato
     * precedentemente all'immagine della calcolatrice
     */
    @FXML
    private void restoreStyleCalculator() {
        resetDarkExit(IV_calculator_fiscal_code);
    }

    /**
     * Utilizzato per gestire la registrazione di nuovi vaccinati, viene effettuato
     * un controllo per verificare la correttezza dei campi inseriti (se sono vuoti o meno).
     * Se il controllo viene superato si verifica, tramite chiamata al server, se il
     * cittadino non ha ancora ricevuto la prima dose.
     *
     * @see org.project.utils.RegistrationUtil#checkName(String)
     * @see org.project.utils.RegistrationUtil#checkFiscalCode(String)
     * @see org.project.server.Server#checkIfFirstDose(String)
     */
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

    /**
     * Alert per controllare eventuali errori occorsi in fase
     * di digitazione o per verificare che l'utente sia alla sua
     * prima dose.
     *
     * @param typeError intero per identificare il tipo di errore verificatosi
     */
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
