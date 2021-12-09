package org.project.user;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.project.server.ServerReference;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Classe utilizzata per l'invio della email di verifica all'utente indicato,
 * usato in {@link UserSignUpController}
 *
 * @author Federico Mainini 740691 (VA)
 * @author Gianluca Latronico 739893 (VA)
 * @author Marc Alexander Orlando 741473 (VA)
 * @author Enrico Luigi Lamperti 740612 (VA)
 */
public class UserVerifyEmailController implements Initializable {

    /**
     * Timer utilizzato per impostare il boolean per permettere il
     * rinvio dell'email con il codice
     */
    private final Timer HIDDENTIMER = new Timer();
    /**
     * Timer utilizzato per mostrare il conto alla rovescia per
     * il rinvio dell'email
     */
    private final Timer VISIBLETIMER = new Timer();

    /**
     * AnchorPane esterno
     */
    @FXML
    private AnchorPane AP_ext;

    /**
     * Immagine che funge da quit dal programma
     */
    @FXML
    private ImageView BT_quit;

    /**
     * Field per il numero del codice 1
     */
    @FXML
    private TextField TF_one;

    /**
     * Field per il numero del codice 2
     */
    @FXML
    private TextField TF_two;

    /**
     * Field per il numero del codice 3
     */
    @FXML
    private TextField TF_three;

    /**
     * Field per il numero del codice 4
     */
    @FXML
    private TextField TF_four;

    /**
     * Field per il numero del codice 5
     */
    @FXML
    private TextField TF_five;

    /**
     * Field per il numero del codice 6
     */
    @FXML
    private TextField TF_six;

    /**
     * Bottone per confermare l'inserimento del codice
     */
    @FXML
    private MFXButton BT_confirmed;

    /**
     * Label per richiedere l'invio di un nuovo
     * codice
     */
    @FXML
    private Label BT_new_code;

    /**
     * Label di errore per l'inseriemnto errato del
     * codice
     */
    @FXML
    private Label LB_error_code;

    /**
     * Label per il conto alla rovescia
     */
    @FXML
    private Label LB_timer;

    /**
     * Stage riferito a questo controller
     */
    private Stage stage;

    /**
     * Boolean per il rinvio dell'email
     */
    private boolean resend = true;

    /**
     * Int per il conto alla rovescia
     */
    private int countdown;

    /**
     * Email
     */
    private String email;

    /**
     * Nickname
     */
    private String nickname;

    /**
     * Boolean per la conferma della verifica
     * dell'email
     */
    private boolean isVerified;

    /**
     * Utilizzato per impostare l'email e il nickname dell'utente
     *
     * @param email    email
     * @param nickname nickname
     */
    void setUserData(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }

    /**
     * Utilizzato per impostare il boolean di verifica dell'email
     *
     * @return true se è verificata, false altrimenti
     */
    boolean getIsVerified() {
        return isVerified;
    }

    /**
     * Utilizzato per inizializzare l'interfaccia prendendo la scena
     * e aggiungendo i listener sulle textField far si che si possa solo
     * scrivere un numero all'interno
     *
     * @param url            url
     * @param resourceBundle resourceBundle
     * @see org.project.server.Server#sendVerifyEmail(String, String)
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            stage = (Stage) AP_ext.getScene().getWindow();

            try {
                ServerReference.getServer().sendVerifyEmail(email, nickname);
            } catch (NotBoundException | RemoteException e) {
                e.printStackTrace();
            }
        });

        TF_one.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                TF_one.setText(newValue.replaceAll("[^\\d]", ""));
            } else {
                TF_two.requestFocus();
            }
        });

        TF_two.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                TF_two.setText(newValue.replaceAll("[^\\d]", ""));
            } else {
                TF_three.requestFocus();
            }
        });

        TF_three.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                TF_three.setText(newValue.replaceAll("[^\\d]", ""));
            } else {
                TF_four.requestFocus();
            }
        });

        TF_four.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                TF_four.setText(newValue.replaceAll("[^\\d]", ""));
            } else {
                TF_five.requestFocus();
            }
        });

        TF_five.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                TF_five.setText(newValue.replaceAll("[^\\d]", ""));
            } else {
                TF_six.requestFocus();
            }
        });

        TF_six.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                TF_six.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    /**
     * Quando premuto, il tasto exit viene cancellata viene cancellata la referenza
     * presente sul server per poter verificare il codice e chiuso lo stage
     *
     * @see org.project.server.Server#deleteReferenceVerifyEmail(String)
     */
    @FXML
    private void quit() {
        isVerified = false;
        try {
            ServerReference.getServer().deleteReferenceVerifyEmail(email);
            VISIBLETIMER.cancel();
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
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
     * Utilizzato per confeconfermare il codice inserito, nel caso il codice sia correto lo stage viene chiuso
     * al contrario se false, vengono resettati tutti i campi a vuoti.
     * Per poter verificare che il codice sia corretto viene utilizzato il metodo remoto presente sul server
     *
     * @see org.project.server.Server#verifyCodeEmail(String, int)
     */
    @FXML
    private void confirmed() {
        try {
            int code = Integer.parseInt(TF_one.getText() + TF_two.getText() + TF_three.getText() + TF_four.getText() + TF_five.getText() + TF_six.getText());
            try {
                boolean verified = ServerReference.getServer().verifyCodeEmail(email, code);
                if (verified) {
                    isVerified = true;
                    VISIBLETIMER.cancel();
                    stage.close();
                } else {
                    LB_error_code.setVisible(true);
                    TF_one.setText("");
                    TF_two.setText("");
                    TF_three.setText("");
                    TF_four.setText("");
                    TF_five.setText("");
                    TF_six.setText("");
                }
            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
            }
        } catch (NumberFormatException e) {
            LB_error_code.setVisible(true);
        }
    }

    /**
     * Utilizzato per richiedere un nuovo codice, ma utilizzando un timer non è possibile richiederlo in continuazione,
     * questo timeout è impostato a 60 secondi
     */
    @FXML
    private void newCode() {
        if (resend) {
            LB_error_code.setVisible(false);
            TF_one.setText("");
            TF_two.setText("");
            TF_three.setText("");
            TF_four.setText("");
            TF_five.setText("");
            TF_six.setText("");

            try {
                ServerReference.getServer().deleteReferenceVerifyEmail(email);
                ServerReference.getServer().sendVerifyEmail(email, nickname);
                resend = false;

                HIDDENTIMER.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        resend = true;
                    }
                }, 60 * 1000);

                countdown = 60;
                LB_timer.setVisible(true);
                System.out.println(countdown);
                VISIBLETIMER.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(() -> LB_timer.setText(String.valueOf(--countdown)));

                        if (countdown == 0) {
                            LB_timer.setVisible(false);
                            cancel();
                        }
                    }
                }, 0, 1000);
            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
            }
        }
    }
}
