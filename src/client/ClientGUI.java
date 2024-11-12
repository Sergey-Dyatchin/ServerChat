package client;

import server.ServerWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ClientGUI extends JFrame {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;
    private static final String NAME_CLIENT = "Window client chat";
    private ServerWindow serverWindow;
    private boolean connected;
    private String name;
    private JTextArea history;
    private JTextField ipAddress, port, login, message;
    private JPasswordField password;
    private JButton buttonLogin, buttonSend;
    private JPanel connectedPanel;

    public ClientGUI(ServerWindow serverWindow, int offsetX) {
        this.serverWindow = serverWindow;
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setTitle(NAME_CLIENT);
        setLocation(serverWindow.getX() + offsetX, serverWindow.getY());
        createPanel();
        setVisible(true);

    }

    private void createPanel() {
        add(createConnectedPanel(), BorderLayout.NORTH);
        add(createHistory());
        add(createBottom(), BorderLayout.SOUTH);
    }

    private Component createConnectedPanel() {
        connectedPanel = new JPanel(new GridLayout(2, 3));
        ipAddress = new JTextField("127.0.0.1");
        port = new JTextField("8081");
        login = new JTextField("User name");
        password = new JPasswordField("123");
        buttonLogin = new JButton("Login");
        buttonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectServer();
            }
        });
        connectedPanel.add(ipAddress);
        connectedPanel.add(port);
        connectedPanel.add(new JPanel());
        connectedPanel.add(login);
        connectedPanel.add(password);
        connectedPanel.add(buttonLogin);

        return connectedPanel;
    }

    private Component createHistory() {
        history = new JTextArea();
        history.setEditable(false);
        return new JScrollPane(history);
    }

    private Component createBottom() {
        JPanel panel = new JPanel(new BorderLayout());
        message = new JTextField();
        message.addKeyListener(new KeyAdapter() {
                                   public void keyPressed(KeyEvent e) {
                                       if (e.getKeyChar() == '\n') {
                                           sendMessage();
                                       }
                                   }
                               });
        buttonSend = new JButton("Send");
        buttonSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        panel.add(message);
        panel.add(buttonSend, BorderLayout.EAST);
        return panel;
    }

    public void showMessage(String text) {
        addHistory(text);
    }

    private void connectServer() {
        if (serverWindow.connectUser(this)) {
            addHistory("Successful connection");
            connectedPanel.setVisible(false);
            connected = true;
            name = login.getText();
            String servHistory = serverWindow.getHistory();
            if (servHistory != null) {
                addHistory(servHistory);
            }
        } else {
            addHistory("Connection error");
        }
    }

    public void disconnect(boolean initServ) {
        if (connected) {
            connectedPanel.setVisible(true);
            connected = false;
            addHistory("Disconnection");
            if (!initServ) {
                serverWindow.disconnect(this);
            }
        }
    }

    private void addHistory(String text) {
        history.append(text + "\n");
    }

    public void sendMessage() {
        if (connected) {
            String text = message.getText();
            if (!text.isEmpty()) {
                serverWindow.publishMessage(name + ": " + text);
                message.setText("");
            } else {
                addHistory("You are not connected");
            }
        }
    }

}
