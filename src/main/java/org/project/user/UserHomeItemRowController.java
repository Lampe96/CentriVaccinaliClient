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
import org.project.models.Hub;
import org.project.models.User;
import org.project.server.ServerReference;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Questa classe gestisce le funzionalita' della row
 * presente nella home dei cittadini
 *
 * @author Federico Mainini 740691 (VA)
 * @author Gianluca Latronico 739893 (VA)
 * @author Marc Alexander Orlando 741473 (VA)
 * @author Enrico Luigi Lamperti 740612 (VA)
 */
public class UserHomeItemRowController implements Initializable {

    /**
     * HBox esterno
     */
    @FXML
    private HBox HB_ext;

    /**
     * Label per il nome del centro vaccinale
     */
    @FXML
    private Label LB_hub_name;

    /**
     * Label per la tipologia del centro vaccinale
     */
    @FXML
    private Label LB_typology;

    /**
     * Label per il comune del centro vaccinale
     */
    @FXML
    private Label LB_city;

    /**
     * Label per la media degli eventi segnalati presso quel centro
     */
    @FXML
    private Label LB_avg_adverse_event;

    /**
     * Stage riferito a questo controller
     */
    private Stage stage;

    /**
     * Oggetto scaricato dal DB e passato dal controller
     * {@link UserHomeController}, utilizzato per riempire le row
     * con i dati del centro vaccinali
     */
    private Hub hub;

    /**
     * Oggetto scaricato dal DB e passato dal controller
     * {@link UserHomeController}
     */
    private User us;

    /**
     * Utilizzato per impostare i dati dei centri vaccinali e degli utenti.
     * Inoltre riempe alcune label come: nome del centro vaccinale e citta'.
     * Utilizzato anche per fare il controllo e impostare la tipologia del centro
     * vaccinale
     *
     * @param hub       centro vaccinale
     * @param us        utente
     * @param applyGrey se true imposta il background della riga di un altro colore
     * @see org.project.server.Server#getAvgAdverseEvent(String)
     */
    void setData(@NotNull Hub hub, User us, boolean applyGrey) {
        this.hub = hub;
        this.us = us;

        LB_hub_name.setText(hub.getNameHub());
        checkType(hub.getType());
        LB_city.setText(hub.getAddress().getCity());

        try {
            double avg = Math.round(ServerReference.getServer().getAvgAdverseEvent(hub.getNameHub()) * 10.0) / 10.0;
            if (avg > 0.0) {
                LB_avg_adverse_event.setText(String.valueOf(avg));
            } else {
                LB_avg_adverse_event.setText("Nessun e.a.");
            }
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }

        if (applyGrey) {
            HB_ext.setBackground(new Background(new BackgroundFill(Paint.valueOf("#eeeeee"), CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }

    /**
     * Utilizzato per inizializzare l'interfaccia
     * prendendo la scena e impostare il tooltip sulle righe
     *
     * @param url            url
     * @param resourceBundle resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> stage = (Stage) HB_ext.getScene().getWindow());

        Tooltip tool = new Tooltip("Visualizza info centro vaccinale");
        tool.setShowDelay(new Duration(500));
        Tooltip.install(HB_ext, tool);
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
     * Utilizzato per caricare lo stagee la scena dell'{@link UserHomeInfoHubController} e impostare
     * i dati relativi a centri vaccinali e i dati degli utenti
     */
    @FXML
    private void openInfoHub() {
        try {
            FXMLLoader loader = new FXMLLoader(UserHomeInfoHubController.class.getResource("fxml/user_home_info_hub.fxml"));
            Parent root = loader.load();
            UserHomeInfoHubController userHomeInfoHubController = loader.getController();

            userHomeInfoHubController.setData(hub);
            userHomeInfoHubController.setUser(us);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            scene.setFill(Color.TRANSPARENT);
            stage.setResizable(false);
            stage.setTitle("Visualizza info hub");
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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Utilizzato per impostare nella label della tipologia l'abbreviazione della tipologia
     * |       Key      |    Value   |
     * |----------------|------------|
     * | OSPEDALIERO    | OSPED      |
     * | AZIENDALE      | AZIEND     |
     * | HUB            | HUB        |
     *
     * @param type tipologia di centro vaccinale
     */
    private void checkType(@NotNull String type) {
        switch (type) {
            case "OSPEDALIERO":
                LB_typology.setText("OSPED");
                break;

            case "AZIENDALE":
                LB_typology.setText("AZIEND");
                break;

            case "HUB":
                LB_typology.setText("HUB");
                break;
        }
    }
}
