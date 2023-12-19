package org.example.socket;

import org.example.javafx.Constants;

import java.io.*;
import java.net.Socket;

public class VoiceServer {
    public static Socket socket;
    public static InputStream in;
    public static OutputStream out;
    public static void Init() {
        try {
            socket = new Socket(Server.ipAddress, 5555);
            in = socket.getInputStream();
            out = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
