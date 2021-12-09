package org.project.hub;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXLabel;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
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
import javafx.util.Duration;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.project.UserType;
import org.project.login.LoginMainController;
import org.project.models.Hub;
import org.project.models.User;
import org.project.server.ServerReference;
import org.project.shared.AboutController;
import org.project.shared.ChartController;
import org.project.user.UserHomeSettingsController;

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

/**
 * Questa classe gestisce tutti i componenti presenti nella home del
 * centro vaccinale.
 * <br>
 * Viene gestito l'avvio di altri {@link Stage} ovvero:
 * <ul>
 * <li>Impostazioni ({@link #startSetting()})</li>
 * <li>About ({@link #startAbout()})</li>
 * <li>Logout ({@link #startLogin()})</li>
 * <li>Chart ({@link #startChart()})</li>
 * <li>Registra nuovo vaccinato ({@link #startRegister()})</li>
 * <li>Visualizza info vaccinato ({@link #openRegisterVaccinatedUser()})</li>
 * </ul>
 *
 * @author Federico Mainini 740691 (VA)
 * @author Gianluca Latronico 739893 (VA)
 * @author Marc Alexander Orlando 741473 (VA)
 * @author Enrico Luigi Lamperti 740612 (VA)
 */
public class HubHomeController implements Initializable {

    /**
     * AnchorPane esterno
     */
    @FXML
    private AnchorPane AP_ext;

    /**
     * Immagine del centro vaccinale
     */
    @FXML
    private ImageView IV_hub;

    /**
     * Label nome del centro vaccinale
     */
    @FXML
    private Label LB_hub_name;

    /**
     * Label indirizzo del centro vaccinale
     */
    @FXML
    private Label LB_hub_address;

    /**
     * Bottone per aprire le impostazioni
     */
    @FXML
    private MFXButton BT_setting;

    /**
     * Bottone per aprire la schermata about
     */
    @FXML
    private MFXButton BT_about;

    /**
     * Bottone per eseguire il logout
     */
    @FXML
    private MFXButton BT_logout;

    /**
     * Immagine che funge da quit dall'applicazione
     */
    @FXML
    private ImageView BT_quit;

    /**
     * Immagine che funge per minimizzare l'applicazione
     */
    @FXML
    private ImageView BT_minimize;

    /**
     * Label per indicare il totale dei vaccinati
     */
    @FXML
    private MFXLabel LB_total_vaccinated;

    /**
     * Label per indicare il totale dei vaccinati presso uno specifico centro vaccinale
     */
    @FXML
    private MFXLabel LB_total_vaccinated_center;

    /**
     * Label per indicare il totale dei vaccinati con prima dose
     */
    @FXML
    private MFXLabel LB_vaccinated_first;

    /**
     * Label per indicare il totale dei vaccinati con seconda dose
     */
    @FXML
    private MFXLabel LB_vaccinated_second;

    /**
     * Bottone che apre il grafico dell'andamento vaccinale
     */
    @FXML
    private MFXButton BT_open_chart;

    /**
     * Immagine utilizzata per ricaricare l'elenco dei vaccinati
     */
    @FXML
    private ImageView IV_refresh;

    /**
     * Field adibito alla ricerca tra i cittadini vaccinati
     */
    @FXML
    private MFXTextField TF_search_citizen;

    /**
     * Bottone per registrare nuovi vaccinati
     */
    @FXML
    private MFXButton BT_registration_citizen;

    /**
     * VBox utilizzata per caricare tutti i cittadini vaccinati
     */
    @FXML
    private VBox VB_vaccinated_layout;

    /**
     * Stage riferito a questo controller
     */
    private Stage stage;

    /**
     * Immagine selezionata per questo centro vaccinale
     */
    private int hubImage;

    /**
     * Stringa utilizzata per settare il nome del centro vaccinale
     */
    private String hubName;

    /**
     * Array usato per caricare tutti gli utenti vaccinati presi dal DB
     */
    private ArrayList<User> avu;

    /**
     * Controller utilizzato per passare dati all'interfaccia delle impostazioni
     */
    private HubHomeSettingsController hubHomeSettingsController;

    /**
     * Array utilizzato per monitorare l'andamento delle vaccinazioni
     */
    private int[] vcn;

    /**
     * Utilizzato per settare il nome del centro
     * vaccinale in questo controller
     *
     * @param hubName nome del centro vaccinale
     */
    public void setNameHub(String hubName) {
        this.hubName = hubName;
    }

    /**
     * Utilizzato per inizializzare l'interfaccia riempiendo
     * varie label tra cui: nome, indirizzo, totale vaccinati, totale
     * prima dose, totale seconda dose, vaccinati presso il centro.
     * Serve inoltre ad eseguire la query per ottenere la lista completa
     * dei vaccinati presso il centro.
     *
     * @param url            url
     * @param resourceBundle resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            stage = (Stage) AP_ext.getScene().getWindow();

            LB_hub_name.setText(hubName);

            try {
                vcn = ServerReference.getServer().getNumberVaccinated(hubName);
                LB_total_vaccinated.setText(String.valueOf(vcn[0]));
                LB_vaccinated_first.setText(String.valueOf(vcn[1]));
                LB_vaccinated_second.setText(String.valueOf(vcn[2]));
                LB_total_vaccinated_center.setText(String.valueOf(vcn[3]));
            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
            }

            try {
                Hub hub = ServerReference.getServer().getHub(hubName);
                hubImage = hub.getImage();
                IV_hub.setImage(new Image(Objects.requireNonNull(UserType.class.getResourceAsStream("drawable/hospital_icon_" + hubImage + ".png"))));
                LB_hub_address.setText(hub.getAddress().toStringCustom());
            } catch (NotBoundException | RemoteException e) {
                e.printStackTrace();
            }

            getListUser();

            Tooltip tool = new Tooltip("Visualizza andamento vaccinazioni");
            tool.setShowDelay(new Duration(500));
            Tooltip.install(BT_open_chart, tool);

            Tooltip tool1 = new Tooltip("Registra nuovo vaccinato");
            tool1.setShowDelay(new Duration(500));
            Tooltip.install(BT_registration_citizen, tool1);
        });

        TF_search_citizen.textProperty().addListener((observable, oldValue, newValue) -> {
            String value = newValue.strip();
            if (!value.equals("")) {
                ArrayList<User> vuf = (ArrayList<User>) avu.stream().filter(vu ->
                        StringUtils.containsIgnoreCase(vu.getSurname() + " " + vu.getName() + " " + vu.getNickname(), (value)) ||
                                StringUtils.containsIgnoreCase(vu.getSurname() + " " + vu.getNickname() + " " + vu.getName(), (value)) ||
                                StringUtils.containsIgnoreCase(vu.getName() + " " + vu.getSurname() + " " + vu.getNickname(), (value)) ||
                                StringUtils.containsIgnoreCase(vu.getName() + " " + vu.getNickname() + " " + vu.getSurname(), (value)) ||
                                StringUtils.containsIgnoreCase(vu.getNickname() + " " + vu.getSurname() + " " + vu.getName(), (value)) ||
                                StringUtils.containsIgnoreCase(vu.getNickname() + " " + vu.getName() + " " + vu.getSurname(), (value))).collect(Collectors.toList());
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

    /**
     * Utilizzato per caricare la lista degli utenti vaccinati
     */
    private void getListUser() {
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
    }

    /**
     * Utilizzato per caricare nel VB_box le righe con i dati dei cittadini vaccinati
     *
     * @param vu        info per settare i campi presenti nella riga
     * @param applyGrey usato per colorare le righe
     * @throws IOException IOException
     */
    private void loadVaccinatedUserRow(User vu, boolean applyGrey) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(HubHomeController.class.getResource("fxml/hub_home_row.fxml"));

        HBox hBox = fxmlLoader.load();
        HubHomeItemRowController hirc = fxmlLoader.getController();
        hirc.setData(vu, applyGrey);
        hirc.setHubName(hubName);
        VB_vaccinated_layout.getChildren().add(hBox);
    }

    /**
     * Utilizzato per ridurre la finestra ad icona
     */
    @FXML
    private void minimize() {
        stage.setIconified(true);
    }

    /**
     * Utilizzato per scurire l'icona minimize
     * quando il cursore entra
     */
    @FXML
    private void darkStyleMinimize() {
        setDarkHover(BT_minimize);
    }

    /**
     * Utilizzato per riportare l'immagine alla normalità
     * una volta uscito il cursore
     */
    @FXML
    private void restoreStyleMinimize() {
        resetDarkExit(BT_minimize);
    }

    /**
     * Quando premuto, il tasto exit chiude il programma
     */
    @FXML
    private void quit() {
        System.exit(0);
    }

    /**
     * Utilizzato per scurire l'icona quit
     * quando il cursore entra
     */
    @FXML
    private void darkStyleQuit() {
        setDarkHover(BT_quit);
    }

    /**
     * Utilizzato per riportare l'immagine alla normalità
     * una volta uscito il cursore
     */
    @FXML
    private void restoreStyleQuit() {
        resetDarkExit(BT_quit);
    }

    /**
     * Utilizzato da certe immagini per scurire l'interno
     *
     * @param iv ImageView che si vuole scurire
     */
    private void setDarkHover(@NotNull ImageView iv) {
        iv.setEffect(new InnerShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, 0.5), 10, 0, 5, 5));
    }

    /**
     * Utilizzato da certe immagini per portare alla normalità
     * l'effetto interno di scurimento
     *
     * @param iv ImageView che si vuole portare alla normalità
     */
    private void resetDarkExit(@NotNull ImageView iv) {
        iv.setEffect(null);
    }

    /**
     * Utilizzato per ricaricare la lista dei vaccinati, andando a richiamare il metodo
     */
    @FXML
    private void refreshVaccinatedList() {
        RotateTransition rt = new RotateTransition(Duration.seconds(1), IV_refresh);
        rt.setFromAngle(360);
        rt.setToAngle(0);
        rt.setCycleCount(2);
        rt.setInterpolator(Interpolator.LINEAR);
        rt.play();

        VB_vaccinated_layout.getChildren().clear();
        TF_search_citizen.setText("");
        getListUser();
    }

    /**
     * Utilizzato per scurire l'icona refresh
     * quando il cursore entra
     */
    @FXML
    private void darkStyleRefresh() {
        setDarkHover(IV_refresh);
    }

    /**
     * Utilizzato per riportare l'immagine alla normalità
     * una volta uscito il cursore
     */
    @FXML
    private void restoreStyleRefresh() {
        resetDarkExit(IV_refresh);
    }

    /**
     * Si occupa dell'apertura delle impostazioni tramite {@link #startSetting()},
     * se alla chiusura dello stage delle impostazioni risulta confermata
     * l'eliminazione viene eliminato l'account da DB e viene avviata la login
     * tramite {@link #startLogin()}
     *
     * @see HubHomeSettingsController#getDeleteAccSettings()
     * @see org.project.server.Server#deleteAccount(String, String)
     */
    @FXML
    private void openSetting() {
        try {
            startSetting();

            int newSelectedImage = hubHomeSettingsController.getSelectedImage();
            if (newSelectedImage != hubImage) {
                hubImage = newSelectedImage;
                IV_hub.setImage(new Image(Objects.requireNonNull(UserType.class.getResourceAsStream("drawable/hospital_icon_" + hubImage + ".png"))));
                ServerReference.getServer().changeImage(hubImage, hubName, "");
            }

            if (hubHomeSettingsController.getDeleteAccSettings()) {
                ServerReference.getServer().deleteAccount(hubName, "");
                startLogin();
            }
        } catch (IOException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Chiamato dal metodo {@link #openSetting()}, si occupa
     * dell'apertura dello stage gestito da {@link UserHomeSettingsController}
     *
     * @throws IOException IOException
     */
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

    /**
     * Utilizzato per effettuare il logout e tornare alla schermata di login
     */
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

    /**
     * Utilizzato per chiedere all'utente attraverso un Alert se è sicuro di voler
     * effettuare il logout
     *
     * @return true se l'utente clicca su ok
     */
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

    /**
     * Utilizzato per mostrare la schermata di login
     *
     * @throws IOException IOException
     */
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

        stage.show();
    }

    /**
     * Utilizzato per mostrare la schermata dell'about
     * che richiama {@link #startAbout()}
     */
    @FXML
    private void openAbout() {
        try {
            startAbout();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Utilizzato per mostrare l'about
     *
     * @throws IOException IOException
     */
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

        stage.show();
    }

    /**
     * Utilizzato per mostrare la schermata del chart
     * che richiama {@link #startChart()}
     */
    @FXML
    private void openChart() {
        try {
            startChart();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Utilizzato per mostrare il grafico dell'andamento vaccinale
     *
     * @throws IOException IOException
     */
    private void startChart() throws IOException {
        FXMLLoader loader = new FXMLLoader(ChartController.class.getResource("fxml/chart.fxml"));
        Parent root = loader.load();
        ChartController chartController = loader.getController();
        chartController.setData(vcn);
        chartController.setUserType(UserType.HUB);

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        stage.setResizable(false);
        stage.setTitle("Andamento vaccinazioni");
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

        stage.show();
    }

    /**
     * Utilizzato per mostrare la schermata per registrare un nuovo vaccinato
     */
    @FXML
    private void openRegisterVaccinatedUser() {
        try {
            startRegister();
            VB_vaccinated_layout.getChildren().clear();
            initialize(null, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Avvia la schermata di registrazione di un nuovo
     * vaccinato
     *
     * @throws IOException IOException
     */
    private void startRegister() throws IOException {
        FXMLLoader loader = new FXMLLoader(HubHomeController.class.getResource("fxml/hub_home_registration_new_vaccinated.fxml"));
        Parent root = loader.load();
        HubHomeRegistrationNewVaccinatedController hubHomeRegistrationNewVaccinatedController = loader.getController();
        hubHomeRegistrationNewVaccinatedController.setHubName(hubName);

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
