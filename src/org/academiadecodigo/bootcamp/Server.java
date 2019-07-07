package org.academiadecodigo.bootcamp;

import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter port Number");
        String port = scanner.nextLine();

        ChatServer chatServer = new ChatServer(Integer.parseInt(port));
        chatServer.init();
    }
}
