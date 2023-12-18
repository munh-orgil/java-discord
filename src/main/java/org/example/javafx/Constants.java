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
import org.example.modules.Server;
import org.example.modules.TextChannel;
import org.example.modules.User;
import org.example.modules.VoiceChannel;

public class Constants {
    public static User user;
    public static Server selectedServer = null;
    public static VoiceChannel selectedVoiceChannel = null;
    public static TextChannel selectedTextChannel = null;
    public static void ImportStyle(Scene scene, Object obj) {
        scene.getStylesheets().add(obj.getClass().getResource("styles.css").toExternalForm());
    }
}
