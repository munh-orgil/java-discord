package org.example.javafx;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.example.modules.*;
import org.example.socket.Request;
import org.example.socket.Response;

public class Constants {
    public static User user;
    public static Server selectedServer = null;
    public static VoiceChannel selectedVoiceChannel = null;
    public static TextChannel selectedTextChannel = null;
    public  static Stage stage;
    public static void ImportStyle(Scene scene, Object obj) {
        scene.getStylesheets().add(obj.getClass().getResource("styles.css").toExternalForm());
    }

    public static void HandleChat(Response response) {
        if (response.status != 200 || selectedTextChannel == null) {
            return;
        }
        Message msg = (Message) response.body;
        if (msg.textChannel == null) {
            return;
        }
        if (selectedTextChannel.id.equals(msg.textChannel.id)) {
            Layout.CurrentInstance.messages.addFirst(msg);
            Layout.CurrentInstance.DrawListView();
            Layout.CurrentInstance.textInput.setText("");
        }
    }
}
