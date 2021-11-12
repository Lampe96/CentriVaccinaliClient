package org.project.hub;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import org.jetbrains.annotations.NotNull;
import org.project.UserType;
import org.project.models.VaccinatedUser;

public class HubHomeItemRowController {

    @FXML
    private HBox HB_ext;

    @FXML
    private Label LB_surname;

    @FXML
    private Label LB_name;

    @FXML
    private Label LB_nickname;

    @FXML
    private ImageView IV_dose;

    @FXML
    private ImageView IV_event;

    public void setData(@NotNull VaccinatedUser vaccinatedUser, boolean applyGrey) {
        System.out.println(vaccinatedUser);
        LB_name.setText(vaccinatedUser.getName());
        LB_surname.setText(vaccinatedUser.getSurname());
        LB_nickname.setText(vaccinatedUser.getNickname());

        if (vaccinatedUser.getId().startsWith("1")) {
            IV_dose.setImage(new Image(String.valueOf(UserType.class.getResource("drawable/number_1.png"))));
        } else {
            IV_dose.setImage(new Image(String.valueOf(UserType.class.getResource("drawable/number_2.png"))));
        }

        if (vaccinatedUser.getEvent() == null) {
            IV_event.setVisible(false);
        }

        if (applyGrey) {
            HB_ext.setBackground(new Background(new BackgroundFill(Paint.valueOf("#eeeeee"), CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }
}
