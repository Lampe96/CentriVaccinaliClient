package org.project.user;

import com.jfoenix.controls.JFXTextArea;
import io.github.palexdev.materialfx.controls.MFXLabel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.controlsfx.control.Rating;
import org.jetbrains.annotations.NotNull;
import org.project.models.AdverseEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Questa classe gestisce tutti i componenti presenti nella schermata
 * di visualizzazione degli eventi avversi segnalati presso quel centro.
 * Eseguibile selezionando una riga presente in {@link UserHomeInfoHubController}
 *
 * @author Federico Mainini 740691 (VA)
 * @author Gianluca Latronico 739893 (VA)
 * @author Marc Alexander Orlando 741473 (VA)
 * @author Enrico Luigi Lamperti 740612 (VA)
 */
public class UserHomeShowAdverseEventController implements Initializable {

    /**
     * AnchorPane esterno
     */
    @FXML
    private AnchorPane AP_ext;

    /**
     * Immagine che funge da quit dal programma
     */
    @FXML
    private ImageView BT_quit;

    /**
     * Field con le stelle da 1 a 5
     */
    @FXML
    private Rating R_severity;

    /**
     * Area di testo in cui visualizzare la nota
     */
    @FXML
    private JFXTextArea TA_text;

    /**
     * Label con il tipo dell'evento avveros
     */
    @FXML
    private MFXLabel LB_type;

    /**
     * Stage riferito a questo controller
     */
    private Stage stage;

    /**
     * Uvento avverso
     */
    private AdverseEvent ae;

    /**
     * Utilizzato per settare l'evento avverso in questo controller
     * 
     * @param ae evento avverso
     */
    void setData(AdverseEvent ae) {
        this.ae = ae;
    }

    /**
     * Utilizzato per inizializzare l'interfaccia
     * prendendo la scena e impostando i componenti
     * presenti nella schermata, tra cui {@link #LB_type}
     * {@link #R_severity} e {@link #TA_text}
     *
     * @param url            url
     * @param resourceBundle resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            stage = (Stage) AP_ext.getScene().getWindow();
            LB_type.setText(ae.getEventType());
            R_severity.setRating(ae.getGravity());
            TA_text.setText(ae.getText());
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
}
