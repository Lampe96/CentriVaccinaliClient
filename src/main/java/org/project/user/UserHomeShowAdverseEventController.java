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

public class UserHomeShowAdverseEventController implements Initializable {

    @FXML
    private AnchorPane AP_ext;

    @FXML
    private ImageView BT_quit;

    @FXML
    private Rating R_severity;

    @FXML
    private JFXTextArea TA_text;

    @FXML
    private MFXLabel LB_type;

    private Stage stage;
    private AdverseEvent ae;

    void setData(AdverseEvent ae){
        this.ae = ae;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            stage = (Stage) AP_ext.getScene().getWindow();
            LB_type.setText(ae.getEventType());
            R_severity.setRating(ae.getGravity());
            TA_text.setText(ae.getText());
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
}

