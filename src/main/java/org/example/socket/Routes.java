package org.example.socket;

import javafx.application.Platform;
import org.example.javafx.Constants;
import org.example.javafx.Layout;
import org.example.javafx.Login;

import javax.sound.sampled.AudioInputStream;

public class Routes {
    public static void Handle(Response response) {
        AudioInputStream ais;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (response.func.equals("chat")) {
                    Constants.HandleChat(response);
                    return;
                }
                if (response.status != 200) {
                    return;
                }
                switch (response.func) {
                    case "fetchServer" -> Layout.CurrentInstance.fetchServer(response);
                    case "findServer" -> Layout.CurrentInstance.findServer(response);
                    case "fetchMessage" -> Layout.CurrentInstance.fetchMessage(response);
                    case "loginUser" -> Login.CurrentInstance.loginUser(response);
                    case "joinChannel" -> Layout.CurrentInstance.joinChannel(response);
                    default -> {
                    }
                }
            }
        });


    }
}
