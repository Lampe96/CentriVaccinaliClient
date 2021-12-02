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

public class UserVerifyEmailController implements Initializable {

    private final Timer HIDDENTIMER = new Timer();
    private final Timer VISIBLETIMER = new Timer();

    @FXML
    private AnchorPane AP_ext;

    @FXML
    private ImageView BT_quit;

    @FXML
    private TextField TF_one;

    @FXML
    private TextField TF_two;

    @FXML
    private TextField TF_three;

    @FXML
    private TextField TF_four;

    @FXML
    private TextField TF_five;

    @FXML
    private TextField TF_six;

    @FXML
    private MFXButton BT_confirmed;

    @FXML
    private Label BT_new_code;

    @FXML
    private Label LB_error_code;

    @FXML
    private Label LB_timer;

    private Stage stage;
    private boolean resend = true;
    private int countdown;
    private String email;
    private String nickname;
    private boolean isVerified;

    void setUserData(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }

    boolean getIsVerified() {
        return isVerified;
    }

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
