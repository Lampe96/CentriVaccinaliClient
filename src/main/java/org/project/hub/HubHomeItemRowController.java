package org.project.hub;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;
import org.project.UserType;
import org.project.models.User;
import org.project.server.ServerReference;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Questa classe gestisce le funzionalita' della row
 * presente nella home dei centri vaccinali
 *
 * @author Federico Mainini 740691 (VA)
 * @author Gianluca Latronico 739893 (VA)
 * @author Marc Alexander Orlando 741473 (VA)
 * @author Enrico Luigi Lamperti 740612 (VA)
 */
public class HubHomeItemRowController implements Initializable {

    /**
     * HBox esterno
     */
    @FXML
    private HBox HB_ext;

    /**
     * Label per il cognome del cittadino
     */
    @FXML
    private Label LB_surname;

    /**
     * Label per il nome del cittadino
     */
    @FXML
    private Label LB_name;

    /**
     * Label per il nickname del cittadino
     */
    @FXML
    private Label LB_nickname;

    /**
     * Immagine per identificare il numero di dosi ricevute
     */
    @FXML
    private ImageView IV_dose;

    /**
     * Immagine per segnalare la presenza di eventi avversi
     */
    @FXML
    private ImageView IV_event;

    /**
     * Stage riferito a questo controller
     */
    private Stage stage;

    /**
     * Nome del centro vaccinale
     */
    private String hubName;

    /**
     * Informazioni dell'utente che sto visualizzando
     */
    private User vu;

    /**
     * Utilizzato per impostare le informazioni dei singoli cittadini,
     * per gestire i tooltip, in base al numero di dosi
     * ricevute e gli eventuali eventi avversi
     * 
     * @param vaccinatedUser informazioni del cittadino
     * @param applyGrey per colorare la riga
     */
    void setData(@NotNull User vaccinatedUser, boolean applyGrey) {
        vu = vaccinatedUser;
        LB_name.setText(vaccinatedUser.getName());
        LB_surname.setText(vaccinatedUser.getSurname());
        LB_nickname.setText(vaccinatedUser.getNickname());

        if (vaccinatedUser.getDose() == 1) {
            IV_dose.setImage(new Image(String.valueOf(UserType.class.getResource("drawable/number_1.png"))));
            Tooltip tool = new Tooltip("Registra seconda dose");
            tool.setShowDelay(new Duration(500));
            Tooltip.install(HB_ext, tool);
        } else {
            IV_dose.setImage(new Image(String.valueOf(UserType.class.getResource("drawable/number_2.png"))));
            Tooltip tool1 = new Tooltip("Visualizza info cittadino");
            tool1.setShowDelay(new Duration(500));
            Tooltip.install(HB_ext, tool1);
        }

        if (vaccinatedUser.getEvent() == null) {
            IV_event.setVisible(false);
        }

        if (applyGrey) {
            HB_ext.setBackground(new Background(new BackgroundFill(Paint.valueOf("#eeeeee"), CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }

    /**
     * Utilizzato per settare il nome del centro vaccinale passato dal controller
     *
     * @param hubName nome del centro vaccinale
     */
    void setHubName(String hubName) {
        this.hubName = hubName;
    }

    /**
        * Utilizzato per inizializzare l'interfaccia 
        * prendendo la scena
        *
        * @param url            url
        * @param resourceBundle resourceBundle
        */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> stage = (Stage) HB_ext.getScene().getWindow());
    }

    /**
     *  Utilizzato per scurire la row selezionata
     */
    @FXML
    private void darkStyleRow() {
        HB_ext.setEffect(new InnerShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, 0.5), 10, 0, 5, 5));
    }

    /**
     * Utilizzato per riportare alla normalità l'effeto applicato
     * precedentemente alla row selezionata 
     */
    @FXML
    private void restoreStyleRow() {
        HB_ext.setEffect(null);
    }

    /**
     * Utilizzato per visualizzare la schermata di informazioni 
     * del cittadino ed effettuare la seconda vaccinazione  
     */
    @FXML
    private void openInfoVaccinated() {
        try {
            FXMLLoader loader = new FXMLLoader(HubHomeInfoUserController.class.getResource("fxml/hub_home_info_user.fxml"));
            Parent root = loader.load();
            HubHomeInfoUserController hubHomeInfoUserController = loader.getController();
            User vuExtra = ServerReference.getServer().fetchHubVaccinatedInfo(vu.getId(), hubName);

            User vuComplete = new User();

            vuComplete.setName(vu.getName());
            vuComplete.setSurname(vu.getSurname());
            vuComplete.setNickname(vu.getNickname());
            vuComplete.setEvent(vu.getEvent());
            vuComplete.setId(vuExtra.getId());
            vuComplete.setHubName(vuExtra.getHubName());
            vuComplete.setFiscalCode(vuExtra.getFiscalCode());
            vuComplete.setVaccineDate(vuExtra.getVaccineDate());
            vuComplete.setVaccineType(vuExtra.getVaccineType());
            vuComplete.setDose(vuExtra.getDose());

            hubHomeInfoUserController.setVaccinatedUserInfo(vuComplete);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            scene.setFill(Color.TRANSPARENT);
            stage.setResizable(false);
            stage.setTitle("Visualizza info utente");
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
        } catch (IOException | NotBoundException e) {
            e.printStackTrace();
        }
    }
}
