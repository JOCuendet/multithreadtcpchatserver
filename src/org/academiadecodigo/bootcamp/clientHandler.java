package org.academiadecodigo.bootcamp;

import java.io.*;
import java.net.Socket;

public class clientHandler implements Runnable {

    private Socket clientSocket;
    private DataOutputStream out;
    private BufferedReader in;

    public clientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public synchronized void run() {

        //Thread.currentThread().setName("Skorp");

        System.out.println(Thread.currentThread().getName() + " Just logged In");
    while(clientSocket != null) {
        synchronized (this) {
            try {
                this.out = new DataOutputStream(clientSocket.getOutputStream());
                this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String message;
                if((message = in.readLine()) == null)
                {
                    closeall();
                    System.out.println("client left.");
                    return;
                }
                if(!(message.equals("") || message.equals(" "))){
                    System.out.println(message);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    }

    private void closeall() {
        close(out);
        close(in);
        close(clientSocket);
    }

    private void close(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
