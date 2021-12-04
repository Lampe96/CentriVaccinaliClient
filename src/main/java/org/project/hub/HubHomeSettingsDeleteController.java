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

/**
 * Questa classe gestisce tutti i componenti presenti nel pop-up
 * che gestisce l'eliminazione dell'account da DB. eseguibile dal bottone presente
 * nella classe {@link HubHomeSettingsController}
 *
 * @author Federico Mainini 740691 (VA)
 * @author Gianluca Latronico 739893 (VA)
 * @author Marc Alexander Orlando 741473 (VA)
 * @author Enrico Luigi Lamperti 740612 (VA)
 */
public class HubHomeSettingsDeleteController implements Initializable {

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
     * Field per la password
     */
    @FXML
    private MFXPasswordField PF_pwd;

    /**
     * Label di errore per la password
     */
    @FXML
    private Label LB_error_pwd;

    /**
     * Bottone per confermare la delete
     */
    @FXML
    private MFXButton BT_confirmed;

    /**
     * Stage riferito a questo controller
     */
    private Stage stage;

    /**
     * Nome del centro vaccinale
     */
    private String hubName;

    /**
     * boolean per tener traccia dell'eliminazione
     * dell'account
     */
    private boolean deleteAcc = false;

    /**
     * Utilizzao per settare il nome
     * di centro vaccinale
     *
     * @param hubName nome centro vaccinale
     */
    void setHubName(String hubName) {
        this.hubName = hubName;
    }

    /**
     * Utilizzato per settare il boolean per la
     * verifica dell'eliminazione dell'account
     *
     * @return true se viene eliminato, false nel caso contrario
     */
    boolean getDeleteAccPopUp() {
        return deleteAcc;
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
     * Se viene premuto il tasto {@link #BT_confirmed}
     * viene verificata la password tramite l'apposito metodo e,
     * se corretta, viene eliminato dal DB l'account
     *
     * @see org.project.server.Server#checkPassword(String, String, String)
     */
    @FXML
    private void confirmed() {
        String pwd = PF_pwd.getPassword().strip();
        try {
            if ((ServerReference.getServer().checkPassword(hubName, "", pwd))) {
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

    /**
     * Utilizzato per scurire l'icona quit
     * quando il cursore entra
     */
    @FXML
    private void darkStyleQuit() {
        setDarkHover(BT_quit);
    }

    /**
     * Quando premuto, il tasto exit chiude lo stage
     */
    @FXML
    private void quit() {
        deleteAcc = false;
        stage.close();
    }

    /**
     * Utilizzato per riportare l'immagine alla normalità
     * una volta uscito il cursore
     */
    @FXML
    private void restoreStyleQuit() {
        resetDarkExit(BT_quit);
    }
}
