package org.project.hub;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXLabel;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.project.guest.GuestHomeController;
import org.project.models.VaccinatedUser;
import org.project.server.ServerReference;
import org.project.utils.WindowUtil;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HubHomeController implements Initializable {

    @FXML
    private AnchorPane AP_ext;

    @FXML
    private MFXButton BT_setting;

    @FXML
    private MFXButton BT_about;

    @FXML
    private MFXButton BT_logout;

    @FXML
    private ImageView BT_quit;

    @FXML
    private ImageView BT_minimize;

    @FXML
    private Label LB_hub_name;

    @FXML
    private MFXLabel LB_total_vaccinated;

    @FXML
    private MFXLabel LB_total_vaccinated_center;

    @FXML
    private MFXLabel LB_vaccinated_first;

    @FXML
    private MFXLabel LB_vaccinated_second;

    @FXML
    private MFXButton BT_registration_citizen;

    @FXML
    private MFXTextField TF_search_citizen;

    @FXML
    private VBox VB_vaccinated_layout;


    private Stage stage;
    private double xPos = 0;
    private double yPos = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startUpLocation(AP_ext.getPrefWidth(), AP_ext.getPrefHeight());
        Platform.runLater(() -> {
            stage = (Stage) AP_ext.getScene().getWindow();
            if (xPos == 0.0 && yPos == 0.0) {
                stage.centerOnScreen();
            } else {
                stage.setX(xPos);
                stage.setY(yPos);
            }

            String hubName = TempHub.getHubName();
            LB_hub_name.setText(hubName);
            System.out.println(hubName);

            try {
                ArrayList<VaccinatedUser>  vc = new ArrayList<>(ServerReference.getServer().fetchHubVaccinatedUser(hubName));
                for (VaccinatedUser vaccinatedUser : vc) {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(HubHomeController.class.getResource("fxml/hub_home_row.fxml"));

                    try {
                        HBox hBox = fxmlLoader.load();
                        HubItemRowController hirc = fxmlLoader.getController();
                        hirc.setData(vaccinatedUser);
                        VB_vaccinated_layout.getChildren().add(hBox);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
            }
        });
    }


    private void startUpLocation(double windowWidth, double windowHeight) {
        Point p = MouseInfo.getPointerInfo().getLocation();
        List<Screen> screens = Screen.getScreens();
        if (p != null && screens != null && screens.size() > 1) {
            Rectangle2D screenBounds;
            for (Screen screen : screens) {
                screenBounds = screen.getVisualBounds();
                if (screenBounds.contains(p.x, p.y)) {
                    xPos = screenBounds.getMinX() + ((screenBounds.getMaxX() - screenBounds.getMinX() - windowWidth) / 2);
                    yPos = screenBounds.getMinY() + ((screenBounds.getMaxY() - screenBounds.getMinY() - windowHeight) / 2);
                }
            }
        }
    }

    @FXML
    private void minimize() {
        stage.setIconified(true);
    }

    @FXML
    private void darkStyleMinimize() {
        setDarkHover(BT_minimize);
    }

    @FXML
    private void restoreStyleMinimize() {
        resetDarkExit(BT_minimize);
    }

    @FXML
    private void quit() {
        System.exit(0);
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
    public void openSetting() {
        try {
            startSetting();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startSetting() throws IOException {
        //todo fare fxml impostazioni
        Scene scene = new Scene(WindowUtil.newScene(GuestHomeController.class.getResource("fxml/guest_home.fxml")));
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Impostazioni");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(this.stage);
        stage.show();
    }

    @FXML
    public void logout() {
        //todo fare popup sei sicuro di voler uscire
    }

    @FXML
    public void openAbout() {
        try {
            startAbout();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startAbout() throws IOException {
        //todo fare fxml about
        Scene scene = new Scene(WindowUtil.newScene(GuestHomeController.class.getResource("fxml/guest_home.fxml")));
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("About");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(this.stage);
        stage.show();
    }
}
