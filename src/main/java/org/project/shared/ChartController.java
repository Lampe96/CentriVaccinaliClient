package org.project.shared;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.project.UserType;
import org.project.hub.HubHomeController;
import org.project.user.UserHomeController;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Questa classe gestisce tutti i componenti presenti nella
 * schermata del grafico che mostra l'andamento della campagna
 * vaccinale, condivisa da {@link HubHomeController} e {@link UserHomeController}
 *
 * @author Federico Mainini 740691 (VA)
 * @author Gianluca Latronico 739893 (VA)
 * @author Marc Alexander Orlando 741473 (VA)
 * @author Enrico Luigi Lamperti 740612 (VA)
 */
public class ChartController implements Initializable {

    /**
     * AnchorPane esterno
     */
    @FXML
    private AnchorPane AP_ext;

    /**
     * Immagine che funge da quit dall'applicazione
     */
    @FXML
    private ImageView BT_quit;

    /**
     * Grafico di tipo BarChart
     */
    @FXML
    private StackedBarChart<String, Integer> barChart;

    /**
     * Stage riferito al controller
     */
    private Stage stage;

    /**
     * Array che contiene i dati per riempire il grafico
     */
    private int[] totals;

    /**
     * Tipo di utente
     */
    private UserType userType;

    /**
     * Utilizzato per settare i totali delle vaccinazioni in questo controller
     *
     * @param totals totali vaccinazioni
     */
    public void setData(int[] totals) {
        this.totals = totals;
    }

    /**
     * @param userType tipo di utente, se cittadino verranno create tre
     *                 colonne, se centro vaccinale quattro
     */
    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    /**
     * Utilizzato per prendere lo stage e inizializzare
     * il grafico
     *
     * @param url            url
     * @param resourceBundle resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            stage = (Stage) AP_ext.getScene().getWindow();

            XYChart.Series<String, Integer> totalSeries = new XYChart.Series<>();
            totalSeries.setName("TOTALI");
            totalSeries.getData().add(new XYChart.Data<>("Totali", totals[0]));

            XYChart.Series<String, Integer> firstDoseSeries = new XYChart.Series<>();
            firstDoseSeries.setName("TOTALI PRIMA DOSE");
            firstDoseSeries.getData().add(new XYChart.Data<>("Prima", totals[1]));

            XYChart.Series<String, Integer> secondDoseSeries = new XYChart.Series<>();
            secondDoseSeries.setName("TOTALI SECONDA DOSE");
            secondDoseSeries.getData().add(new XYChart.Data<>("Seconda", totals[2]));

            if (userType == UserType.HUB) {
                XYChart.Series<String, Integer> centreVaccinatedSeries = new XYChart.Series<>();
                centreVaccinatedSeries.setName("TOTALI VACCINATI CENTRO");
                centreVaccinatedSeries.getData().add(new XYChart.Data<>("Centro", totals[3]));

                //noinspection unchecked
                barChart.getData().addAll(totalSeries, firstDoseSeries, secondDoseSeries, centreVaccinatedSeries);
            } else {
                //noinspection unchecked
                barChart.getData().addAll(totalSeries, firstDoseSeries, secondDoseSeries);
            }
        });
    }

    /**
     * Quando premuto, il tasto exit chiude lo stage
     */
    @FXML
    private void quit() {
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
}
