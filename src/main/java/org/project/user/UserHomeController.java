package org.project.user;

import com.jfoenix.controls.JFXComboBox;
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
import org.project.hub.HubHomeController;
import org.project.login.LoginMainController;
import org.project.models.Hub;
import org.project.models.User;
import org.project.server.Server;
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

/**
 * Questa classe gestisce tutti i componenti presenti nella home del
 * utente.
 * <br>
 * Viene gestito l'avvio di altri {@link Stage} ovvero:
 * <ul>
 * <li>Impostazioni ({@link UserHomeSettingsController})</li>
 * <li>About ({@link AboutController})</li>
 * <li>Info hub ({@link UserHomeInfoHubController})</li>
 * <li>Logout ({@link #startLogin()})</li>
 * <li>Chart ({@link #startChart()})</li>
 * </ul>
 *
 * @author Federico Mainini 740691 (VA)
 * @author Gianluca Latronico 739893 (VA)
 * @author Marc Alexander Orlando 741473 (VA)
 * @author Enrico Luigi Lamperti 740612 (VA)
 */
public class UserHomeController implements Initializable {

    /**
     * Array utilizzato per inserire i diversi filtri
     * nella combobox.
     */
    private final static String[] FILTER = {
            "NOME",
            "COMUNE",
            "TIPOLOGIA",
            "COMUNE-TIPOLOGIA",
            "NO FILTRO"
    };

    /**
     * AnchorPane esterno.
     */
    @FXML
    private AnchorPane AP_ext;

    /**
     * Immagine dell'utente.
     */
    @FXML
    private ImageView IV_user;

    /**
     * Label per il nome dell'utente.
     */
    @FXML
    private Label LB_user_name;

    /**
     * Label per il nickname dell'utente.
     */
    @FXML
    private Label LB_user_nickname;

    /**
     * Bottone per aprire le impostazioni.
     */
    @FXML
    private MFXButton BT_setting;

    /**
     * Bottone per effettuare il log-out.
     */
    @FXML
    private MFXButton BT_logout;

    /**
     * Bottone per aprire l'about.
     */
    @FXML
    private MFXButton BT_about;

    /**
     * Immagine che funge da quit dal programma.
     */
    @FXML
    private ImageView BT_quit;

    /**
     * Immagine che funge per minimizzare l'applicazione.
     */
    @FXML
    private ImageView BT_minimize;

    /**
     * Label per indicare il totale dei vaccinati.
     */
    @FXML
    private MFXLabel LB_total_vaccinated;

    /**
     * Label per indicare il totale dei vaccinati con prima dose.
     */
    @FXML
    private MFXLabel LB_vaccinated_first;

    /**
     * Label per indicare il totale dei vaccinati con seconda dose.
     */
    @FXML
    private MFXLabel LB_vaccinated_second;

    /**
     * Bottone che apre il grafico dell'andamento vaccinale.
     */
    @FXML
    private MFXButton BT_open_chart;

    /**
     * Field per cercare il centro vaccinale desiderato.
     */
    @FXML
    private MFXTextField TF_search_hub;

    /**
     * Immagine per poter aggiornare l'interfaccia.
     */
    @FXML
    private ImageView IV_refresh;

    /**
     * ComboBox utilizzata per selezionare il filtro desiderato.
     */
    @FXML
    private JFXComboBox<String> CB_filter;

    /**
     * VBox per contenere la lista di centri vaccinali.
     */
    @FXML
    private VBox VB_hub_layout;

    /**
     * Stage riferito a questo controller.
     */
    private Stage stage;

    /**
     * Immagine del utente.
     */
    private int userImage;

    /**
     * Email dell'utente.
     */
    private String email;

    /**
     * Utente.
     */
    private User us;

    /**
     * Lista di centri vaccinali.
     */
    private ArrayList<Hub> ahub;

    /**
     * Controller delle impostazioni.
     */
    private UserHomeSettingsController userHomeSettingsController;

    /**
     * Totali vaccinati.
     */
    private int[] vcn;

    /**
     * Utilizzato per settare l'email dell'utente.
     *
     * @param email email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Utilizzato per inizializzare l'interfaccia riempiendo
     * varie label tra cui: nome, indirizzo, totale vaccinati, totale
     * prima dose e totale seconda dose.
     * Serve inoltre ad eseguire la query per ottenere la lista completa
     * dei centri vaccinali presenti nel DB.
     *
     * @param url            url
     * @param resourceBundle resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            stage = (Stage) AP_ext.getScene().getWindow();

            if (!email.equals("Guest")) {
                try {
                    us = ServerReference.getServer().getUser(email);
                    LB_user_name.setText(us.getName() + " " + us.getSurname());
                    LB_user_nickname.setText(us.getNickname());
                    IV_user.setImage(new Image(Objects.requireNonNull(UserType.class.getResourceAsStream("drawable/user_icon_" + us.getImage() + ".png"))));
                } catch (RemoteException | NotBoundException e) {
                    e.printStackTrace();
                }
            } else {
                LB_user_name.setText("Ospite");
                LB_user_nickname.setText("");
                IV_user.setImage(new Image(Objects.requireNonNull(UserType.class.getResourceAsStream("drawable/user_icon_placeholder.png"))));
                BT_setting.setDisable(true);
            }

            CB_filter.getItems().addAll(FILTER);
            fillRow();

            try {
                vcn = ServerReference.getServer().getNumberVaccinated("CITTADINO");
                LB_total_vaccinated.setText(String.valueOf(vcn[0]));
                LB_vaccinated_first.setText(String.valueOf(vcn[1]));
                LB_vaccinated_second.setText(String.valueOf(vcn[2]));
            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
            }

            Tooltip tool = new Tooltip("Visualizza andamento vaccinazioni");
            tool.setShowDelay(new Duration(500));
            Tooltip.install(BT_open_chart, tool);
        });

        TF_search_hub.textProperty().addListener((observable, oldValue, newValue) -> {
            String value = newValue.strip();
            if (CB_filter.getValue() != null) {
                filterSelection(CB_filter.getValue(), value);
            } else {
                ArrayList<Hub> vuf = (ArrayList<Hub>) ahub.stream().filter(hub ->
                        StringUtils.containsIgnoreCase(hub.getNameHub() + " " + hub.getAddress().getCity() + " " + hub.getType(), (value)) ||
                                StringUtils.containsIgnoreCase(hub.getNameHub() + " " + hub.getType() + " " + hub.getAddress().getCity(), (value)) ||
                                StringUtils.containsIgnoreCase(hub.getAddress().getCity() + " " + hub.getNameHub() + " " + hub.getType(), (value)) ||
                                StringUtils.containsIgnoreCase(hub.getAddress().getCity() + " " + hub.getType() + " " + hub.getNameHub(), (value)) ||
                                StringUtils.containsIgnoreCase(hub.getType() + " " + hub.getNameHub() + " " + hub.getAddress().getCity(), (value)) ||
                                StringUtils.containsIgnoreCase(hub.getType() + " " + hub.getAddress().getCity() + " " + hub.getNameHub(), (value))).collect(Collectors.toList());
                VB_hub_layout.getChildren().clear();

                vuf.forEach(vu -> {
                    try {
                        loadHubRow(vu, ahub.indexOf(vu) % 2 == 0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }

    /**
     * Utilizzato per riempire le righe del {@link #VB_hub_layout}.
     *
     * @see Server#fetchAllHub()
     */
    private void fillRow() {
        try {
            ahub = ServerReference.getServer().fetchAllHub();
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
    }

    /**
     * Utilizzato per ridurre la finestra ad icona.
     */
    @FXML
    private void minimize() {
        stage.setIconified(true);
    }

    /**
     * Utilizzato per scurire l'icona minimize
     * quando il cursore entra.
     */
    @FXML
    private void darkStyleMinimize() {
        setDarkHover(BT_minimize);
    }

    /**
     * Utilizzato per riportare l'immagine alla normalita'
     * una volta uscito il cursore.
     */
    @FXML
    private void restoreStyleMinimize() {
        resetDarkExit(BT_minimize);
    }

    /**
     * Quando premuto il tasto exit chiude il programma.
     */
    @FXML
    private void quit() {
        System.exit(0);
    }

    /**
     * Utilizzato per scurire l'icona quit
     * quando il cursore entra.
     */
    @FXML
    private void darkStyleQuit() {
        setDarkHover(BT_quit);
    }

    /**
     * Utilizzato per riportare l'immagine alla normalita'
     * una volta uscito il cursore.
     */
    @FXML
    private void restoreStyleQuit() {
        resetDarkExit(BT_quit);
    }

    /**
     * Utilizzato da certe immagini per scurire l'interno.
     *
     * @param iv ImageView che si vuole scurire
     */
    private void setDarkHover(@NotNull ImageView iv) {
        iv.setEffect(new InnerShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, 0.5), 10, 0, 5, 5));
    }

    /**
     * Utilizzato da certe immagini per portare alla normalita'
     * l'effetto interno di scurimento.
     *
     * @param iv ImageView che si vuole portare alla normalita'
     */
    private void resetDarkExit(@NotNull ImageView iv) {
        iv.setEffect(null);
    }

    /**
     * Utilizzato per ricaricare la lista dei centri vaccinali,
     * andando a richiamare il metodo.
     */
    @FXML
    private void refreshHubList() {
        RotateTransition rt = new RotateTransition(Duration.seconds(1), IV_refresh);
        rt.setFromAngle(360);
        rt.setToAngle(0);
        rt.setCycleCount(2);
        rt.setInterpolator(Interpolator.LINEAR);
        rt.play();

        VB_hub_layout.getChildren().clear();
        fillRow();
    }

    /**
     * Utilizzato per scurire l'icona refresh
     * quando il cursore entra.
     */
    @FXML
    private void darkStyleRefresh() {
        setDarkHover(IV_refresh);
    }

    /**
     * Utilizzato per riportare l'immagine alla normalita'
     * una volta uscito il cursore.
     */
    @FXML
    private void restoreStyleRefresh() {
        resetDarkExit(IV_refresh);
    }

    /**
     * Utilizzato per effettuare il logout e tornare alla schermata di login.
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
     * Utilizzato per chiedere all'utente attraverso un Alert se e' sicuro di voler
     * effettuare il logout.
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
     * Utilizzato per mostrare la schermata di login.
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
     * Utilizzato per caricare nel VB_box le righe con i dati dei centri
     * vaccinali registrati.
     *
     * @param hub       info per settare i campi presenti nella riga
     * @param applyGrey usato per colorare le righe
     * @throws IOException IOException
     */
    private void loadHubRow(Hub hub, boolean applyGrey) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(UserHomeController.class.getResource("fxml/user_home_row.fxml"));

        HBox hBox = fxmlLoader.load();
        UserHomeItemRowController uhirc = fxmlLoader.getController();
        if (!email.equals("Guest")) {
            uhirc.setData(hub, us, applyGrey);
        } else {
            User guest = new User();
            guest.setName("Guest");
            uhirc.setData(hub, guest, applyGrey);
        }
        VB_hub_layout.getChildren().add(hBox);
    }

    /**
     * Utilizzato per mostrare la schermata dell'about
     * che richiama {@link #startAbout()}.
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
     * Utilizzato per mostrare l'about.
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
     * che richiama {@link #startChart()}.
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
     * Utilizzato per mostrare il grafico dell'andamento vaccinale.
     *
     * @throws IOException IOException
     */
    private void startChart() throws IOException {
        FXMLLoader loader = new FXMLLoader(ChartController.class.getResource("fxml/chart.fxml"));
        Parent root = loader.load();
        ChartController chartController = loader.getController();
        chartController.setData(vcn);
        chartController.setUserType(UserType.USER);

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
     * Si occupa dell'apertura delle impostazioni tramite {@link #startSetting()},
     * se alla chiusura dello stage delle impostazioni risulta confermata
     * l'eliminazione viene eliminato l'account da DB e viene avviata la login
     * tramite {@link #startLogin()}.
     *
     * @see UserHomeSettingsController#getDeleteAccSettings()
     * @see org.project.server.Server#deleteAccount(String, String)
     */
    @FXML
    private void openSetting() {
        try {
            startSetting();

            int newSelectedImage = userHomeSettingsController.getSelectedImage();
            if (newSelectedImage != userImage) {
                userImage = newSelectedImage;
                IV_user.setImage(new Image(Objects.requireNonNull(UserType.class.getResourceAsStream("drawable/user_icon_" + userImage + ".png"))));
                ServerReference.getServer().changeImage(userImage, "", us.getFiscalCode());
            }

            if (userHomeSettingsController.getDeleteAccSettings()) {
                ServerReference.getServer().deleteAccount("", email);
                startLogin();
            }
        } catch (IOException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Chiamato dal metodo {@link #openSetting()}, si occupa
     * dell'apertura dello stage gestito da {@link UserHomeSettingsController}.
     *
     * @throws IOException IOException
     */
    private void startSetting() throws IOException {
        FXMLLoader loader = new FXMLLoader(UserHomeSettingsController.class.getResource("fxml/user_home_settings.fxml"));
        Parent root = loader.load();
        userHomeSettingsController = loader.getController();
        userHomeSettingsController.setSelectedImage(userImage);
        userHomeSettingsController.setEmail(email);

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
     * Utilizzato nella {@link #initialize(URL, ResourceBundle)}, viene
     * usato per effettuare il filtro sulla lista dei centri vaccinali,
     * va a ricaricare la lista nella {@link #VB_hub_layout}.
     *
     * @param CBvalue valore selezionato nella {@link #CB_filter}
     * @param TFvalue testo inserito dal cittadino
     */
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
                        StringUtils.containsIgnoreCase(hub.getAddress().getCity() + " " + hub.getType(), (TFvalue)) ||
                                StringUtils.containsIgnoreCase(hub.getType() + " " + hub.getAddress().getCity(), (TFvalue))).collect(Collectors.toList());
                VB_hub_layout.getChildren().clear();
                break;

            default:
                vuf = (ArrayList<Hub>) ahub.stream().filter(hub ->
                        StringUtils.containsIgnoreCase(hub.getNameHub() + " " + hub.getAddress().getCity() + " " + hub.getType(), (TFvalue)) ||
                                StringUtils.containsIgnoreCase(hub.getNameHub() + " " + hub.getType() + " " + hub.getAddress().getCity(), (TFvalue)) ||
                                StringUtils.containsIgnoreCase(hub.getAddress().getCity() + " " + hub.getNameHub() + " " + hub.getType(), (TFvalue)) ||
                                StringUtils.containsIgnoreCase(hub.getAddress().getCity() + " " + hub.getType() + " " + hub.getNameHub(), (TFvalue)) ||
                                StringUtils.containsIgnoreCase(hub.getType() + " " + hub.getNameHub() + hub.getAddress().getCity(), (TFvalue)) ||
                                StringUtils.containsIgnoreCase(hub.getType() + " " + hub.getAddress().getCity() + " " + hub.getNameHub(), (TFvalue))).collect(Collectors.toList());
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
