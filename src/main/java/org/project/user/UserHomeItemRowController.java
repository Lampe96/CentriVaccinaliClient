package org.project.user;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.project.models.Hub;

import java.net.URL;
import java.util.ResourceBundle;

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

    void setData(@NotNull Hub hub, boolean applyGrey) {
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

    }

    private void checkType(@NotNull String type){
        switch (type){
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

