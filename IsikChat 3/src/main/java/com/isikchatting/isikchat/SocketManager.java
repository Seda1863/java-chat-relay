package com.isikchatting.isikchat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketManager {
    private static SocketManager instance;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Thread readerThread;
    private MessageListener listener;

    private SocketManager() {
    }

    public static SocketManager getInstance() {
        if (instance == null) {
            instance = new SocketManager();
        }
        return instance;
    }

    public void setSocket(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        startReaderThread();
    }

    private void startReaderThread() {
        readerThread = new Thread(() -> {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    if (listener != null) {
                        listener.onMessageReceived(message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        readerThread.setDaemon(true);
        readerThread.start();
    }

    public void setListener(MessageListener listener) {
        this.listener = listener;
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public interface MessageListener {
        void onMessageReceived(String message);
    }
}
