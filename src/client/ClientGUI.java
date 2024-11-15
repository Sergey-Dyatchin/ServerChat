package client;

import server.Server;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ClientGUI extends JFrame implements ClienView {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;
    private static final String NAME_CLIENT = "Window client chat";

    private JTextArea history;
    private JTextField ipAddress, port, login, message;
    private JPasswordField password;
    private JButton buttonLogin, buttonSend;
    private JPanel connectedPanel;

    private Client client;

    public ClientGUI(Server server, int offsetX, int offsetY) {
        client = new Client(this, server);
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setTitle(NAME_CLIENT);
        setLocation(offsetX, offsetY);
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
                connectToServer();
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
                    sendMessages();
                }
            }
        });
        buttonSend = new JButton("Send");
        buttonSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessages();
            }
        });
        panel.add(message);
        panel.add(buttonSend, BorderLayout.EAST);
        return panel;
    }

    private void sendMessages() {
        client.sendMessage(message.getText());
        message.setText("");
    }


    private void addHistory(String text) {
        history.append(text + "\n");
    }


    @Override
    public void showMessage(String message) {
        addHistory(message);
    }

    @Override
    public void disconnectFromServer(boolean initServ) {
        setVisibleConnectedPanel(true);
        if (!initServ) {
            client.disconnect(initServ);
        }
    }

    private void connectToServer() {
        if (client.connectToServer(login.getText())) {
            setVisibleConnectedPanel(false);
        }
    }

    void setVisibleConnectedPanel(boolean visible) {
        connectedPanel.setVisible(visible);
    }


}
