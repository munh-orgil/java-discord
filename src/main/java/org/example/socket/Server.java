package org.example.socket;

import org.example.javafx.Constants;
import org.example.javafx.Layout;
import org.example.modules.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Server {
    public static Socket socket;
    public static ObjectInputStream in;
    public static ObjectOutputStream out;
    public static void Init(String ipAddress) throws IOException {
        socket = new Socket(ipAddress, 5000);
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());
    }

    public static void MessageListener() {
        try {
            Response res = (Response) in.readObject();
            if (res.status == 200) {
                Message msg = (Message) res.body;
                if (msg.textChannel != null && Constants.selectedTextChannel != null && Constants.selectedTextChannel.id.equals(msg.textChannel.id)) {
                    Layout.listView.getItems().add(msg);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
