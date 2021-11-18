package org.project.hub;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXLabel;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.project.UserType;
import org.project.login.LoginMainController;
import org.project.models.VaccinatedUser;
import org.project.server.ServerReference;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class HubHomeController implements Initializable {

    @FXML
    private AnchorPane AP_ext;

    @FXML
    private ImageView IV_hub;

    @FXML
    private Label LB_hub_name;

    @FXML
    private Label LB_hub_address;

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
    private MFXLabel LB_total_vaccinated;

    @FXML
    private MFXLabel LB_total_vaccinated_center;

    @FXML
    private MFXLabel LB_vaccinated_first;

    @FXML
    private MFXLabel LB_vaccinated_second;

    @FXML
    private MFXButton BT_open_chart;

    @FXML
    private MFXTextField TF_search_citizen;

    @FXML
    private MFXButton BT_registration_citizen;

    @FXML
    private VBox VB_vaccinated_layout;

    private Stage stage;
    private double xPos = 0;
    private double yPos = 0;
    private double xOffset, yOffset;
    private int hubImage;
    private String hubName;
    private ArrayList<VaccinatedUser> avu;
    private HubHomeSettingsController hubHomeSettingsController;

    public void setNameHub(String hubName) {
        this.hubName = hubName;
    }

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

            LB_hub_name.setText(hubName);

            try {
                hubImage = ServerReference.getServer().getImage(hubName);
                IV_hub.setImage(new Image(Objects.requireNonNull(UserType.class.getResourceAsStream("drawable/hospital_icon_" + hubImage + ".png"))));
                LB_hub_address.setText(ServerReference.getServer().getAddress(hubName).toStringCustom());
            } catch (NotBoundException | RemoteException e) {
                e.printStackTrace();
            }

            try {
                avu = ServerReference.getServer().fetchHubVaccinatedUser(hubName);
                avu.forEach(vu -> {
                    try {
                        loadVaccinatedUserRow(vu, avu.indexOf(vu) % 2 == 0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
            }
        });

        TF_search_citizen.textProperty().addListener((observable, oldValue, newValue) -> {
            String value = newValue.strip().replaceAll("\\s+", "");
            if (!value.equals("")) {
                ArrayList<VaccinatedUser> vuf = (ArrayList<VaccinatedUser>) avu.stream().filter(vu ->
                        StringUtils.containsIgnoreCase(vu.getSurname() + vu.getName() + vu.getNickname(), (value)) ||
                                StringUtils.containsIgnoreCase(vu.getSurname() + vu.getNickname() + vu.getName(), (value)) ||
                                StringUtils.containsIgnoreCase(vu.getName() + vu.getSurname() + vu.getNickname(), (value)) ||
                                StringUtils.containsIgnoreCase(vu.getName() + vu.getNickname() + vu.getSurname(), (value)) ||
                                StringUtils.containsIgnoreCase(vu.getNickname() + vu.getSurname() + vu.getName(), (value)) ||
                                StringUtils.containsIgnoreCase(vu.getNickname() + vu.getName() + vu.getSurname(), (value))).collect(Collectors.toList());
                VB_vaccinated_layout.getChildren().clear();
                vuf.forEach(vu -> {
                    try {
                        loadVaccinatedUserRow(vu, vuf.indexOf(vu) % 2 == 0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } else {
                VB_vaccinated_layout.getChildren().clear();
                avu.forEach(vu -> {
                    try {
                        loadVaccinatedUserRow(vu, avu.indexOf(vu) % 2 == 0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
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

    private void loadVaccinatedUserRow(VaccinatedUser vu, boolean applyGrey) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(HubHomeController.class.getResource("fxml/hub_home_row.fxml"));

        HBox hBox = fxmlLoader.load();
        HubHomeItemRowController hirc = fxmlLoader.getController();
        hirc.setData(vu, applyGrey);
        VB_vaccinated_layout.getChildren().add(hBox);
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
    private void openSetting() {
        try {
            startSetting();

            int newSelectedImage = hubHomeSettingsController.getSelectedImage();
            if (newSelectedImage != hubImage) {
                hubImage = newSelectedImage;
                IV_hub.setImage(new Image(Objects.requireNonNull(UserType.class.getResourceAsStream("drawable/hospital_icon_" + hubImage + ".png"))));
                ServerReference.getServer().changeImageHub(hubImage, hubName);
            }
            if (hubHomeSettingsController.getDeleteAccSettings()){
                ServerReference.getServer().deleteHub(hubName);
                startLogin();
            }
        } catch (IOException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    private void startSetting() throws IOException {
        FXMLLoader loader = new FXMLLoader(HubHomeController.class.getResource("fxml/hub_home_settings.fxml"));
        Parent root = loader.load();
        hubHomeSettingsController = loader.getController();
        hubHomeSettingsController.setSelectedImage(hubImage);
        hubHomeSettingsController.setHubName(hubName);

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        stage.setResizable(false);
        stage.setTitle("Impostazioni");
        stage.getIcons().add(new Image(Objects.requireNonNull(UserType.class.getResourceAsStream("drawable/primula.png"))));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(this.stage);

        scene.setOnMousePressed(mouseEvent -> {
            xOffset = mouseEvent.getSceneX();
            yOffset = mouseEvent.getSceneY();
        });

        scene.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() - xOffset);
            stage.setY(mouseEvent.getScreenY() - yOffset);
        });

        stage.showAndWait();
    }

    @FXML
    private void logout() {
        if (logoutAlert()) {
            try {
                startLogin();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean logoutAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Sei sicuro di voler effettuare il logout?");
        alert.initStyle(StageStyle.TRANSPARENT);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);

        ButtonType buttonTypeOk = new ButtonType("OK");
        ButtonType buttonTypeCancel = new ButtonType("Annulla", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOk, buttonTypeCancel);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setPrefSize(255, 0);
        dialogPane.lookupButton(buttonTypeCancel).setId("btnCancel");
        dialogPane.getStylesheets().add(Objects.requireNonNull(HubHomeController.class.getResource("alert_error.css")).toExternalForm());
        dialogPane.getStyleClass().add("alert");

        Scene dialogScene = dialogPane.getScene();
        dialogScene.setFill(Color.TRANSPARENT);

        AtomicReference<Double> xOffset = new AtomicReference<>((double) 0);
        AtomicReference<Double> yOffset = new AtomicReference<>((double) 0);

        dialogScene.setOnMousePressed(mouseEvent -> {
            xOffset.set(mouseEvent.getSceneX());
            yOffset.set(mouseEvent.getSceneY());
        });

        dialogScene.setOnMouseDragged(mouseEvent -> {
            dialogScene.getWindow().setX(mouseEvent.getScreenX() - xOffset.get());
            dialogScene.getWindow().setY(mouseEvent.getScreenY() - yOffset.get());
        });

        Stage dialogStage = (Stage) dialogScene.getWindow();
        dialogStage.getIcons().add(new Image(Objects.requireNonNull(UserType.class.getResourceAsStream("drawable/primula.png"))));

        Optional<ButtonType> result = alert.showAndWait();

        return result.orElse(null) == buttonTypeOk;
    }

    private void startLogin() throws IOException {
        Scene scene = new Scene(FXMLLoader.load(Objects.requireNonNull(LoginMainController.class.getResource("fxml/login.fxml"))));
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        stage.setResizable(false);
        stage.setTitle("CVI");
        stage.getIcons().add(new Image(Objects.requireNonNull(UserType.class.getResourceAsStream("drawable/primula.png"))));
        this.stage.hide();
        stage.show();

        scene.setOnMousePressed(mouseEvent -> {
            xOffset = mouseEvent.getSceneX();
            yOffset = mouseEvent.getSceneY();
        });

        scene.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() - xOffset);
            stage.setY(mouseEvent.getScreenY() - yOffset);
        });
    }

    @FXML
    private void openAbout() {
        try {
            startAbout();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startAbout() throws IOException {
        Scene scene = new Scene(FXMLLoader.load(Objects.requireNonNull(HubHomeController.class.getResource("fxml/hub_home_about.fxml"))));
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        stage.setResizable(false);
        stage.setTitle("About");
        stage.getIcons().add(new Image(Objects.requireNonNull(UserType.class.getResourceAsStream("drawable/primula.png"))));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(this.stage);
        stage.show();

        scene.setOnMousePressed(mouseEvent -> {
            xOffset = mouseEvent.getSceneX();
            yOffset = mouseEvent.getSceneY();
        });

        scene.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() - xOffset);
            stage.setY(mouseEvent.getScreenY() - yOffset);
        });
    }

    @FXML
    private void openChart() {
        try {
            startChart();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startChart() throws IOException {
        FXMLLoader loader = new FXMLLoader(HubHomeController.class.getResource("fxml/hub_home_chart.fxml"));
        Parent root = loader.load();
        //HubHomeChartController hubHomeChartController = loader.getController();
        //hubHomeChartController.setData();

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Grafico vaccinazioni");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(this.stage);
        stage.show();
    }
    
    @FXML
    private void openRegisterVaccinatedUser(){
        try {
            startRegister();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void startRegister() throws IOException {
        FXMLLoader loader = new FXMLLoader(HubHomeController.class.getResource("fxml/hub_home_registration_new_vaccinated.fxml"));
        Parent root = loader.load();
        hubHomeSettingsController = loader.getController();
        hubHomeSettingsController.setHubName(hubName);

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        stage.setResizable(false);
        stage.setTitle("Registra nuovo vaccinato");
        stage.getIcons().add(new Image(Objects.requireNonNull(UserType.class.getResourceAsStream("drawable/primula.png"))));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(this.stage);

        scene.setOnMousePressed(mouseEvent -> {
            xOffset = mouseEvent.getSceneX();
            yOffset = mouseEvent.getSceneY();
        });

        scene.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() - xOffset);
            stage.setY(mouseEvent.getScreenY() - yOffset);
        });

        stage.showAndWait();
    }
}
