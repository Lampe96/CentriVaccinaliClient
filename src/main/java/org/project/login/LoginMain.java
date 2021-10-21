package org.project.login;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;
import org.project.UserType;
import org.project.server.ServerReference;
import org.project.utils.WindowUtil;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Objects;

public class LoginMain extends Application {

    private double xOffset, yOffset;

    public static void main(String[] args) {
        try {
            ServerReference.initializeServer();
            launch();
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            //poi togliere questo launch dal catch e mettere un alert per no connessione
            launch();
        }
    }

    @Override
    public void start(@NotNull Stage stage) throws IOException {
        Scene scene = new Scene(WindowUtil.newScene(LoginMain.class.getResource("fxml/login.fxml")));
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("CVI");
        stage.getIcons().add(new Image(Objects.requireNonNull(UserType.class.getResourceAsStream("drawable/primula.png"))));
        stage.show();

        scene.setOnMousePressed(mouseEvent -> {
            xOffset = mouseEvent.getSceneX();
            yOffset = mouseEvent.getSceneY();
        });

        scene.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() - xOffset);
            stage.setY(mouseEvent.getScreenY() - yOffset);
        });
    }
}