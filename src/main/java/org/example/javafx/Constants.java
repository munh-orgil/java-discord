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
import org.example.socket.VoiceServer;

import javax.sound.sampled.*;
import java.io.IOException;

public class Constants {

    public static AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
    public static AudioFormat format;
    public static int port = 50005;
    public static float rate = 44100.0f;
    public static int channels = 2;
    public static int sampleSize = 16;
    public static boolean bigEndian = true;
    public static TargetDataLine targetDataLine;
    public static DataLine.Info sourceDataLineInfo;
    public static SourceDataLine sourceDataLine;
    public static DataLine.Info targetDataLineInfo;
    public static User user;
    public static Server selectedServer;
    public static VoiceChannel selectedVoiceChannel;
    public static TextChannel selectedTextChannel;
    public static Stage stage;

    public static void InitVoice() {
        try {
            format = new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize / 8) * channels, rate,
                    bigEndian);
            sourceDataLineInfo = new DataLine.Info(SourceDataLine.class, format);
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(sourceDataLineInfo);
            sourceDataLine.open(format);
            sourceDataLine.start();
//            FloatControl volumeControl = (FloatControl) sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN);
//            volumeControl.setValue(-20f);

            targetDataLineInfo = new DataLine.Info(TargetDataLine.class, format);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }
    public static void ImportStyle(Scene scene, Object obj) {
        scene.getStylesheets().add(obj.getClass().getResource("styles.css").toExternalForm());
    }

    public static void Listener() {
        byte[] receiveData = new byte[50000];
        try {
            while (selectedVoiceChannel != null && VoiceServer.in.read(receiveData) != -1) {
                FloatControl volumeControl = (FloatControl) sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN);
                volumeControl.setValue(-20f);
                sourceDataLine.write(receiveData, 0, receiveData.length);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void Speak() {
        if (user.isMuted.equals(0L)) {
            try {
                targetDataLine = (TargetDataLine) AudioSystem.getLine(targetDataLineInfo);
                targetDataLine.open(format);
                targetDataLine.start();
            } catch (LineUnavailableException e) {
                throw new RuntimeException(e);
            }
            byte[] data = new byte[50000];
            while (selectedVoiceChannel != null) {
                try {
                    targetDataLine.read(data, 0, data.length);
                    VoiceServer.out.write(data);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    public static void StopSpeak() {
        targetDataLine.stop();
    }
    public static void LeaveSpeak() {
        targetDataLine.close();
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
