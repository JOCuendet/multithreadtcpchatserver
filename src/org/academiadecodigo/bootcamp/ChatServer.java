package org.academiadecodigo.bootcamp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class ChatServer {

    private final int portNumber;
    private ServerSocket serverSocket;
    private ExecutorService clientThreadPool;
    private DataOutputStream out;
    private BufferedReader in;
    private Socket clientSocket;
    private CopyOnWriteArrayList<ClientHandler> connectedClientsList = new CopyOnWriteArrayList<>();

    ChatServer(int portNumber) {
        this.clientThreadPool = Executors.newFixedThreadPool(100);
        this.portNumber = portNumber;
        init();
    }

    void init() {
        try {
            serverSocket = new ServerSocket(portNumber);

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("\nServer online and waiting incoming connections\n");
        startListening();
    }

    void removeUserFromList(ClientHandler clientHandler) {
        connectedClientsList.remove(clientHandler);

    }

    void sendMessage(Socket sendingSocket, String message, boolean broadCast) {

        DataOutputStream outMessage;

        if (!broadCast) {
            try {
                outMessage = new DataOutputStream(sendingSocket.getOutputStream());
                outMessage.writeBytes(message + ConsoleColors.RESET);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        if (broadCast) {
            if (connectedClientsList != null) {
                for (ClientHandler clientHandler : connectedClientsList) {
                    try {
                        if (!clientHandler.getClientSocket().equals(sendingSocket)) {
                            outMessage = new DataOutputStream(clientHandler.getClientSocket().getOutputStream());
                            outMessage.writeBytes(message + ConsoleColors.RESET);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    CopyOnWriteArrayList<ClientHandler> getConnectedClientsList() {
        return connectedClientsList;
    }

    private void startListening() {
        try {
            synchronized (this) {
                while (!serverSocket.isClosed()) {

                    clientSocket = serverSocket.accept();
                    ClientHandler clientHandler = new ClientHandler(this, clientSocket);
                    connectedClientsList.add(clientHandler);
                    clientThreadPool.submit(clientHandler);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        clientThreadPool.shutdown();
    }
}
