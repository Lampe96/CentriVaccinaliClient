package org.project.user;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import io.github.palexdev.materialfx.controls.MFXButton;
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
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.Rating;
import org.jetbrains.annotations.NotNull;
import org.project.UserType;
import org.project.login.LoginMainController;
import org.project.models.AdverseEvent;
import org.project.models.Hub;
import org.project.server.ServerReference;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Questa classe gestisce tutti i componenti presenti nella
 * schermata di aggiunta eventi avversi {@link UserHomeAddAdverseEventController},
 * accessibile da {@link UserHomeInfoHubController}.In caso di aggiunta con successo,
 * l'evento viene inserito nel DB tramite metodo remoto
 *
 * @author Federico Mainini 740691 (VA)
 * @author Gianluca Latronico 739893 (VA)
 * @author Marc Alexander Orlando 741473 (VA)
 * @author Enrico Luigi Lamperti 740612 (VA)
 */
public class UserHomeAddAdverseEventController implements Initializable {

    /**
     * Array utilizzato per riempire {@link #CB_event_type}
     */
    private final static String[] TYPEAE = {
            "MAL DI TESTA",
            "FEBBRE",
            "DOLORI MUSCOLARI",
            "DOLORI ARTICOLARI",
            "LINFOADENOPATIA",
            "TACHICARDIA",
            "CRISI IPERTENSIVA"
    };

    /**
     * AnchorPane esterno
     */
    @FXML
    private AnchorPane AP_ext;

    /**
     * ComboBox per selezionare il tipo
     * dell'evento avverso
     */
    @FXML
    private JFXComboBox<String> CB_event_type;

    /**
     * Field con le stelle da 1 a 5
     */
    @FXML
    private Rating R_severity;

    /**
     * Label per conteggio dei caratteri nel commento
     */
    @FXML
    private Label LB_char_counter;

    /**
     * Area di testo in cui lasciareuna nota
     */
    @FXML
    private JFXTextArea TA_text;

    /**
     * Immagine che funge da quit da questo stage
     */
    @FXML
    private ImageView BT_quit;

    /**
     * Bottone per aggiungere l'evento avverso
     */
    @FXML
    private MFXButton BT_add_adverse_event;

    /**
     * Stage riferito a questo controller
     */
    private Stage stage;

    /**
     * Variabile per il nickname
     */
    private String nick;

    /**
     * Dati necessari nella classe
     * dei centri vaccinali
     */
    private Hub hub;

    /**
     * Utilizzato per settare il nickname nel controller
     * 
     * @param nick nickname
     */
    void setNick(String nick) {
        this.nick = nick;
    }

    /**
     * Utilizzato per settare i dati dei centri vaccinali
     *
     * @param hub hub
     */
    void setHubData(Hub hub) {
        this.hub = hub;
    }

    /**
     * Utilizzato per riempire la combo box dei tipi di malesseri
     * e per verificare la lunghezza della nota
     *
     * @param url            url
     * @param resourceBundle resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            stage = (Stage) AP_ext.getScene().getWindow();

            CB_event_type.getItems().addAll(TYPEAE);

            TA_text.textProperty().addListener((observable, oldValue, newValue) -> {
                String value = newValue.strip();
                int vLength = value.length();
                LB_char_counter.setText(vLength + " / 255");
                if (vLength >= 255) {
                    LB_char_counter.setTextFill(Paint.valueOf("#FF0000"));
                    TA_text.setText(TA_text.getText(0, 255));
                } else {
                    LB_char_counter.setTextFill(Paint.valueOf("#4d4d4d"));
                }
            });
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
     * Collegato al bottone {@link #BT_add_adverse_event}, viene
     * verificata la correttezza dei campi e successivamente viene
     * effettuata l'aggiunta dell'evento su DB. Se il processo termina con
     * successo viene mostrato il pop-up {@link #addOtherEvent}, in caso
     * contrario il pop-up {@link #errorAlert(int)}, con 1 si indica un errore
     * nella compilazione dei campi, con 2 un evento avverso doppio
     *
     * @see org.project.server.Server#addAdverseEvent(AdverseEvent)
     */
    @FXML
    private void addAdverseEvent() {
        if (CB_event_type.getValue() != null && R_severity.getRating() != 0) {

            AdverseEvent ae = new AdverseEvent();
            ae.setEventType(CB_event_type.getValue());
            ae.setGravity((short) R_severity.getRating());
            ae.setText(StringUtils.capitalize(TA_text.getText().strip()));
            ae.setHubName(hub.getNameHub());
            ae.setNickname(nick);

            try {
                if (ServerReference.getServer().addAdverseEvent(ae)) {
                    addOtherEvent();
                } else {
                    errorAlert(2);
                }
            } catch (NotBoundException | RemoteException e) {
                e.printStackTrace();
            }
        } else {
            errorAlert(1);
        }
    }

    /**
     * Utilizzato per far apparire un pop-up
     * che chiedera' all'utente se vuole inserire un altro
     * evento avverso oppure uscire
     */
    private void addOtherEvent() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Evento aggiunto con successo, vuoi aggiungerne altri?");
        alert.initStyle(StageStyle.TRANSPARENT);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);

        ButtonType buttonTypeContinue = new ButtonType("Continua");
        ButtonType buttonTypeCancel = new ButtonType("Annulla", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeContinue, buttonTypeCancel);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setPrefSize(270, 0);
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

        Optional<ButtonType> result = alert.showAndWait();

        if (result.orElse(null) == buttonTypeContinue) {
            CB_event_type.setValue("");
            R_severity.setRating(0);
            TA_text.setText("");
            initialize(null, null);
        } else {
            stage.close();
        }
    }

    /**
     * Utilizzato per mostrare un pop-up di errore
     * (tipologia non selezionata o evento duplicato)
     *
     * @param typeError intero che indica la tipologia di errore da mostrare
     */
    private void errorAlert(int typeError) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Errore");
        if (typeError == 1) {
            alert.setContentText("Seleziona una tipologia e scegli una valutazione.");
        } else {
            alert.setContentText("Evento duplicato in questo centro.");
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
