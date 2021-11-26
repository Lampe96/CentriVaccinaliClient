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

import java.net.URL;
import java.util.ResourceBundle;

public class ChartController implements Initializable {

    @FXML
    private AnchorPane AP_ext;

    @FXML
    private ImageView BT_quit;

    @FXML
    private StackedBarChart<String, Integer> barChart;

    private Stage stage;
    private int[] totals;
    private UserType userType;

    public void setData(int[] totals) {
        this.totals = totals;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

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

    @FXML
    private void quit() {
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
}
