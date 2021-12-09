package org.project.shared;

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
import org.project.hub.HubHomeController;
import org.project.user.UserHomeController;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Questa classe gestisce tutti i componenti presenti nella
 * schermata about, condivisa da {@link HubHomeController} e
 * {@link UserHomeController}
 *
 * @author Federico Mainini 740691 (VA)
 * @author Gianluca Latronico 739893 (VA)
 * @author Marc Alexander Orlando 741473 (VA)
 * @author Enrico Luigi Lamperti 740612 (VA)
 */
public class AboutController implements Initializable {

    /**
     * Array utilizzato per aprire la pagina git
     * corretta
     */
    private static final String[] LINKGIT = {
            "https://github.com/Fede22dev",
            "https://github.com/Lampe96",
            "https://github.com/Marcalexorlando",
            "https://github.com/GianlucaLa"
    };

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
     * Label utilizzata per il collegamento web
     * al github di Federico Mainini
     */
    @FXML
    private Label BT_git_fede;

    /**
     * Label utilizzata per il collegamento web
     * al github di Enrico Luigi Lamperti
     */
    @FXML
    private Label BT_git_enri;

    /**
     * Label utilizzata per il collegamento web
     * al github di Marc Alexander Orlando
     */
    @FXML
    private Label BT_git_marc;

    /**
     * Label utilizzata per il collegamento web
     * al github di Gianluca Latronico
     */
    @FXML
    private Label BT_git_gian;

    /**
     * Stage riferito a questo controller
     */
    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> stage = (Stage) AP_ext.getScene().getWindow());
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
     * Avviato quando premuto {@link #BT_git_fede},
     * porta alla pagina git di Federico Mainini 740691 (VA)
     */
    @FXML
    private void gitFede() {
        try {
            browse(LINKGIT[0]);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Avviato quando premuto {@link #BT_git_enri},
     * porta alla pagina git di Enrico Luigi Lamperti 740612 (VA)
     */
    @FXML
    private void gitEnri() {
        try {
            browse(LINKGIT[1]);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Avviato quando premuto {@link #BT_git_marc},
     * porta alla pagina git di @author Marc Alex Orlando 741473 (VA)
     */
    @FXML
    private void gitMarc() {
        try {
            browse(LINKGIT[2]);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Avviato quando premuto {@link #BT_git_gian},
     * porta alla pagina git di Gianluca Latronico 739893 (VA)
     */
    @FXML
    private void gitGian() {
        try {
            browse(LINKGIT[3]);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo utilizzato per caricare la pagina internet
     *
     * @param link link di git corretto
     * @throws URISyntaxException URISyntaxException
     * @throws IOException        IOException
     */
    private void browse(String link) throws URISyntaxException, IOException {
        URI uri = new URI(link);
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            desktop.browse(uri);
        }
    }
}
