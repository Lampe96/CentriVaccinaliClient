package org.project.hub;

import io.github.palexdev.materialfx.controls.MFXButton;
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

public class HubHomeSettingsController implements Initializable {

    @FXML
    public AnchorPane AP_ext;

    @FXML
    private ImageView BT_quit;

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
    private ImageView IV_eight;

    @FXML
    private ImageView IV_nine;

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

    private int selectedImage;
    private Stage stage;

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

    int getSelectedImage() {
        return selectedImage;
    }

    void setSelectedImage(int selectedImage) {
        this.selectedImage = selectedImage;
        setDarkStyle();
    }

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
    private void darkStyleIVOne() {
        setDarkHover(IV_one);
    }

    @FXML
    private void restoreStyleIVOne() {
        if (selectedImage != 1) {
            resetDarkExit(IV_one);
        }
    }

    @FXML
    private void selectImageOne() {
        if (selectedImage != 1) {
            resetStyle();
            setDarkHover(IV_one);
            selectedImage = 1;
        }
    }

    @FXML
    private void darkStyleIVTwo() {
        setDarkHover(IV_two);
    }

    @FXML
    private void restoreStyleIVTwo() {
        if (selectedImage != 2) {
            resetDarkExit(IV_two);
        }
    }

    @FXML
    private void selectImageTwo() {
        if (selectedImage != 2) {
            resetStyle();
            setDarkHover(IV_two);
            selectedImage = 2;
        }
    }

    @FXML
    private void darkStyleIVThree() {
        setDarkHover(IV_three);
    }

    @FXML
    private void restoreStyleIVThree() {
        if (selectedImage != 3) {
            resetDarkExit(IV_three);
        }
    }

    @FXML
    private void selectImageThree() {
        if (selectedImage != 3) {
            resetStyle();
            setDarkHover(IV_three);
            selectedImage = 3;
        }
    }

    @FXML
    private void darkStyleIVFour() {
        setDarkHover(IV_four);
    }

    @FXML
    private void restoreStyleIVFour() {
        if (selectedImage != 4) {
            resetDarkExit(IV_four);
        }
    }

    @FXML
    private void selectImageFour() {
        if (selectedImage != 4) {
            resetStyle();
            setDarkHover(IV_four);
            selectedImage = 4;
        }
    }

    @FXML
    private void darkStyleIVFive() {
        setDarkHover(IV_five);
    }

    @FXML
    private void restoreStyleIVFive() {
        if (selectedImage != 5) {
            resetDarkExit(IV_five);
        }
    }

    @FXML
    private void selectImageFive() {
        if (selectedImage != 5) {
            resetStyle();
            setDarkHover(IV_five);
            selectedImage = 5;
        }
    }

    @FXML
    private void darkStyleIVSix() {
        setDarkHover(IV_six);
    }

    @FXML
    private void restoreStyleIVSix() {
        if (selectedImage != 6) {
            resetDarkExit(IV_six);
        }
    }

    @FXML
    private void selectImageSix() {
        if (selectedImage != 6) {
            resetStyle();
            setDarkHover(IV_six);
            selectedImage = 6;
        }
    }

    @FXML
    private void darkStyleIVSeven() {
        setDarkHover(IV_seven);
    }

    @FXML
    private void restoreStyleIVSeven() {
        if (selectedImage != 7) {
            resetDarkExit(IV_seven);
        }
    }

    @FXML
    private void selectImageSeven() {
        if (selectedImage != 7) {
            resetStyle();
            setDarkHover(IV_seven);
            selectedImage = 7;
        }
    }

    @FXML
    private void darkStyleIVEight() {
        setDarkHover(IV_eight);
    }

    @FXML
    private void restoreStyleIVEight() {
        if (selectedImage != 8) {
            resetDarkExit(IV_eight);
        }
    }

    @FXML
    private void selectImageEight() {
        if (selectedImage != 8) {
            resetStyle();
            setDarkHover(IV_eight);
            selectedImage = 8;
        }
    }

    @FXML
    private void darkStyleIVNine() {
        setDarkHover(IV_nine);
    }

    @FXML
    private void restoreStyleIVNine() {
        if (selectedImage != 9) {
            resetDarkExit(IV_nine);
        }
    }

    @FXML
    private void selectImageNine() {
        if (selectedImage != 9) {
            resetStyle();
            setDarkHover(IV_nine);
            selectedImage = 9;
        }
    }

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



