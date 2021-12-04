package org.project.hub;

import com.password4j.Password;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;
import org.project.UserType;
import org.project.server.ServerReference;
import org.project.utils.RegistrationUtil;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Questa classe gestisce tutti i componenti presenti nella
 * schermata delle impostazioni, eseguibile dal bottone presente 
 * nella classe {@link HubHomeController}. Gestisce l'eliminazione 
 * dell'account tramite il bottone che richiama la classe 
 * {@link HubHomeSettingsDeleteController}
 * 
 * @author Federico Mainini 740691 (VA)
 * @author Gianluca Latronico 739893 (VA)
 * @author Marc Alexander Orlando 741473 (VA)
 * @author Enrico Luigi Lamperti 740612 (VA)
 */
public class HubHomeSettingsController implements Initializable {

    /**
     * Inzializzazione del timer
     */
    private final Timer TIMER = new Timer();

    /**
     * AnchorPane esterno
     */
    @FXML
    public AnchorPane AP_ext;

    /**
     * Immagine che funge da quit dall'applicazione
     */
    @FXML
    private ImageView BT_quit;

    /**
     * Immagine profilo 1
     */
    @FXML
    private ImageView IV_one;

    /**
     * Immagine profilo 2
     */
    @FXML
    private ImageView IV_two;

    /**
     * Immagine profilo 3
     */
    @FXML
    private ImageView IV_three;

    /**
     * Immagine profilo 4
     */
    @FXML
    private ImageView IV_four;

    /**
     * Immagine profilo 5
     */
    @FXML
    private ImageView IV_five;

    /**
     * Immagine profilo 6
     */
    @FXML
    private ImageView IV_six;

    /**
     * Immagine profilo 7
     */
    @FXML
    private ImageView IV_seven;

    /**
     * Immagine profilo 8
     */
    @FXML
    private ImageView IV_eight;


    /**
     * Immagine profilo 9
     */
    @FXML
    private ImageView IV_nine;

    /**
     * Bottone per aprire lo satge del {@link HubHomeSettingsDeleteController }
     */
    @FXML
    private MFXButton BT_delete;

    /**
     * Field utilizzato per inserire la vecchia password per essere verificata
     */
    @FXML
    private MFXPasswordField TF_old_password;

    /**
     * Label di errore password
     */
    @FXML
    private Label LB_error_password;

    /**
     * Label per la conferma dell'avvenuto
     * cambio della password
     */
    @FXML
    private Label LB_success_change;

    /**
     * Field utilizzato per inserire la nuova password
     */
    @FXML
    private MFXPasswordField TF_new_password;

    /**
     * Field utilizzato per inserire la conferma della nuova password
     */
    @FXML
    private MFXPasswordField TF_confirm_password;

    /**
     * Bottone per confermare la vecchia password
     */
    @FXML
    private MFXButton BT_confirm_old_pwd;

    /**
     * Bottone per confermare la nuova password
     */
    @FXML
    private MFXButton BT_confirm_new_pwd;

    /**
     * Stage riferito a questo controller
     */
    private Stage stage;

    /**
     * Immagine selezionata
     */
    private int selectedImage;

    /**
     * Nome del centro vaccinale
     */
    private String hubName;

    /**
     * Boolean per verificare l'apertura del {@link HubHomeSettingsDeleteController }
     */
    private boolean openDelete = false;

    /**
     * Controller utilizzato per scambio dati
     */
    private HubHomeSettingsDeleteController hubHomeSettingsDeleteController;

    /**
     * Utilizzato per settare il nome del centro vaccinale
     *
     * @param hubName nome centro vaccinale
     */
    void setHubName(String hubName) {
        this.hubName = hubName;
    }

    /**
     * Utilizzato per ottenere l'esito di eliminazione dell'account dal
     * controller {@link HubHomeSettingsDeleteController}
     *
     * @return true se la schermata per eliminare l'accont e' stata aperta e
     * l'eliminazione ha avuto esito positivo, false in caso contrario
     */
    boolean getDeleteAccSettings() {
        if (openDelete) {
            return hubHomeSettingsDeleteController.getDeleteAccPopUp();
        }
        return false;
    }

    /**
     * Utilizzato per scurire le immagini
     * quando il cursore entra su una di esse
     */
    private void setDarkStyle() {
        switch (selectedImage) {
            case 1:
                setDarkHover(IV_one);
                break;
            case 2:
                setDarkHover(IV_two);
                break;
            case 3:
                setDarkHover(IV_three);
                break;
            case 4:
                setDarkHover(IV_four);
                break;
            case 5:
                setDarkHover(IV_five);
                break;
            case 6:
                setDarkHover(IV_six);
                break;
            case 7:
                setDarkHover(IV_seven);
                break;
            case 8:
                setDarkHover(IV_eight);
                break;
            case 9:
                setDarkHover(IV_nine);
                break;
        }
    }

    /**
     * Utilizzato per prendere il numero
     * dell'immagine selezionata
     *
     * @return l'immagine selezionato
     */
    int getSelectedImage() {
        return selectedImage;
    }

    /**
     * Assegna alla variabile la nuova immagine selezionata
     *
     * @param selectedImage l'immagine selezionata al momento
     */
    void setSelectedImage(int selectedImage) {
        this.selectedImage = selectedImage;
        setDarkStyle();
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
        Platform.runLater(() -> {
            stage = (Stage) AP_ext.getScene().getWindow();
            BT_confirm_old_pwd.requestFocus();
        });
    }

    /**
     * Utilizzato per fare i vari controlli sulla nuova
     * password e per inserire il cambio della password
     * su db.
     * Se rispetta i diversi controlli fa apparire
     * una label che ne conferma la riuscita
     *
     * @see org.project.utils.RegistrationUtil#checkPassword(String)
     * @see org.project.utils.RegistrationUtil#checkPasswordConfirmed(String, String)
     * @see org.project.utils.RegistrationUtil#checkChangePwd(String, String)
     * @see org.project.server.Server#changePwd(String, String, String);
     */
    @FXML
    private void confirm_new_password() {
        String newPwd = TF_new_password.getPassword().strip();
        String confirmNewPwd = TF_confirm_password.getPassword().strip();
        String oldPwd = TF_old_password.getPassword().strip();

        if (RegistrationUtil.checkPassword(newPwd)) {
            if (RegistrationUtil.checkPasswordConfirmed(newPwd, confirmNewPwd)) {
                if (RegistrationUtil.checkChangePwd(newPwd, oldPwd)) {
                    String cryptPwd = Password.hash(newPwd).addRandomSalt().withArgon2().getResult();
                    try {
                        ServerReference.getServer().changePwd(hubName, "", cryptPwd);
                        LB_error_password.setVisible(false);
                        TF_new_password.setVisible(false);
                        BT_confirm_new_pwd.setVisible(false);
                        TF_confirm_password.setVisible(false);
                        LB_success_change.setVisible(true);

                        TIMER.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                TF_old_password.setVisible(true);
                                BT_confirm_old_pwd.setVisible(true);
                                LB_success_change.setVisible(false);
                                //TODO settare vuoti text pwd
                            }
                        }, 5 * 1000);
                    } catch (RemoteException | NotBoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    LB_error_password.setText("La nuova password è uguale alla precedente");
                    LB_error_password.setVisible(true);
                }
            } else {
                LB_error_password.setText("Le password non coincidono");
                LB_error_password.setVisible(true);
            }
        } else {
            LB_error_password.setText("Minimo 6 caratteri, 1 speciale, 1 minuscolo, 1 maiuscolo, 1 numero");
            LB_error_password.setVisible(true);
        }
    }

    /**
     * Per verificare la correttezza della password inserita.
     * Se corrisponde con quella presente nel DB allora si abilitano i campi
     * per inserire la nuova password. In caso contrario viene segnalato l'errore
     *
     * @see org.project.server.Server#checkPassword(String, String, String)
     */
    @FXML
    private void confirm_old_password() {
        String pwd = TF_old_password.getPassword().strip();
        try {
            boolean checkPwd = ServerReference.getServer().checkPassword(hubName, "", pwd);
            if (checkPwd) {
                TF_old_password.setVisible(false);
                LB_error_password.setVisible(false);
                TF_new_password.setVisible(true);
                BT_confirm_old_pwd.setVisible(false);
                BT_confirm_new_pwd.setVisible(true);
                TF_confirm_password.setVisible(true);
            } else {
                LB_error_password.setText("Password non corretta");
                BT_confirm_old_pwd.setVisible(true);
                BT_confirm_new_pwd.setVisible(false);
                LB_error_password.setVisible(true);
            }
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Attivato quando viene premuto il bottone {@link #BT_delete} e
     * va a richiamare il metodo {@link #openDeletePopUp} che gestisce l'eventuale
     * eliminazione dell'account
     */
    @FXML
    private void delete() {
        try {
            openDeletePopUp();
            openDelete = true;
            if (hubHomeSettingsDeleteController.getDeleteAccPopUp()) {
                stage.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Viene aperto il pop per la delete
     *
     * @throws IOException IOException
     */
    private void openDeletePopUp() throws IOException {
        FXMLLoader loader = new FXMLLoader(HubHomeSettingsDeleteController.class.getResource("fxml/hub_settings_delete.fxml"));
        Parent root = loader.load();
        hubHomeSettingsDeleteController = loader.getController();
        hubHomeSettingsDeleteController.setHubName(hubName);

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        stage.setResizable(false);
        stage.setTitle("Cancella account");
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
     * Quando premuto, il tasto exit chiude lo stage
     */
    @FXML
    private void quit() {
        TIMER.cancel();
        stage.close();
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
     * Per scurire l'immagine 1
     */
    @FXML
    private void darkStyleIVOne() {
        setDarkHover(IV_one);
    }

    /**
     * Per rifristinare l'effetto dell'immagine 1
     */
    @FXML
    private void restoreStyleIVOne() {
        if (selectedImage != 1) {
            resetDarkExit(IV_one);
        }
    }

    /**
     * per deselezionare l'immagine selezionata precedentemente e
     * selezionare quella corrente, utilizza {@link #resetStyle} e
     * {@link #setDarkHover}
     */
    @FXML
    private void selectImageOne() {
        if (selectedImage != 1) {
            resetStyle();
            setDarkHover(IV_one);
            selectedImage = 1;
        }
    }

    /**
     * Per scurire l'immagine 2
     */
    @FXML
    private void darkStyleIVTwo() {
        setDarkHover(IV_two);
    }

    /**
     * Per rifristinare l'effetto dell'immagine 2
     */
    @FXML
    private void restoreStyleIVTwo() {
        if (selectedImage != 2) {
            resetDarkExit(IV_two);
        }
    }

    /**
     * per deselezionare l'immagine selezionata precedentemente e
     * selezionare quella corrente, utilizza {@link #resetStyle} e
     * {@link #setDarkHover}
     */
    @FXML
    private void selectImageTwo() {
        if (selectedImage != 2) {
            resetStyle();
            setDarkHover(IV_two);
            selectedImage = 2;
        }
    }

    /**
     * Per scurire l'immagine 3
     */
    @FXML
    private void darkStyleIVThree() {
        setDarkHover(IV_three);
    }

    /**
     * Per rifristinare l'effetto dell'immagine 3
     */
    @FXML
    private void restoreStyleIVThree() {
        if (selectedImage != 3) {
            resetDarkExit(IV_three);
        }
    }

    /**
     * per deselezionare l'immagine selezionata precedentemente e
     * selezionare quella corrente, utilizza {@link #resetStyle} e
     * {@link #setDarkHover}
     */
    @FXML
    private void selectImageThree() {
        if (selectedImage != 3) {
            resetStyle();
            setDarkHover(IV_three);
            selectedImage = 3;
        }
    }

    /**
     * Per scurire l'immagine 4
     */
    @FXML
    private void darkStyleIVFour() {
        setDarkHover(IV_four);
    }

    /**
     * Per rifristinare l'effetto dell'immagine 4
     */
    @FXML
    private void restoreStyleIVFour() {
        if (selectedImage != 4) {
            resetDarkExit(IV_four);
        }
    }

    /**
     * per deselezionare l'immagine selezionata precedentemente e
     * selezionare quella corrente, utilizza {@link #resetStyle} e
     * {@link #setDarkHover}
     */
    @FXML
    private void selectImageFour() {
        if (selectedImage != 4) {
            resetStyle();
            setDarkHover(IV_four);
            selectedImage = 4;
        }
    }

    /**
     * Per scurire l'immagine 5
     */
    @FXML
    private void darkStyleIVFive() {
        setDarkHover(IV_five);
    }

    /**
     * Per rifristinare l'effetto dell'immagine 5
     */
    @FXML
    private void restoreStyleIVFive() {
        if (selectedImage != 5) {
            resetDarkExit(IV_five);
        }
    }

    /**
     * per deselezionare l'immagine selezionata precedentemente e
     * selezionare quella corrente, utilizza {@link #resetStyle} e
     * {@link #setDarkHover}
     */
    @FXML
    private void selectImageFive() {
        if (selectedImage != 5) {
            resetStyle();
            setDarkHover(IV_five);
            selectedImage = 5;
        }
    }

    /**
     * Per scurire l'immagine 6
     */
    @FXML
    private void darkStyleIVSix() {
        setDarkHover(IV_six);
    }

    /**
     * Per rifristinare l'effetto dell'immagine 6
     */
    @FXML
    private void restoreStyleIVSix() {
        if (selectedImage != 6) {
            resetDarkExit(IV_six);
        }
    }

    /**
     * per deselezionare l'immagine selezionata precedentemente e
     * selezionare quella corrente, utilizza {@link #resetStyle} e
     * {@link #setDarkHover}
     */
    @FXML
    private void selectImageSix() {
        if (selectedImage != 6) {
            resetStyle();
            setDarkHover(IV_six);
            selectedImage = 6;
        }
    }

    /**
     * Per scurire l'immagine 7
     */
    @FXML
    private void darkStyleIVSeven() {
        setDarkHover(IV_seven);
    }

    /**
     * Per rifristinare l'effetto dell'immagine 7
     */
    @FXML
    private void restoreStyleIVSeven() {
        if (selectedImage != 7) {
            resetDarkExit(IV_seven);
        }
    }

    /**
     * per deselezionare l'immagine selezionata precedentemente e
     * selezionare quella corrente, utilizza {@link #resetStyle} e
     * {@link #setDarkHover}
     */
    @FXML
    private void selectImageSeven() {
        if (selectedImage != 7) {
            resetStyle();
            setDarkHover(IV_seven);
            selectedImage = 7;
        }
    }

    /**
     * Per scurire l'immagine 8
     */
    @FXML
    private void darkStyleIVEight() {
        setDarkHover(IV_eight);
    }

    /**
     * Per rifristinare l'effetto dell'immagine 8
     */
    @FXML
    private void restoreStyleIVEight() {
        if (selectedImage != 8) {
            resetDarkExit(IV_eight);
        }
    }

    /**
     * per deselezionare l'immagine selezionata precedentemente e
     * selezionare quella corrente, utilizza {@link #resetStyle} e
     * {@link #setDarkHover}
     */
    @FXML
    private void selectImageEight() {
        if (selectedImage != 8) {
            resetStyle();
            setDarkHover(IV_eight);
            selectedImage = 8;
        }
    }

    /**
     * Per scurire l'immagine 9
     */
    @FXML
    private void darkStyleIVNine() {
        setDarkHover(IV_nine);
    }

    /**
     * Per rifristinare l'effetto dell'immagine 9
     */
    @FXML
    private void restoreStyleIVNine() {
        if (selectedImage != 9) {
            resetDarkExit(IV_nine);
        }
    }

    /**
     * per deselezionare l'immagine selezionata precedentemente e
     * selezionare quella corrente, utilizza {@link #resetStyle} e
     * {@link #setDarkHover}
     */
    @FXML
    private void selectImageNine() {
        if (selectedImage != 9) {
            resetStyle();
            setDarkHover(IV_nine);
            selectedImage = 9;
        }
    }

    /**
     * Utilizzato per reimpostare lo stile delle immagini
     * non piu' selezionate. Per farlo utilizza {@link #resetDarkExit}
     */
    private void resetStyle() {
        switch (selectedImage) {
            case 1:
                resetDarkExit(IV_one);
                break;
            case 2:
                resetDarkExit(IV_two);
                break;
            case 3:
                resetDarkExit(IV_three);
                break;
            case 4:
                resetDarkExit(IV_four);
                break;
            case 5:
                resetDarkExit(IV_five);
                break;
            case 6:
                resetDarkExit(IV_six);
                break;
            case 7:
                resetDarkExit(IV_seven);
                break;
            case 8:
                resetDarkExit(IV_eight);
                break;
            case 9:
                resetDarkExit(IV_nine);
                break;
        }
    }
}
