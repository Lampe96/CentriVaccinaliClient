package org.project.user;

import com.jfoenix.controls.JFXComboBox;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXLabel;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.project.UserType;
import org.project.hub.HubHomeController;
import org.project.login.LoginMainController;
import org.project.models.Hub;
import org.project.models.User;
import org.project.server.ServerReference;
import org.project.shared.AboutController;
import org.project.shared.ChartController;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class UserHomeController implements Initializable {

    private final static String[] FILTER = {
            "NOME",
            "COMUNE",
            "TIPOLOGIA",
            "COMUNE-TIPOLOGIA",
            "NO FILTRO"
    };

    @FXML
    private AnchorPane AP_ext;

    @FXML
    private ImageView IV_user;

    @FXML
    private Label LB_user_name;

    @FXML
    private Label LB_user_nickname;

    @FXML
    private MFXButton BT_setting;

    @FXML
    private MFXButton BT_logout;

    @FXML
    private MFXButton BT_about;

    @FXML
    private ImageView BT_quit;

    @FXML
    private ImageView BT_minimize;

    @FXML
    private MFXLabel LB_total_vaccinated;

    @FXML
    private MFXLabel LB_vaccinated_first;

    @FXML
    private MFXLabel LB_vaccinated_second;

    @FXML
    private MFXButton BT_open_chart;

    @FXML
    private MFXTextField TF_search_hub;

    @FXML
    private JFXComboBox<String> CB_filter;

    @FXML
    private VBox VB_hub_layout;

    private Stage stage;
    private double xOffset, yOffset;
    private ArrayList<Hub> ahub;
    private int[] vcn;
    private String email;

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            stage = (Stage) AP_ext.getScene().getWindow();

            try {
                User us = ServerReference.getServer().getUser(email);
                LB_user_name.setText(us.getName() + " " + us.getSurname());
                LB_user_nickname.setText(us.getNickname());
            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
            }

            CB_filter.getItems().addAll(FILTER);

            try {
                ahub = ServerReference.getServer().fetchAllHub();
                System.out.println(ahub);
                ahub.forEach(hub -> {
                    try {
                        loadHubRow(hub, ahub.indexOf(hub) % 2 == 0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
            }

            try {
                vcn = ServerReference.getServer().getNumberVaccinated("CITTADINO");
                LB_total_vaccinated.setText(String.valueOf(vcn[0]));
                LB_vaccinated_first.setText(String.valueOf(vcn[1]));
                LB_vaccinated_second.setText(String.valueOf(vcn[2]));
            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
            }
        });

        TF_search_hub.textProperty().addListener((observable, oldValue, newValue) -> {
            String value = newValue.strip().replaceAll("\\s+", "");
            filterSelection(CB_filter.getValue(), value);
        });
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
        this.stage.close();
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

    private void loadHubRow(Hub hub, boolean applyGrey) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(UserHomeController.class.getResource("fxml/user_home_row.fxml"));

        HBox hBox = fxmlLoader.load();
        UserHomeItemRowController uhirc = fxmlLoader.getController();
        uhirc.setData(hub, applyGrey);
        VB_hub_layout.getChildren().add(hBox);
    }

    @FXML
    void openAbout() {
        try {
            startAbout();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startAbout() throws IOException {
        Scene scene = new Scene(FXMLLoader.load(Objects.requireNonNull(AboutController.class.getResource("fxml/about.fxml"))));
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        stage.setResizable(false);
        stage.setTitle("About");
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

        stage.show();
    }

    @FXML
    void openChart() {
        try {
            startChart();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startChart() throws IOException {
        FXMLLoader loader = new FXMLLoader(ChartController.class.getResource("fxml/chart.fxml"));
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
    void openSetting() {
        try {
            startSetting();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startSetting() throws IOException {
        FXMLLoader loader = new FXMLLoader(HubHomeController.class.getResource("fxml/hub_home_settings.fxml"));
        Parent root = loader.load();
//        hubHomeSettingsController = loader.getController();
//        hubHomeSettingsController.setSelectedImage(hubImage);
//        hubHomeSettingsController.setHubName(hubName);

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

    private void filterSelection(@NotNull String CBvalue, String TFvalue) {
        ArrayList<Hub> vuf;
        switch (CBvalue) {
            case "NOME":
                vuf = (ArrayList<Hub>) ahub.stream().filter(hub ->
                        StringUtils.containsIgnoreCase(hub.getNameHub(), (TFvalue))).collect(Collectors.toList());
                VB_hub_layout.getChildren().clear();
                break;

            case "COMUNE":
                vuf = (ArrayList<Hub>) ahub.stream().filter(hub ->
                        StringUtils.containsIgnoreCase(hub.getAddress().getCity(), (TFvalue))).collect(Collectors.toList());
                VB_hub_layout.getChildren().clear();
                break;

            case "TIPOLOGIA":
                vuf = (ArrayList<Hub>) ahub.stream().filter(hub ->
                        StringUtils.containsIgnoreCase(hub.getType(), (TFvalue))).collect(Collectors.toList());
                VB_hub_layout.getChildren().clear();
                break;

            case "COMUNE-TIPOLOGIA":
                vuf = (ArrayList<Hub>) ahub.stream().filter(hub ->
                        StringUtils.containsIgnoreCase(hub.getAddress().getCity() + hub.getType(), (TFvalue)) ||
                                StringUtils.containsIgnoreCase(hub.getType() + hub.getAddress().getCity(), (TFvalue))).collect(Collectors.toList());
                VB_hub_layout.getChildren().clear();
                break;

            default:
                vuf = (ArrayList<Hub>) ahub.stream().filter(hub ->
                        StringUtils.containsIgnoreCase(hub.getNameHub() + hub.getAddress().getCity() + hub.getType(), (TFvalue)) ||
                                StringUtils.containsIgnoreCase(hub.getNameHub() + hub.getType() + hub.getAddress().getCity(), (TFvalue)) ||
                                StringUtils.containsIgnoreCase(hub.getAddress().getCity() + hub.getNameHub() + hub.getType(), (TFvalue)) ||
                                StringUtils.containsIgnoreCase(hub.getAddress().getCity() + hub.getType() + hub.getNameHub(), (TFvalue)) ||
                                StringUtils.containsIgnoreCase(hub.getType() + hub.getNameHub() + hub.getAddress().getCity(), (TFvalue)) ||
                                StringUtils.containsIgnoreCase(hub.getType() + hub.getAddress().getCity() + hub.getNameHub(), (TFvalue))).collect(Collectors.toList());
                VB_hub_layout.getChildren().clear();
                break;
        }

        vuf.forEach(vu -> {
            try {
                loadHubRow(vu, ahub.indexOf(vu) % 2 == 0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
