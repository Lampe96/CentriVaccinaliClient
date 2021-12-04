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

/**
 * Questa classe gestisce il pop-up che viene aperto
 * in fase di registrazione, viene chiesto al cittadino se
 * ha gia' ricevuto o meno una dose di vaccino. In caso
 * affermativo viene chiesto di inserire il codice fiscale
 * e il centro vaccinale presso cui e' stato vaccinato e,
 * in caso di controllo eseguito con successo, vengono
 * riempiti i campi della schermata {@link UserSignUpController}
 *
 * @author Federico Mainini 740691 (VA)
 * @author Gianluca Latronico 739893 (VA)
 * @author Marc Alexander Orlando 741473 (VA)
 * @author Enrico Luigi Lamperti 740612 (VA)
 */
public class UserAlreadyVaccinatedController implements Initializable {

    /**
     * AnchorPane esterno
     */
    @FXML
    private AnchorPane AP_ext;

    /**
     * Immagine che funge da quit dall'applicazione
     */
    @FXML
    private ImageView BT_quit;

    /**
     * Field per l'inserimento del codice fiscale
     */
    @FXML
    private MFXTextField TF_fiscal_code;

    /**
     * Field per l'inserimento del nome del centro vaccinale
     */
    @FXML
    private MFXTextField TF_hub_name;

    /**
     * Bottone per la conferma
     */
    @FXML
    private MFXButton BT_confirmed;

    /**
     * Label di errore per il codice fiscale
     */
    @FXML
    private Label LB_error_fiscal_code;

    /**
     * Label di errore per il nome
     * del centro vaccinale
     */
    @FXML
    private Label LB_error_hub_name;

    /**
     * Stage riferito a questo controller
     */
    private Stage stage;

    /**
     * Dati necessari nella classe
     */
    private User vu;

    /**
     * Utilizzato per settare l'utente vaccinato
     */
    User getVaccinatedUser() {
        return vu;
    }

    /**
     * Utilizzato per spostare il focus iniziale sul bottone di conferma
     *
     * @param url            url
     * @param resourceBundle resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            stage = (Stage) AP_ext.getScene().getWindow();
            BT_confirmed.requestFocus();
        });
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
     * Azione collegata al bottone {@link #BT_confirmed}, vengono
     * controllati i parametri inseriti nei field tramite un metodo remoto.
     * se il controllo termina con successo vengono compilati alcuni campi della
     * registrazione
     *
     * @param event event
     * @see org.project.server.Server#checkIfUserIsVaccinated(String, String) 
     */
    @FXML
    private void confirmed(MouseEvent event) {
        String fiscalCode = TF_fiscal_code.getText().strip().toUpperCase(Locale.ROOT);
        String hubName = TF_hub_name.getText().strip();

        try {
            Object[] objVaccinated = Arrays.copyOf(ServerReference.getServer().checkIfUserIsVaccinated(hubName, fiscalCode), 2);

            if (objVaccinated[0].equals(1)) {
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
