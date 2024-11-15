package server;

import client.Client;
import server.repository.Repository;

import java.util.ArrayList;
import java.util.List;

public class Server {

    private List<Client> clientList;
    private boolean work;
    private ServerView serverView;
    private Repository repository;

    public Server(ServerView serverView, Repository repository) {
        this.serverView = serverView;
        this.repository = repository;
        clientList = new ArrayList<Client>();
    }

    public boolean connectUser (Client client) {
        if (work && !clientList.contains(client)) {
            clientList.add(client);
            return true;
        } else {
            return false;
        }
    }

    public String getHistory() {
        return repository.loadHistory();
    }


    public void publishMessage(String message) {
        if (work) {
            addHistory(message);
            serverView.showMessage(message);
            publishClients(message);
        }
    }
    private void publishClients(String message) {
        for (Client client : clientList) {
            client.serverAnswer(message);
        }
    }

    private void addHistory(String message) {
        repository.saveHistory(message);
    }

    public void disconnect(Client client) {
        clientList.remove(client);
    }

    public void start() {
        if (!work) {
            work = true;
            serverView.showMessage("Server start");
        }
    }

    public void stop() {
        if (work) {
            work = false;
            serverView.showMessage("Server stop");
            for (Client client : clientList) {
                client.disconnect(true);
            }
            clientList.clear();
        }
    }
}
