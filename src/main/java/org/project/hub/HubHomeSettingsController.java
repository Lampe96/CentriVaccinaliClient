package org.project.hub;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXLabel;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.ResourceBundle;

public class HubHomeSettingsController  implements Initializable {

    @FXML
    public AnchorPane AP_ext;

    @FXML
    private ImageView BT_quit;

    @FXML
    private ImageView IV_zero;

    @FXML
    private ImageView IV_one;

    @FXML
    private ImageView IV_two;

    @FXML
    private ImageView IV_three;

    @FXML
    private ImageView IV_four;

    @FXML
    private ImageView IV_five;

    @FXML
    private ImageView IV_six;

    @FXML
    private ImageView IV_seven;

    @FXML
    private ImageView IV_eigth;

    @FXML
    private MFXButton BT_delete;

    @FXML
    private MFXTextField TF_old_password;

    @FXML
    private MFXTextField TF_new_password;

    @FXML
    private MFXTextField TF_confirm_password;

    @FXML
    private MFXButton BT_confirm_old_pwd;

    @FXML
    private MFXButton BT_confirm_new_pwd;

    private int selectedImage = 0;
    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> stage = (Stage) AP_ext.getScene().getWindow());
    }

    @FXML
    private void confirm_new_password() {

    }

    @FXML
    private void confirm_old_password() {

    }

    @FXML
    private void delete() {

    }

    @FXML
    private void quit() {
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
    void darkStyleIVZero() {
        setDarkHover(IV_zero);
    }

    @FXML
    void darkStyleIVOne() {
        setDarkHover(IV_one);
    }

    @FXML
    void darkStyleIVTwo() {
        setDarkHover(IV_two);
    }

    @FXML
    void darkStyleIVThree() {
        setDarkHover(IV_three);
    }

    @FXML
    void darkStyleIVFour() {
        setDarkHover(IV_four);
    }

    @FXML
    void darkStyleIVFive() {
        setDarkHover(IV_five);
    }

    @FXML
    void darkStyleIVSix() {
        setDarkHover(IV_six);
    }

    @FXML
    void darkStyleIVSeven() {
        setDarkHover(IV_seven);
    }

    @FXML
    void darkStyleIVEigth() {
        setDarkHover(IV_eigth);
    }

    @FXML
    void restoreStyleIVEigth() {
        if (selectedImage != 8){
            resetDarkExit(IV_eigth);
        }
    }

    @FXML
    void restoreStyleIVFive() {
        if (selectedImage != 5){
            resetDarkExit(IV_five);
        }
    }

    @FXML
    void restoreStyleIVFour() {
        if (selectedImage != 4){
            resetDarkExit(IV_four);
        }
    }

    @FXML
    void restoreStyleIVOne() {
        if (selectedImage != 1){
            resetDarkExit(IV_one);
        }
    }

    @FXML
    void restoreStyleIVSix() {
        if (selectedImage != 6){
            resetDarkExit(IV_six);
        }
    }

    @FXML
    void restoreStyleIVThree() {
        if (selectedImage != 3){
            resetDarkExit(IV_three);
        }
    }

    @FXML
    void restoreStyleIVTwo() {
        if (selectedImage != 2){
            resetDarkExit(IV_two);
        }
    }

    @FXML
    void restoreStyleIVZero() {
        if (selectedImage != 0){
            resetDarkExit(IV_zero);
        }
    }

    @FXML
    void restoreStyleIVSeven() {
        if (selectedImage != 7){
            resetDarkExit(IV_seven);
        }
    }

    @FXML
    void selectImageEigth() {
        setDarkHover(IV_eigth);
        selectedImage = 8;
    }

    @FXML
    void selectImageFive() {
        setDarkHover(IV_five);
        selectedImage = 5;
    }

    @FXML
    void selectImageFour() {
        setDarkHover(IV_four);
        selectedImage = 4;
    }

    @FXML
    void selectImageOne() {
        setDarkHover(IV_one);
        selectedImage = 0;
    }

    @FXML
    void selectImageSeven() {
        setDarkHover(IV_seven);
        selectedImage = 7;
    }

    @FXML
    void selectImageSix() {
        setDarkHover(IV_six);
        selectedImage = 6;
    }

    @FXML
    void selectImageThree() {
        setDarkHover(IV_three);
        selectedImage = 3;
    }

    @FXML
    void selectImageTwo() {
        setDarkHover(IV_two);
        selectedImage = 2;
    }

    @FXML
    void selectImageZero() {
        setDarkHover(IV_zero);
        selectedImage = 0;
    }

    private void resetStyle() {
        switch (selectedImage) {
            case 0:
                resetDarkExit(IV_zero);
        }
    }
}



