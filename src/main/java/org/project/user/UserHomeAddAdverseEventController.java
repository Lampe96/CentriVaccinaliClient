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

public class UserHomeAddAdverseEventController implements Initializable {

    private final static String[] TYPE = {
            "MAL DI TESTA",
            "FEBBRE",
            "DOLORI MUSCOLARI",
            "DOLORI ARTICOLARI",
            "LINFOADENOPATIA",
            "TACHICARDIA",
            "CRISI IPERTENSIVA"
    };

    @FXML
    private AnchorPane AP_ext;

    @FXML
    private JFXComboBox<String> CB_event_type;

    @FXML
    private Rating R_severity;

    @FXML
    private Label LB_char_counter;

    @FXML
    private JFXTextArea TA_text;

    @FXML
    private ImageView BT_quit;

    @FXML
    private MFXButton BT_add_adverse_event;

    private Stage stage;
    private String nick;
    private Hub hub;

    void setNick(String nick) {
        this.nick = nick;
    }

    void setHubData(Hub hub) {
        this.hub = hub;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            stage = (Stage) AP_ext.getScene().getWindow();

            TA_text.textProperty().addListener((observable, oldValue, newValue) -> {
                String value = newValue.strip();
                LB_char_counter.setText(value.length() + " / 255");
                if(value.length() >= 255){
                    LB_char_counter.setTextFill(Paint.valueOf("#FF0000"));
                    TA_text.setText(TA_text.getText(0, 255));
                }else {
                    LB_char_counter.setTextFill(Paint.valueOf("#4d4d4d"));
                }
            });

            CB_event_type.getItems().addAll(TYPE);
        });
    }

    @FXML
    private void quit() {
        stage.close();
    }

    @FXML
    private void darkStyleQuit() {
        setDarkHover(BT_quit);
    }

    @FXML
    private void restoreStyleQuit() {
        resetDarkExit(BT_quit);
    }

    private void setDarkHover(@NotNull ImageView iv) {
        iv.setEffect(new InnerShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, 0.5), 10, 0, 5, 5));
    }

    private void resetDarkExit(@NotNull ImageView iv) {
        iv.setEffect(null);
    }

    @FXML
    private void addAdverseEvent() {
        if(CB_event_type.getValue() != null && R_severity.getRating() != 0){

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
        }else{
            errorAlert(1);
        }
    }

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

        if(result.orElse(null) == buttonTypeContinue){
            CB_event_type.setValue("");
            R_severity.setRating(0);
            TA_text.setText("");
            initialize(null, null);
        }else{
            stage.close();
        }
    }

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
