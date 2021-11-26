package org.project.user;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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
import org.jetbrains.annotations.NotNull;
import org.project.UserType;
import org.project.models.Hub;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class UserHomeItemRowController implements Initializable {

    @FXML
    private HBox HB_ext;

    @FXML
    private Label LB_hub_name;

    @FXML
    private Label LB_typology;

    @FXML
    private Label LB_city;

    @FXML
    private Label LB_avg_adverse_event;

    private Stage stage;
    private Hub hub;
    private String nick;

    void setData(@NotNull Hub hub, String nick, boolean applyGrey) {
        this.hub = hub;
        this.nick = nick;

        LB_hub_name.setText(hub.getNameHub());
        checkType(hub.getType());
        LB_city.setText(hub.getAddress().getCity());
        //TODO query con media
        LB_avg_adverse_event.setText(String.valueOf(0));

        if (applyGrey) {
            HB_ext.setBackground(new Background(new BackgroundFill(Paint.valueOf("#eeeeee"), CornerRadii.EMPTY, Insets.EMPTY)));
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
    private void openInfoHub() {
        try {
            FXMLLoader loader = new FXMLLoader(UserHomeInfoHubController.class.getResource("fxml/user_home_info_hub.fxml"));
            Parent root = loader.load();
            UserHomeInfoHubController userHomeInfoHubController = loader.getController();

            userHomeInfoHubController.setData(hub);
            userHomeInfoHubController.setNick(nick);

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

