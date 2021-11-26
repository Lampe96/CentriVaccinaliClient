package org.project.user;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
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
import org.jetbrains.annotations.NotNull;
import org.project.UserType;
import org.project.models.AdverseEvent;

public class UserHubEventRowController {


    @FXML
    private HBox HB_ext;

    @FXML
    private Label LB_typology;

    @FXML
    private Label LB_severity;

    @FXML
    private ImageView IV_text;

    private AdverseEvent ae;

    public void setData(@NotNull AdverseEvent ae, boolean applyGrey) {
        this.ae = ae;

        LB_typology.setText(ae.getEventType());
        LB_severity.setText(String.valueOf(ae.getGravity()));

        if (!ae.getText().equals("")) {
            IV_text.setImage(new Image(String.valueOf(UserType.class.getResource("drawable/checkAe.png"))));
        } else {
            IV_text.setImage(new Image(String.valueOf(UserType.class.getResource("drawable/x_button.png"))));
        }

        if (applyGrey) {
            HB_ext.setBackground(new Background(new BackgroundFill(Paint.valueOf("#eeeeee"), CornerRadii.EMPTY, Insets.EMPTY)));
        }
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
    private void openInfoVaccinated() {

    }
}
