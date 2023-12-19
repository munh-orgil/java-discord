package org.example.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Server {
    public static Socket socket;
    public static ObjectInputStream in;
    public static ObjectOutputStream out;
    public static String ipAddress;
    public static Integer retry = 0;
    public static void Init() {
        retry++;
        try {
            socket = new Socket(ipAddress, 5000);
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            if (retry > 1) {
                e.printStackTrace();
            } else {
                throw new RuntimeException(e);
            }
        }
        new Thread(Server::Listener).start();
    }

    public static void Listener() {
        Response res;
        while (true) {
            try {
                res = (Response) in.readObject();
                Routes.Handle(res);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                break;
            }
        }
        try {
            Thread.sleep(5 * 1000);
            if (retry < 10) {
                Init();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
