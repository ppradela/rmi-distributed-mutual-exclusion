package rmi.mutex.server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Objects;

public class StartServer extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/server.fxml"));
        Image icon = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("icon/server.png")));
        Parent root = fxmlLoader.load();
        ServerController fxmController = fxmlLoader.getController();

        Scene scene = new Scene(root);
        stage.setTitle("Server");
        stage.getIcons().add(icon);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(event -> {
            try {
                fxmController.handleExit();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }
}