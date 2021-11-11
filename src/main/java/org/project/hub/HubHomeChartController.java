package org.project.hub;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.util.ResourceBundle;

public class HubHomeChartController implements Initializable {

    @FXML
    private StackedBarChart<String, Integer> barChart;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        series.setName("TOATaaaaaaaaaaa75766576567aaLI");
        series.getData().add(new XYChart.Data<>("Vaccinazioni totali\n" +
                "      750000", 750000));

        XYChart.Series<String, Integer> series2 = new XYChart.Series<>();
        series2.setName("TOAbbbbbbbbbbbbb444444TLI");
        series2.getData().add(new XYChart.Data<>("000", 50000));

        XYChart.Series<String, Integer> series3 = new XYChart.Series<>();
        series3.setName("AAAAA444444444444444444444AA");
        series3.getData().add(new XYChart.Data<>("0aaaaaaa", 500000));

        //noinspection unchecked
        barChart.getData().addAll(series, series2, series3);
    }
}
