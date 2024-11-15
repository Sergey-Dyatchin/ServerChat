package server;

import server.repository.FileData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ServerWindow extends JFrame implements ServerView {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;
    private static final String NAME_SERVER = "Window server chat";
    private JButton buttonStart, buttonStop;
    private JTextArea history;
    private Server server;

    public ServerWindow() {
        this.server = new Server(this, new FileData());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setTitle(NAME_SERVER);
        setLocationRelativeTo(null);
        createPanel();
        setVisible(true);
    }

    private void createPanel() {
        history = new JTextArea();
        add(history);
        add(createButtons(), BorderLayout.SOUTH);
    }

    private Component createButtons() {
        JPanel panel = new JPanel(new GridLayout(1, 2));
        buttonStart = new JButton("Start server");
        buttonStop = new JButton("Stop server");

        buttonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                server.start();
            }
        });

        buttonStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                server.stop();
            }
        });

        panel.add(buttonStart);
        panel.add(buttonStop);

        return panel;
    }

    private void addLog(String message) {
        history.append(message + "\n");
    }


    @Override
    public void showMessage(String message) {
        addLog(message);
    }

    public Server getServer() {
        return server;
    }
}
