package org.academiadecodigo.bootcamp;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket clientSocket;
    private DataOutputStream out;
    private BufferedReader in;
    private ChatServer chatServer;
    private String alias;
    private boolean firstConnection;
    private String userTextColor;

    public ClientHandler(ChatServer chatServer, Socket clientSocket) {
        userTextColor = "";
        firstConnection = true;
        this.clientSocket = clientSocket;
        this.chatServer = chatServer;

    }

    public String getUserTextColor() {
        return userTextColor;
    }

    public void setUserTextColor(String userTextColor) {
        this.userTextColor = userTextColor;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    private String setRandomAlias() {
        String usr = Thread.currentThread().getName();
        return "User_" + usr.substring(usr.length() - 1);
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String message) {
        String[] str = message.split(" ");
        this.alias = str[1];
    }

    @Override
    public synchronized void run() {

        this.alias = setRandomAlias();


        while (!clientSocket.isClosed() && (clientSocket != null)) {
            synchronized (this) {
                try {
                    this.out = new DataOutputStream(clientSocket.getOutputStream());
                    this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));


                    handleUserInput(in);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void handleUserInput(BufferedReader in) throws IOException {

        if(firstConnection){
            welcomeMessage();
            newUserBroadcast();
            firstConnection = false;
        }

        String message;
        if ((message = in.readLine()) == null) {
            exit();
            return;
        }
        if (message.equals("") || message.equals(" ")) {
            return;
        }
        if (message.equals("/help")) {

            chatServer.sendMessage(clientSocket, helpMessage(), false);
            log("typed help command");
            return;
        }
        if (message.equals("/exit")) {
            String quitMessage = "\n"+ConsoleColors.PURPLE_BOLD_BRIGHT+" #SYS# User: " + getAlias() + " has left the chat.\n";
            chatServer.sendMessage(clientSocket, quitMessage, true);
            exit();
            return;
        }
        if(message.startsWith("/whisper ")){

            String[] whisperdMessage = message.split(" ");

            if(sendWhisper(whisperdMessage)){
                log("Whisper sent @" + whisperdMessage[1]);
                return;
            }
            chatServer.sendMessage(clientSocket, "\n #SYS# user "+whisperdMessage[1]+" don't exist or is offline \n", false);
            return;

        }
        if (message.startsWith("/list")) {
                String[] options = message.split(" ");
                if(options[1].equals("-users")){
                    StringBuilder clientsList = new StringBuilder();
                    for (ClientHandler list : chatServer.getConnectedClientsList()){
                        clientsList.append(list.alias + " | ");
                    }
                    clientsList.substring(clientsList.length() - 2);
                    log("command /list -users called");

                    chatServer.sendMessage(clientSocket, "" + clientsList, false);
                    return;
                }
                if(options[1].equals("-colors")){
                    StringBuilder colorsList = new StringBuilder();
                    for (UserColors colors : UserColors.values()){
                        colorsList.append(colors.getColor()+ colors.name() + ConsoleColors.RESET + " | ");
                    }
                    log("command /list -colors called");

                    chatServer.sendMessage(clientSocket, "" + colorsList, false);
                    return;
                }
        }

        if(message.startsWith("/color ")){
            String[] colorPicked = message.split(" ");
            setUserTextColor(colorPicked[1].toUpperCase());
            return;
        }
        if (message.startsWith("/alias")) {
            String changedUserMessage = "\n"+ConsoleColors.PURPLE_BOLD_BRIGHT+" #SYS# User " + getAlias();
            setAlias(message);
            changedUserMessage += " changed alias to: " + getAlias() + "!\n";

            chatServer.sendMessage(clientSocket, changedUserMessage, true);
            log(changedUserMessage);
            return;
        }

        if(message.endsWith("/") | message.startsWith("/")){
            chatServer.sendMessage(clientSocket, "\n"+ConsoleColors.PURPLE_BOLD_BRIGHT+" #SYS# Command not recognized. \n Use /help for commands \n", false);
            return;
        }

        log(message);
        chatServer.sendMessage(clientSocket,  UserColorMapper.getColor(getUserTextColor()) + getAlias() + ": " + message + "\n", true);
    }

    private boolean sendWhisper(String[] whisperdMessage) {

        for (ClientHandler list : chatServer.getConnectedClientsList()){
            if(whisperdMessage[1].equals(list.alias)){
                StringBuilder messageStr = new StringBuilder("\n #WHISPER FROM "+getAlias()+": ");
                for (int i=2; i<whisperdMessage.length; i++){
                    messageStr.append(whisperdMessage[i]+" ");
                }
                chatServer.sendMessage(list.getClientSocket(), getAlias() + ": " + messageStr + "\n", false);
                return true;
            }
        }
        return false;
    }

    private String helpMessage() {
        return  "" + ConsoleColors.PURPLE_BOLD_BRIGHT +
                "\n" +
                "\n #SYS# help:" +
                "\n /help                           [show help menu]" +
                "\n /exit                           [exit chat]" +
                "\n /whisper username message       [private message @username]" +
                "\n /alias newalias                 [changes your alias/nickname]" +
                "\n /color color                    [changes your text color]" +
                "\n /list -users                    [shows a list of all users connected]" +
                "\n /list -colors                   [list all possible text color]" +
                "\n" +
                "\n";
    }

    private void newUserBroadcast() {
        String newUserBroadcast = "\n"+ConsoleColors.PURPLE_BOLD_BRIGHT +" #SYS# User: " + getAlias() + " joined the chat.\n";
        chatServer.sendMessage(clientSocket, newUserBroadcast, true);
        log(getAlias() + " Just logged In");
    }

    private void welcomeMessage() {
        String welcomeMessage = "\n"+ConsoleColors.PURPLE_BOLD_BRIGHT +" #SYS# Welcome to the super Chat. for help type [/help]\n";
        chatServer.sendMessage(clientSocket, welcomeMessage, false);
    }

    private void log(String message) {
        System.out.println("log: " + getAlias() + " says: " + message);
    }

    private void exit() {
        chatServer.removeUserFromList(this);
        closeall();
        System.out.println("\n"+ConsoleColors.PURPLE_BOLD_BRIGHT +" #SYS# " +getAlias() + " left the chat.\n");
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
