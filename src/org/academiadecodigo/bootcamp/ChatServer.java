package org.academiadecodigo.bootcamp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {

    private final int portNumber;
    private ServerSocket serverSocket;
    private ExecutorService fixedPool;
    private Socket clientSocket;

    public ChatServer(int portNumber) {
        this.fixedPool = Executors.newFixedThreadPool(10);
        this.portNumber = portNumber;
        init();
    }

    public void init() {
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }

        startListening();
    }

    private void startListening() {

        try {

            while (!serverSocket.isClosed()) {


                clientSocket = serverSocket.accept();

                        fixedPool.submit(new clientHandler(clientSocket));


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        fixedPool.shutdown();
    }
}
