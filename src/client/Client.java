package client;

import server.Server;

public class Client {

    private Server server;
    private boolean connected;
    private String name;
    private ClienView view;

    public Client(ClienView view, Server server) {
        this.view = view;
        this.server = server;
    }

    public boolean connectToServer(String name) {
        this.name = name;
        if (server.connectUser(this)) {
            connected = true;
            String servHistory = server.getHistory();
            if (servHistory != null) {
                printText(servHistory);
            }
            printText("You connection successful");
            return true;
        } else {
            printText("Connection error");
        }
        return false;

    }

    public void disconnect(boolean initServ) {
        if (connected) {
            connected = false;
            printText("Disconnection");
            if (!initServ) {
                server.disconnect(this);
            } else {
                view.disconnectFromServer(initServ);
            }
        }
    }

    private void printText(String text) {
        view.showMessage(text);
    }

    public void sendMessage(String message) {
        if (connected) {
            if (!message.isEmpty()) {
                server.publishMessage(name + ": " + message);
            } else {
                printText("You are not connected");
            }
        }
    }

    public void serverAnswer(String answer) {
        printText(answer);
    }


}
