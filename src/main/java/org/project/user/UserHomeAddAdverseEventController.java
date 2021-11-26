package org.project.user;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.Rating;
import org.jetbrains.annotations.NotNull;
import org.project.models.AdverseEvent;
import org.project.models.Hub;
import org.project.server.ServerReference;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

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
    private JFXTextArea TA_text;

    @FXML
    private ImageView BT_quit;

    @FXML
    private MFXButton BT_add_adverse_event;

    private Stage stage;
    private String nick;
    private Hub hub;

    void setNick(String nick){
        this.nick = nick;
    }

    void setHubData(Hub hub){
        this.hub = hub;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            stage = (Stage) AP_ext.getScene().getWindow();

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
    void addAdverseEvent() {
        AdverseEvent ae = new AdverseEvent();
        ae.setEventType(CB_event_type.getValue());
        ae.setGravity((short) R_severity.getRating());
        ae.setText(StringUtils.capitalize(TA_text.getText().strip()));
        ae.setHubName(hub.getNameHub());
        ae.setNickname(nick);

        try {
            if (ServerReference.getServer().addAdverseEvent(ae)){
                //popup altro evento
                System.out.println("aaaaaaaaaaaaaaaaaaa");
            } else {
                //popoup errore
                System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbb");
            }
        } catch (NotBoundException | RemoteException e) {
            e.printStackTrace();
        }
    }
}
