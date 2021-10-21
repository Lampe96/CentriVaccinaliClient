package org.project.hub;

import com.jfoenix.controls.JFXComboBox;
import com.password4j.Password;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.project.login.LoginMainController;
import org.project.models.Address;
import org.project.models.Hub;
import org.project.server.ServerReference;
import org.project.utils.RegistrationUtil;
import org.project.utils.WindowUtil;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Locale;
import java.util.ResourceBundle;

public class HubSignUpController implements Initializable {

    private final static String[] QUALIFICATOR = {
            "Via", "Corso", "Viale", "Piazza"
    };
    private final static String[] PROVINCES = {
            "AG", "AL", "AN", "AO", "AR", "AP", "AT", "AV", "BA", "BT", "BL", "BN", "BG", "BI", "BO", "BZ", "BS",
            "BS", "CA", "CL", "CB", "CI", "CE", "CT", "CZ", "CH", "CO", "CS", "CR", "KR", "CN", "EN", "FM", "FE",
            "FI", "FG", "FC", "FR", "GE", "GO", "GR", "IM", "IS", "SP", "AQ", "LT", "LE", "LC", "LI", "LO", "LU",
            "MC", "MN", "MS", "MT", "VS", "ME", "MI", "MO", "MB", "NA", "NO", "NU", "OG", "OT", "OR", "PD", "PA",
            "PR", "PV", "PG", "PU", "PE", "PC", "PI", "PT", "PN", "PZ", "PO", "RG", "RA", "RC", "RE", "RI", "RN",
            "RM", "RO", "SA", "SS", "SV", "SI", "SR", "SO", "TA", "TE", "TR", "TO", "TP", "TN", "TV", "TS", "UD",
            "VA", "VE", "VB", "VC", "VR", "VV", "VI", "VT"
    };
    private final static String[] TYPOLOGY = {
            "OSPEDALIERO", "AZIENDALE", "HUB"
    };

    @FXML
    public AnchorPane AP_ext;

    @FXML
    public ImageView BT_back;

    @FXML
    public ImageView BT_minimize;

    @FXML
    public ImageView BT_quit;

    @FXML
    public MFXTextField TF_name_hub;

    @FXML
    public MFXPasswordField PF_password;

    @FXML
    public MFXPasswordField PF_confirmed_password;

    @FXML
    public JFXComboBox<String> CB_qualificator;

    @FXML
    public MFXTextField TF_address;

    @FXML
    public MFXTextField TF_number;

    @FXML
    public MFXTextField TF_city;

    @FXML
    public JFXComboBox<String> CB_province;

    @FXML
    public JFXComboBox<String> CB_typology;

    @FXML
    public MFXButton BT_sing_up;

    @FXML
    public Label LB_error_name;

    @FXML
    public Label LB_error_password;

    @FXML
    public Label LB_error_confirmed_password;

    @FXML
    public Label LB_error_qualificator;

    @FXML
    public Label LB_error_address;

    @FXML
    public Label LB_error_number;

    @FXML
    public Label LB_error_city;

    @FXML
    public Label LB_error_province;

    @FXML
    public Label LB_error_typology;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CB_qualificator.getItems().addAll(QUALIFICATOR);
        CB_province.getItems().addAll(PROVINCES);
        CB_typology.getItems().addAll(TYPOLOGY);
    }

    /**
     * Metodo per inizializzare la backarrow
     */
    @FXML
    public void back() {
        try {
            WindowUtil.setRoot(LoginMainController.class.getResource("fxml/login.fxml"), AP_ext.getScene());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo per scurire il pulsante backarrow quando ci si passa sopra
     */
    @FXML
    public void darkStyleBack() {
        setDarkHover(BT_back);
    }

    /**
     * Metodo per resettare il colore del pulsante backarrow quando ci si toglie dal pulsante
     */
    @FXML
    public void restoreStyleBack() {
        resetDarkExit(BT_back);
    }

    /**
     * Metodo per ridurre il programma ad icona
     */
    @FXML
    private void minimize() {
        Stage stage = (Stage) AP_ext.getScene().getWindow();
        stage.setIconified(true);
    }

    /**
     *
     */
    @FXML
    private void darkStyleMinimize() {
        setDarkHover(BT_minimize);
    }

    /**
     *
     */
    @FXML
    private void restoreStyleMinimize() {
        resetDarkExit(BT_minimize);
    }

    /**
     *
     */
    @FXML
    private void quit() {
        System.exit(0);
    }

    /**
     *
     */
    @FXML
    private void darkStyleQuit() {
        setDarkHover(BT_quit);
    }

    /**
     *
     */
    @FXML
    private void restoreStyleQuit() {
        resetDarkExit(BT_quit);
    }

    /**
     *
     */
    private void setDarkHover(@NotNull ImageView iv) {
        iv.setEffect(new InnerShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, 0.5), 10, 0, 5, 5));
    }

    /**
     *
     */
    private void resetDarkExit(@NotNull ImageView iv) {
        iv.setEffect(null);
    }

    /**
     * Metodo per gestire la registrazione, andando a controllare tutti i campi tramite i metodi
     * richiamati dalla classe RegistrationUtil
     */
    @FXML
    public void signUp() {
        boolean saveOk = true;
        String hub_name = StringUtils.capitalize(TF_name_hub.getText().strip());
        String pwd = PF_password.getText().strip();
        String confirm_pwd = PF_confirmed_password.getText().strip();
        String quali = CB_qualificator.getValue();
        String address = StringUtils.capitalize(TF_address.getText().strip());
        String number = TF_number.getText().strip().toUpperCase(Locale.ROOT);
        String city = StringUtils.capitalize(TF_city.getText().strip());
        String prov = CB_province.getValue();
        String typology = CB_typology.getValue();

        if (RegistrationUtil.checkLength(hub_name)) {
            LB_error_name.setVisible(false);
        } else {
            LB_error_name.setVisible(true);
            saveOk = false;
        }

        if (RegistrationUtil.checkPassword(pwd)) {
            LB_error_password.setVisible(false);
        } else {
            LB_error_password.setVisible(true);
            saveOk = false;
        }

        if (RegistrationUtil.checkPasswordConfirmed(pwd, confirm_pwd)) {
            LB_error_confirmed_password.setVisible(false);
        } else {
            LB_error_confirmed_password.setVisible(true);
            saveOk = false;
        }

        if (quali != null) {
            LB_error_qualificator.setVisible(false);
        } else {
            LB_error_qualificator.setVisible(true);
            saveOk = false;
        }

        if (RegistrationUtil.checkAddress(address)) {
            LB_error_address.setVisible(false);
        } else {
            LB_error_address.setVisible(true);
            saveOk = false;
        }

        if (RegistrationUtil.checkNumberAddress(number)) {
            LB_error_number.setVisible(false);
        } else {
            LB_error_number.setVisible(true);
            saveOk = false;
        }

        if (RegistrationUtil.checkCityAddress(city)) {
            LB_error_city.setVisible(false);
        } else {
            LB_error_city.setVisible(true);
            saveOk = false;
        }

        if (prov != null) {
            LB_error_province.setVisible(false);
        } else {
            LB_error_province.setVisible(true);
            saveOk = false;
        }

        if (typology != null) {
            LB_error_typology.setVisible(false);
        } else {
            LB_error_typology.setVisible(true);
            saveOk = false;
        }

        if (saveOk) {
            String cryptPwd = Password.hash(pwd).addRandomSalt().withArgon2().getResult();
            Address finalAddress = new Address(quali, address, number, city, prov);
            Hub hub = new Hub(hub_name, cryptPwd, finalAddress, typology);
            System.out.println(hub);

            try {
                ServerReference.getServer().insertDataHub(hub);
            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
            }
        }
    }
}