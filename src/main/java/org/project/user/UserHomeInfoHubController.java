package org.project.user;

import com.jfoenix.controls.JFXComboBox;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;
import org.project.UserType;
import org.project.models.AdverseEvent;
import org.project.models.Hub;
import org.project.server.ServerReference;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class UserHomeInfoHubController implements Initializable {

    private final static String[] FILTER = {
            "MAL DI TESTA",
            "FEBBRE",
            "DOLORI MUSCOLARI",
            "DOLORI ARTICOLARI",
            "LINFOADENOPATIA",
            "TACHICARDIA",
            "CRISI IPERTENSIVA",
            "NO FILTRO"
    };

    @FXML
    private AnchorPane AP_ext;

    @FXML
    private ImageView BT_quit;

    @FXML
    private MFXTextField TF_hub_name;

    @FXML
    private MFXTextField TF_type;

    @FXML
    private MFXTextField TF_address;

    @FXML
    private ImageView IV_maps;

    @FXML
    private ImageView IV_hub;

    @FXML
    private ImageView IV_refresh;

    @FXML
    private JFXComboBox<String> CB_filter;

    @FXML
    private MFXButton BT_add_adverse_event;

    @FXML
    private VBox VB_adverse_event_layout;

    private Stage stage;
    private ArrayList<AdverseEvent> aae;
    private Hub hub;
    private String nick;

    void setData(Hub hub) {
        this.hub = hub;
    }

    void setNick(String nick) {
        this.nick = nick;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            stage = (Stage) AP_ext.getScene().getWindow();

            TF_hub_name.setText(hub.getNameHub());
            TF_type.setText(hub.getType());
            TF_address.setText(hub.getAddress().toStringCustom());
            System.out.println(hub.getImage());
            IV_hub.setImage(new Image(Objects.requireNonNull(UserType.class.getResourceAsStream("drawable/hospital_icon_" + hub.getImage() + ".png"))));

            try {
                aae = ServerReference.getServer().fetchAllAdverseEvent(hub.getNameHub());
                aae.forEach(ae -> {
                    try {
                        loadAdverseEvent(ae, aae.indexOf(ae) % 2 == 0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
            }

            CB_filter.getItems().addAll(FILTER);
        });
    }

    private void loadAdverseEvent(AdverseEvent ae, boolean applyGrey) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(UserHomeController.class.getResource("fxml/user_hub_event_row.fxml"));

        HBox hBox = fxmlLoader.load();
        UserHubEventRowController uherc = fxmlLoader.getController();
        uherc.setData(ae, applyGrey);
        VB_adverse_event_layout.getChildren().add(hBox);
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
    private void refreshVaccinatedList() {
        RotateTransition rt = new RotateTransition(Duration.seconds(1), IV_refresh);
        rt.setFromAngle(360);
        rt.setToAngle(0);
        rt.setCycleCount(2);
        rt.setInterpolator(Interpolator.LINEAR);
        rt.play();

        VB_adverse_event_layout.getChildren().clear();
        initialize(null, null);
    }

    @FXML
    private void darkStyleRefresh() {
        setDarkHover(IV_refresh);
    }

    @FXML
    private void restoreStyleRefresh() {
        resetDarkExit(IV_refresh);
    }

    @FXML
    private void addAdverseEvent() {
        try {
            startAddAdverseEvent();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startAddAdverseEvent() throws IOException {
        FXMLLoader loader = new FXMLLoader(UserHomeController.class.getResource("fxml/user_home_add_adverse_event.fxml"));
        Parent root = loader.load();
        UserHomeAddAdverseEventController userHomeAddAdverseEventController = loader.getController();
        userHomeAddAdverseEventController.setNick(nick);
        userHomeAddAdverseEventController.setHubData(hub);

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        stage.setResizable(false);
        stage.setTitle("Aggiungi evento");
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

    @FXML
    private void openMaps() {
        String address = hub.getAddress().getAddress().replaceAll("\\s+", "+");
        String city = hub.getAddress().getCity().replaceAll("\\s+", "+");

        try {
            browse("https://www.google.it/maps/place/" +
                    hub.getAddress().getQualificator() + "+" +
                    address + ",+" +
                    hub.getAddress().getNumber() + ",+" +
                    hub.getAddress().getCap() + "+" +
                    city + "+" +
                    hub.getAddress().getProvince());
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    public void darkStyleMaps() {
        setDarkHover(IV_maps);
    }

    public void restoreStyleMaps() {
        resetDarkExit(IV_maps);
    }

    private void browse(String link) throws URISyntaxException, IOException {
        URI uri = new URI(link);
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            desktop.browse(uri);
        }
    }
}
