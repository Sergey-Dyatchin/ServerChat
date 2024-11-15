import client.ClientGUI;
import server.ServerWindow;

public class Main {
    public static void main(String[] args) {
        ServerWindow serverWindow = new ServerWindow();
        new ClientGUI(serverWindow.getServer(), 100, 350);
        new ClientGUI(serverWindow.getServer(), 1400,350);
    }
}