package org.example.socket;

import javafx.application.Platform;
import org.example.javafx.Constants;
import org.example.javafx.Layout;
import org.example.javafx.Login;

public class Routes {
    public static void Handle(Response response) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (response.func.equals("voice")) {
                    return;
                }
                if (response.func.equals("chat")) {
                    Constants.HandleChat(response);
                    return;
                }
                if (response.status != 200) {
                    return;
                }
                switch (response.func) {
                    case "fetchServer":
                        Layout.CurrentInstance.fetchServer(response);
                        break;
                    case "findServer":
                        Layout.CurrentInstance.findServer(response);
                        break;
                    case "fetchMessage":
                        Layout.CurrentInstance.fetchMessage(response);
                        break;
                    case "loginUser":
                        Login.CurrentInstance.loginUser(response);
                    default:
                }
            }
        });


    }
}
