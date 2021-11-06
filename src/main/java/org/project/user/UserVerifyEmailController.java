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
import java.util.concurrent.atomic.AtomicInteger;

public class UserVerifyEmailController implements Initializable {

    private static final Timer HIDDENTIMER = new Timer();
    private static final Timer VISIBLETIMER = new Timer();
    private static boolean resend = true;
    @FXML
    public AnchorPane AP_ext;
    @FXML
    public ImageView BT_quit;
    @FXML
    public TextField TF_one;
    @FXML
    public TextField TF_two;
    @FXML
    public TextField TF_three;
    @FXML
    public TextField TF_four;
    @FXML
    public TextField TF_five;
    @FXML
    public TextField TF_six;
    @FXML
    public MFXButton BT_confirmed;
    @FXML
    public Label BT_new_code;
    @FXML
    public Label LB_error_code;
    @FXML
    public Label LB_timer;

    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            ServerReference.getServer().sendVerifyEmail(TempUser.getEmail(), TempUser.getNickname());
        } catch (NotBoundException | RemoteException e) {
            e.printStackTrace();
        }

        Platform.runLater(() -> stage = (Stage) AP_ext.getScene().getWindow());

        TF_one.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                TF_one.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        TF_two.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                TF_two.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        TF_three.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                TF_three.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        TF_four.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                TF_four.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        TF_five.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                TF_five.setText(newValue.replaceAll("[^\\d]", ""));
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
        TempUser.setEmailIsVerified(false);
        try {
            ServerReference.getServer().deleteReferenceVerifyEmail(TempUser.getEmail());
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
        stage.hide();
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
    public void confirmed() {
        try {
            int code = Integer.parseInt(TF_one.getText() + TF_two.getText() + TF_three.getText() + TF_four.getText() + TF_five.getText() + TF_six.getText());
            try {
                boolean verified = ServerReference.getServer().verifyCodeEmail(TempUser.getEmail(), code);
                if (verified) {
                    TempUser.setEmailIsVerified(true);
                    stage.hide();
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
    public void newCode() {
        if (resend) {
            LB_error_code.setVisible(false);
            TF_one.setText("");
            TF_two.setText("");
            TF_three.setText("");
            TF_four.setText("");
            TF_five.setText("");
            TF_six.setText("");

            try {
                ServerReference.getServer().sendVerifyEmail(TempUser.getEmail(), TempUser.getNickname());
                resend = false;

                HIDDENTIMER.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        resend = true;
                    }
                }, 60 * 1000);

                AtomicInteger countdown = new AtomicInteger(60);
                LB_timer.setVisible(true);
                VISIBLETIMER.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(() -> {
                            LB_timer.setText(String.valueOf(countdown.get()));
                            countdown.getAndDecrement();

                            if (countdown.get() == 0) {
                                LB_timer.setVisible(false);
                            }
                        });
                    }
                }, 0, 1000);
            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
            }
        }
    }
}
