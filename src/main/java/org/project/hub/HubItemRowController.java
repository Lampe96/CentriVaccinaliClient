package org.project.hub;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.project.UserType;
import org.project.models.VaccinatedUser;
import org.project.server.ServerReference;

import java.net.URISyntaxException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class HubItemRowController implements Initializable {

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setData(VaccinatedUser vaccinatedUser){
        LB_name.setText(vaccinatedUser.getName());
        LB_surname.setText(vaccinatedUser.getSurname());
        LB_surname.setText(vaccinatedUser.getNickname());

        if (vaccinatedUser.getId().equals("1")){
            IV_dose.setImage(new Image(String.valueOf(UserType.class.getResource("drawable/number_1.png"))));
        } else {
            IV_dose.setImage(new Image(String.valueOf(UserType.class.getResource("drawable/number_2.png"))));
        }

        if (vaccinatedUser.getEvent() != null) {

        }

    }
}
