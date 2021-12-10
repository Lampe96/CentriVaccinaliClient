package org.project.user;

import com.jfoenix.controls.JFXComboBox;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXLabel;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.project.UserType;
import org.project.models.AdverseEvent;
import org.project.models.Hub;
import org.project.models.User;
import org.project.server.ServerReference;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Questa classe gestisce tutti i componenti presenti nella visualizzazione
 * delle informazioni del centro vaccinale preso in considerazione. Per
 * questo avra' bisogno di un oggetto centro vaccinale passato da {@link UserHomeItemRowController}.
 *
 * @author Federico Mainini 740691 (VA)
 * @author Gianluca Latronico 739893 (VA)
 * @author Marc Alexander Orlando 741473 (VA)
 * @author Enrico Luigi Lamperti 740612 (VA)
 */
public class UserHomeInfoHubController implements Initializable {

    /**
     * Array utilizzato per riempire la ComboBox filtri.
     */
    private final static String[] FILTER = {
            "MAL DI TESTA",
            "FEBBRE",
            "DOLORI MUSCOLARI",
            "DOLORI ARTICOLARI",
            "LINFOADENOPATIA",
            "TACHICARDIA",
            "CRISI IPERTENSIVA",
            "NO FILTRO"
    };

    /**
     * AnchorPane esterno.
     */
    @FXML
    private AnchorPane AP_ext;

    /**
     * Immagine che funge da quit dal programma.
     */
    @FXML
    private ImageView BT_quit;

    /**
     * Label per il nome dell'hub.
     */
    @FXML
    private MFXLabel TF_hub_name;

    /**
     * Label per il tipo dell' evento avverso.
     */
    @FXML
    private MFXLabel TF_type;

    /**
     * Label per l'indirizzo.
     */
    @FXML
    private MFXLabel TF_address;

    /**
     * Immagine per aprire la mappa all'indirizzo del centro vaccinale.
     */
    @FXML
    private ImageView IV_maps;

    /**
     * Immagine del centro vaccinale.
     */
    @FXML
    private ImageView IV_hub;

    /**
     * Immagine per ricaricare la pagina.
     */
    @FXML
    private ImageView IV_refresh;

    /**
     * ComboBox per i filtri.
     */
    @FXML
    private JFXComboBox<String> CB_filter;

    /**
     * Anchor per mettere la tooltip.
     */
    @FXML
    private AnchorPane AP_tooltip_btn;

    /**
     * Bottone per aggiungere evento avverso.
     */
    @FXML
    private MFXButton BT_add_adverse_event;

    /**
     * Vbox per mostrare gli eventi avversi.
     */
    @FXML
    private VBox VB_adverse_event_layout;

    /**
     * Stage riferito a questo controller.
     */
    private Stage stage;

    /**
     * ArrayList per i dati riguardanti gli eventi avversi.
     */
    private ArrayList<AdverseEvent> aae;

    /**
     * Dati dei centri vaccinali.
     */
    private Hub hub;

    /**
     * Dati degli utenti.
     */
    private User us;

    /**
     * Boolean per verificare se l'evento e'
     * stato inserito dall'utente.
     */
    private boolean userEvent = false;

    /**
     * Utilizzato per impostare i dati dei centri vaccinali.
     *
     * @param hub centro vaccinale
     */
    void setData(Hub hub) {
        this.hub = hub;
    }

    /**
     * Utilizzato per impostare i dati degli Utenti.
     *
     * @param us Utenti
     */
    void setUser(User us) {
        this.us = us;
    }

    /**
     * Utilizzato per inizializzare l'interfaccia riempiendo
     * varie label tra cui: nome centro vaccinale, tipo evento avverso, indirizzo centro vaccinale e l'immagine
     * del centro vaccinale.
     * Inoltre carica la row degli eventi avversi e riempe la combobox con i filtri, infine aggiunge
     * i tooltip.
     *
     * @param url            url
     * @param resourceBundle resourceBundle
     * @see org.project.server.Server#checkBeforeAddEvent(String, String)
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            stage = (Stage) AP_ext.getScene().getWindow();

            TF_hub_name.setText(hub.getNameHub());
            TF_type.setText(hub.getType());
            TF_address.setText(hub.getAddress().toStringCustom());
            IV_hub.setImage(new Image(Objects.requireNonNull(UserType.class.getResourceAsStream("drawable/hospital_icon_" + hub.getImage() + ".png"))));

            fillRow();

            CB_filter.getItems().addAll(FILTER);

            CB_filter.valueProperty().addListener((observable, oldValue, newValue) -> filterSelection(CB_filter.getValue()));

            try {
                if (us.getName().equals("Guest")) {
                    BT_add_adverse_event.setDisable(true);

                    Tooltip tool3 = new Tooltip("Registrati per poter\nusare questa funzione");
                    tool3.setShowDelay(new Duration(500));
                    Tooltip.install(AP_tooltip_btn, tool3);
                } else if (!ServerReference.getServer().checkBeforeAddEvent(hub.getNameHub(), us.getFiscalCode())) {
                    BT_add_adverse_event.setDisable(true);

                    Tooltip tool3 = new Tooltip("Non sei stato vaccinato\npresso questo centro");
                    tool3.setShowDelay(new Duration(500));
                    Tooltip.install(AP_tooltip_btn, tool3);
                }
            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
            }

            Tooltip tool = new Tooltip(hub.getNameHub());
            tool.setShowDelay(new Duration(500));
            Tooltip.install(TF_hub_name, tool);

            Tooltip tool1 = new Tooltip(hub.getAddress().toStringCustom());
            tool1.setShowDelay(new Duration(500));
            Tooltip.install(TF_address, tool1);

            Tooltip tool2 = new Tooltip("Visualizza sulla mappa");
            tool2.setShowDelay(new Duration(500));
            Tooltip.install(IV_maps, tool2);
        });
    }

    /**
     * Utilizzato per riempire le righe con tutti gli eventi avversi
     * riferiti al centro vaccinale selezionato. Viene chiamato in
     * {@link #initialize} e {@link #refreshVaccinatedList}.
     *
     * @see org.project.server.Server#fetchAllAdverseEvent(String)
     */
    private void fillRow() {
        try {
            aae = ServerReference.getServer().fetchAllAdverseEvent(hub.getNameHub());
            aae.forEach(ae -> {
                try {
                    userEvent = ae.getNickname().equals(us.getNickname());
                    loadAdverseEvent(ae, aae.indexOf(ae) % 2 == 0, userEvent);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Utilizzato per caricare le righe degli eventi avversi di un centro vaccinale.
     *
     * @param ae          evento avverso
     * @param applyGrey   se true imposta il background della riga di un altro colore
     * @param isUserEvent se l'evento avverso appartiene all'utente loggato colora la riga di verde
     * @throws IOException IOException
     */
    private void loadAdverseEvent(AdverseEvent ae, boolean applyGrey, boolean isUserEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(UserHomeController.class.getResource("fxml/user_hub_event_row.fxml"));

        HBox hBox = fxmlLoader.load();
        UserHubEventRowController uherc = fxmlLoader.getController();
        uherc.setData(ae, applyGrey, isUserEvent);
        VB_adverse_event_layout.getChildren().add(hBox);
    }

    /**
     * Utilizzato per chiudere questo stage.
     */
    @FXML
    private void quit() {
        stage.close();
    }

    /**
     * Utilizzato per impostare un'ombra interna durante il passaggio del cursore di questa icona.
     */
    @FXML
    private void darkStyleQuit() {
        setDarkHover(BT_quit);
    }

    /**
     * Utilizzato per azzerare lo stile di questa icona all'uscita del cursore.
     */
    @FXML
    private void restoreStyleQuit() {
        resetDarkExit(BT_quit);
    }

    /**
     * Utilizzato per impostare un'ombra interna su una imageview.
     *
     * @param iv imageview da scurire
     */
    private void setDarkHover(@NotNull ImageView iv) {
        iv.setEffect(new InnerShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, 0.5), 10, 0, 5, 5));
    }

    /**
     * Utilizzato per azzerare l'effetto su una imageview.
     *
     * @param iv imageview in cui azzerare lo stile
     */
    private void resetDarkExit(@NotNull ImageView iv) {
        iv.setEffect(null);
    }

    /**
     * Utilizzato per aggiornare la lista degli eventi avversi.
     */
    @FXML
    private void refreshVaccinatedList() {
        RotateTransition rt = new RotateTransition(Duration.seconds(1), IV_refresh);
        rt.setFromAngle(360);
        rt.setToAngle(0);
        rt.setCycleCount(2);
        rt.setInterpolator(Interpolator.LINEAR);
        rt.play();

        CB_filter.setValue("");
        VB_adverse_event_layout.getChildren().clear();
        fillRow();
    }

    /**
     * Utilizzato per impostare un'ombra interna durante il passaggio del cursore di questa icona.
     */
    @FXML
    private void darkStyleRefresh() {
        setDarkHover(IV_refresh);
    }

    /**
     * Utilizzato per azzerare lo stile di questa icona all'uscita del cursore.
     */
    @FXML
    private void restoreStyleRefresh() {
        resetDarkExit(IV_refresh);
    }

    /**
     * Utilizzato per visualizzare la schermata di aggiunta di un evento avverso attraverso questo
     * metodo {@link #startAddAdverseEvent}.
     */
    @FXML
    private void addAdverseEvent() {
        try {
            startAddAdverseEvent();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Utilizzato per visualizzare la schermata di aggiunta di un evento avverso.
     *
     * @throws IOException IOException
     */
    private void startAddAdverseEvent() throws IOException {
        FXMLLoader loader = new FXMLLoader(UserHomeController.class.getResource("fxml/user_home_add_adverse_event.fxml"));
        Parent root = loader.load();
        UserHomeAddAdverseEventController userHomeAddAdverseEventController = loader.getController();
        userHomeAddAdverseEventController.setNick(us.getNickname());
        userHomeAddAdverseEventController.setHubData(hub);

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        stage.setResizable(false);
        stage.setTitle("Aggiungi evento");
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

        VB_adverse_event_layout.getChildren().clear();
        fillRow();
    }

    /**
     * Utilizzato per comporre il link per poi aprirlo sul browser utilizzando google maps.
     */
    @FXML
    private void openMaps() {
        String address = hub.getAddress().getAddress().replaceAll("\\s+", "+");
        String city = hub.getAddress().getCity().replaceAll("\\s+", "+");

        try {
            browse("https://www.google.it/maps/place/" +
                    hub.getAddress().getQualificator() + "+" +
                    address + ",+" +
                    hub.getAddress().getNumber() + ",+" +
                    hub.getAddress().getCap() + "+" +
                    city + "+" +
                    hub.getAddress().getProvince());
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Utilizzato per impostare un'ombra interna durante il passaggio del cursore di questa icona.
     */
    public void darkStyleMaps() {
        setDarkHover(IV_maps);
    }

    /**
     * Utilizzato per azzerare lo stile di questa icona all'uscita del cursore.
     */
    public void restoreStyleMaps() {
        resetDarkExit(IV_maps);
    }

    /**
     * Utilizzato per aprire il browser e navigare nel link passato.
     *
     * @param link link per aprire le mappe
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

    /**
     * Viene chiamato nel metodo {@link #initialize}, viene associato
     * a {@link #FILTER} e gestisce la selezione del malessere.
     *
     * @param value tipologia di malessere
     */
    private void filterSelection(@NotNull String value) {
        switch (value) {
            case "MAL DI TESTA":
                filterList(0);
                break;

            case "FEBBRE":
                filterList(1);
                break;

            case "DOLORI MUSCOLARI":
                filterList(2);
                break;

            case "DOLORI ARTICOLARI":
                filterList(3);
                break;

            case "LINFOADENOPATIA":
                filterList(4);
                break;

            case "TACHICARDIA":
                filterList(5);
                break;

            case "CRISI IPERTENSIVA":
                filterList(6);
                break;

            case "NO FILTRO":
                VB_adverse_event_layout.getChildren().clear();
                aae.forEach(ae -> {
                    try {
                        userEvent = ae.getNickname().equals(us.getNickname());
                        loadAdverseEvent(ae, aae.indexOf(ae) % 2 == 0, userEvent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        }
    }

    /**
     * Utilizzato nel metodo {@link #filterSelection}, va a
     * filtrare in base all'intero passato, il quale si riferisce
     * alla posizione corrispondente al malessere presente nell'array
     * {@link #FILTER}.
     *
     * @param type tipo di filtro
     */
    private void filterList(int type) {
        ArrayList<AdverseEvent> aaef = (ArrayList<AdverseEvent>) aae.stream().filter(ae ->
                StringUtils.containsIgnoreCase(ae.getEventType(), (FILTER[type]))).collect(Collectors.toList());
        VB_adverse_event_layout.getChildren().clear();

        aaef.forEach(ae -> {
            try {
                userEvent = ae.getNickname().equals(us.getNickname());
                loadAdverseEvent(ae, aae.indexOf(ae) % 2 == 0, userEvent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
