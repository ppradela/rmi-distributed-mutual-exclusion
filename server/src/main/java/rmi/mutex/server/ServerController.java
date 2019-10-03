package rmi.mutex.server;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import rmi.mutex.utils.DigitsValidator;
import rmi.mutex.utils.IpAddressValidator;

public class ServerController {
    private IpAddressValidator ipAddressValidator = new IpAddressValidator();
    private DigitsValidator digitsValidator = new DigitsValidator();
    private Alert errorAlert = new Alert(AlertType.ERROR);
    private Registry registry;
    private ServerApi server;
    private boolean registryRunning = false;

    @FXML
    private TextArea logsTextArea;

    @FXML
    private TextField ipTextField;

    @FXML
    private TextField portTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private Button startBtn;

    @FXML
    private Button stopBtn;

    @FXML
    void initialize() {
        errorAlert.setTitle("Błąd");

        startBtn.setOnAction(a -> {
            if (ipTextField.getText().isEmpty()) {
                logsTextArea.appendText(server.getDateFormat()
                        .format(new Date(System.currentTimeMillis()) + "\tERROR\tNie podano adresu IP serwera\n"));
                errorAlert.setHeaderText("Pole adresu IP nie może być puste!");
                errorAlert.showAndWait();
            } else if (portTextField.getText().isEmpty()) {
                logsTextArea.appendText(server.getDateFormat()
                        .format(new Date(System.currentTimeMillis()) + "\tERROR\tNie podano portu serwera\n"));
                errorAlert.setHeaderText("Pole portu może być puste!");
                errorAlert.showAndWait();
            } else if (nameTextField.getText().isEmpty()) {
                logsTextArea.appendText(server.getDateFormat()
                        .format(new Date(System.currentTimeMillis()) + "\tERROR\tNie podano nazwy serwera\n"));
                errorAlert.setHeaderText("Pole nazwy nie może być puste!");
                errorAlert.showAndWait();
            } else if (!ipAddressValidator.validate(ipTextField.getText())) {
                logsTextArea.appendText(server.getDateFormat()
                        .format(new Date(System.currentTimeMillis()) + "\tERROR\tZły format adresu IP\n"));
                errorAlert.setHeaderText("Zły format adresu IP!");
                errorAlert.showAndWait();
            } else if (!digitsValidator.validate(portTextField.getText())) {
                logsTextArea.appendText(server.getDateFormat()
                        .format(new Date(System.currentTimeMillis()) + "\tERROR\tZły format numeru portu\n"));
                errorAlert.setHeaderText("Numer portu może zawierać tylko cyfry!");
                errorAlert.showAndWait();
            } else {
                //System.setProperty("java.rmi.server.hostname", ipTextField.getText());
                try {
                    server = new ServerApi(logsTextArea);
                    registry = LocateRegistry.createRegistry(Integer.parseInt(portTextField.getText()));
                    registry.rebind(nameTextField.getText(), server);
                    logsTextArea.appendText(server.getDateFormat().format(new Date(System.currentTimeMillis()))
                            + "\tINFO\tSerwer został uruchomiony\n");

                    registryRunning = true;

                    ipTextField.setDisable(true);
                    portTextField.setDisable(true);
                    nameTextField.setDisable(true);

                    stopBtn.setDisable(false);
                    startBtn.setDisable(true);
                } catch (RemoteException e) {
                    logsTextArea.appendText(server.getDateFormat()
                            .format(new Date(System.currentTimeMillis()) + "\tERROR\tPort " + portTextField.getText() + " jest już używany\n"));
                    errorAlert.setHeaderText("Port " + portTextField.getText() + " jest już używany!");
                    errorAlert.showAndWait();
                    e.printStackTrace();
                }
            }
        });

        stopBtn.setOnAction(a ->

        {
            try {
                UnicastRemoteObject.unexportObject(registry, true);
                logsTextArea.appendText(server.getDateFormat().format(new Date(System.currentTimeMillis()))
                        + "\tINFO\tSerwer został wyłączony\n");

                registryRunning = false;

                ipTextField.setDisable(false);
                portTextField.setDisable(false);
                nameTextField.setDisable(false);

                startBtn.setDisable(false);
                stopBtn.setDisable(true);
            } catch (NoSuchObjectException e) {
                e.printStackTrace();
            }
        });
    }

    public void handleExit() throws NoSuchObjectException {
        if (registryRunning) {
            UnicastRemoteObject.unexportObject(registry, true);
            Platform.exit();
            System.exit(0);
        } else {
            Platform.exit();
            System.exit(0);
        }
    }
}