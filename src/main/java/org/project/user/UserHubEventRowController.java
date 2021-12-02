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

public class UserHubEventRowController implements Initializable {

    @FXML
    private HBox HB_ext;

    @FXML
    private Label LB_typology;

    @FXML
    private Label LB_severity;

    @FXML
    private ImageView IV_text;

    private Stage stage;
    private AdverseEvent ae;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> stage = (Stage) HB_ext.getScene().getWindow());
    }

    @FXML
    private void darkStyleRow() {
        HB_ext.setEffect(new InnerShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, 0.5), 10, 0, 5, 5));
    }

    @FXML
    private void restoreStyleRow() {
        HB_ext.setEffect(null);
    }

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
