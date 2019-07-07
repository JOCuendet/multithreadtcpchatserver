package org.academiadecodigo.bootcamp;

import java.net.Socket;

public class CommandList {
    private ChatServer chatServer;
    private Socket clientSocket;

    CommandList(ChatServer chatServer, Socket clientSocket) {
        this.chatServer = chatServer;
        this.clientSocket = clientSocket;
    }

    public String displayList(String message) {
        String[] options = message.split(" ");
        if (options[1].equals("-users")) {
            StringBuilder clientsList = new StringBuilder();
            for (ClientHandler list : chatServer.getConnectedClientsList()) {
                clientsList.append(list.getAlias());
                clientsList.append(" | ");
            }
            clientsList.substring(clientsList.length() - 2);

            chatServer.sendMessage(clientSocket, "" + clientsList, false);
            return "command /list -users called";
        }
        if (options[1].equals("-colors")) {
            StringBuilder colorsList = new StringBuilder();
            for (UserColors colors : UserColors.values()) {
                colorsList.append(colors.getColor());
                colorsList.append(colors.name());
                colorsList.append(ConsoleColors.RESET);
                colorsList.append(" | ");
            }

            chatServer.sendMessage(clientSocket, "" + colorsList, false);
            return "command /list -colors called";
        }
        return "";
    }
}
