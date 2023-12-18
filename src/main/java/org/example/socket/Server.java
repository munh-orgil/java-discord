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

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Listener();
            }
        });
        thread.start();
    }

    public static void Listener() {
        Response res;
        Message msg;
        while(true) {
            try {
                res = (Response) in.readObject();
                Routes.Handle(res);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
