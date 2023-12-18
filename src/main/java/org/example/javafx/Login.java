package org.example.javafx;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.modules.User;
import org.example.socket.Request;
import org.example.socket.Response;
import org.example.socket.Server;

public class Login {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField email_input;

    @FXML
    private PasswordField password_input;

    @FXML
    private Text error_login;
    public static Parent root;
    public static Login CurrentInstance;
//    private ActionEvent currentEvent;
    @FXML
    void LoginAction(ActionEvent event) {
        User user = new User();
        user.email = email_input.getText();
        user.password = password_input.getText();
        try {
            Server.out.writeObject(new Request("login", "user", "loginUser", user));
//            currentEvent = event;
        } catch (IOException e) {
            e.printStackTrace();
            error_login.setText("IOException");
        }
    }

    public void loginUser(Response res) {
        if (res.status == 200) {
            Constants.user = (User) res.body;
            try {
                root = FXMLLoader.load(Main.class.getResource("layout.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
                error_login.setText("IOException");
            }

            Stage stage = Constants.stage;
            Scene scene = new Scene(root);
            Constants.ImportStyle(scene, this);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setMinWidth(1300);
            stage.show();
        } else {
            error_login.setText(res.message);
        }
    }

    @FXML
    void RegisterAction(ActionEvent event) {

    }

    @FXML
    void initialize() {
        CurrentInstance = this;
        email_input.setText("m_orgilmn@yahoo.com");
        password_input.setText("123");
    }

}
