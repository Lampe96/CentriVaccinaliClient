package org.project.user;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;
import org.project.UserType;
import org.project.models.AdverseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Questa classe gestisce le funzionalita' della row
 * presente nella visualizzazione delle info del centro
 * vaccinale
 *
 * @author Federico Mainini 740691 (VA)
 * @author Gianluca Latronico 739893 (VA)
 * @author Marc Alexander Orlando 741473 (VA)
 * @author Enrico Luigi Lamperti 740612 (VA)
 */
public class UserHubEventRowController implements Initializable {

    /**
     * Hbox con la row degli eventi avversi
     */
    @FXML
    private HBox HB_ext;

    /**
     * Label per il tipo di evento avverso
     */
    @FXML
    private Label LB_typology;

    /**
     * Label per la severita' dell'evento avverso
     */
    @FXML
    private Label LB_severity;

    /**
     * Immagine per tener traccia della
     */
    @FXML
    private ImageView IV_text;

    /**
     * Stage riferito a questo controller
     */
    private Stage stage;

    /**
     * Oggetto scaricato dal DB e passato dal controller
     * {@link UserHomeInfoHubController}, utilizzato per riempire le row
     * con i dati dell'evento in questione
     */
    private AdverseEvent ae;

    /**
     * Utilizzato per impostare i dati degli eventi avversi.
     *
     * @param ae          evento avverso
     * @param isUserEvent se true imposta il background della riga a verde
     * @param applyGrey   se true imposta il background della riga di un altro colore
     */
    public void setData(@NotNull AdverseEvent ae, boolean applyGrey, boolean isUserEvent) {
        this.ae = ae;

        LB_typology.setText(ae.getEventType());
        LB_severity.setText(String.valueOf(ae.getGravity()));

        if (!ae.getText().equals("")) {
            IV_text.setImage(new Image(String.valueOf(UserType.class.getResource("drawable/checkAe.png"))));
            Tooltip tool = new Tooltip("Visualizza questo evento avverso");
            tool.setShowDelay(new Duration(500));
            Tooltip.install(HB_ext, tool);
        } else {
            IV_text.setImage(new Image(String.valueOf(UserType.class.getResource("drawable/x_button.png"))));
        }

        if (!isUserEvent) {
            if (applyGrey) {
                HB_ext.setBackground(new Background(new BackgroundFill(Paint.valueOf("#eeeeee"), CornerRadii.EMPTY, Insets.EMPTY)));
            }
        } else {
            HB_ext.setBackground(new Background(new BackgroundFill(Paint.valueOf("#90ee90"), CornerRadii.EMPTY, Insets.EMPTY)));
        }
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
        Platform.runLater(() -> stage = (Stage) HB_ext.getScene().getWindow());
    }

    /**
     * Utilizzato per settare un ombra interna durante il passaggio del cursore di questa riga
     */
    @FXML
    private void darkStyleRow() {
        HB_ext.setEffect(new InnerShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, 0.5), 10, 0, 5, 5));
    }

    /**
     * Utilizzato per resettare lo stile di questa riga all'uscita del cursore
     */
    @FXML
    private void restoreStyleRow() {
        HB_ext.setEffect(null);
    }

    /**
     * Utilizzato per richiamare il metodo {@link #startInfoAdverseEvent}
     * se l'evento avverso contiene del testo
     */
    @FXML
    private void openInfoAdverseEvent() {
        if (!ae.getText().equals("")) {
            try {
                startInfoAdverseEvent();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Utilizzato per aprire la schermata in cui visualizzare le info dell'evento avverso
     * preso in considerazione, richiamato da {@link #openInfoAdverseEvent}
     * 
     * @throws IOException IOException
     */
    private void startInfoAdverseEvent() throws IOException {
        FXMLLoader loader = new FXMLLoader(UserHomeShowAdverseEventController.class.getResource("fxml/user_home_show_adverse_event.fxml"));
        Parent root = loader.load();
        UserHomeShowAdverseEventController userHomeShowAdverseEventController = loader.getController();
        userHomeShowAdverseEventController.setData(ae);

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        stage.setResizable(false);
        stage.setTitle("Evento avverso");
        stage.getIcons().add(new Image(Objects.requireNonNull(UserType.class.getResourceAsStream("drawable/primula.png"))));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(this.stage);

        AtomicReference<Double> xOffset = new AtomicReference<>((double) 0);
        AtomicReference<Double> yOffset = new AtomicReference<>((double) 0);

        scene.setOnMousePressed(mouseEvent -> {
            xOffset.set(mouseEvent.getSceneX());
            yOffset.set(mouseEvent.getSceneY());
        });

        scene.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() - xOffset.get());
            stage.setY(mouseEvent.getScreenY() - yOffset.get());
        });

        stage.showAndWait();
    }
}
