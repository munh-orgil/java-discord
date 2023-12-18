package org.example.javafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.socket.Server;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class IpInput {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField ip_field;
    @FXML
    private Text error_text;
    @FXML
    void ConnectButton(ActionEvent event) {
        String ipAddress = ip_field.getText();
        if (ipAddress.isBlank()) {
            ipAddress = "127.0.0.1";
        }
        try {
            Server.Init(ipAddress);
            root = FXMLLoader.load(Main.class.getResource("Login.fxml"));
            stage = Constants.stage;
            scene = new Scene(root);
            Constants.ImportStyle(scene, this);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            error_text.setText("Couldn't connect to "+ipAddress);
        }
    }

    @FXML
    void initialize() {

    }

}
