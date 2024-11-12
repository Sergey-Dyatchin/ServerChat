package server;

import client.ClientGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServerWindow extends JFrame {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;
    private static final String NAME_SERVER = "Window server chat";
    private static String historyPatch = "src/server/history.txt";
    private List<ClientGUI> clientGUIList;
    private boolean work;
    private JButton buttonStart, buttonStop;
    private JTextArea history;

    public ServerWindow() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setTitle(NAME_SERVER);
        setLocationRelativeTo(null);
        createPanel();
        clientGUIList = new ArrayList<ClientGUI>();
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
                if (!work) {
                    work = true;
                    addLog("Server start");
                }
            }
        });

        buttonStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (work) {
                    work = false;
                    addLog("Server stop");
                    for (ClientGUI clientGUI : clientGUIList) {
                        clientGUI.disconnect(true);
                    }
                    clientGUIList.clear();
                }
            }
        });

        panel.add(buttonStart);
        panel.add(buttonStop);

        return panel;
    }

    public boolean connectUser (ClientGUI clientGUI) {
        if (work && !clientGUIList.contains(clientGUI)) {
            clientGUIList.add(clientGUI);
            return true;
        } else {
            return false;
        }
    }

    public String getHistory() {
        return loadHistory();
    }

    private String loadHistory() {
        StringBuilder stringBuilder = new StringBuilder();

        try (FileReader reader = new FileReader(historyPatch);) {
            int c;
            while ((c = reader.read()) != -1) {
                stringBuilder.append((char) c);
            }
            stringBuilder.delete(stringBuilder.length()-1, stringBuilder.length());
            return  stringBuilder.toString();
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public void publishMessage(String message) {
        if (work) {
            addHistory(message);
            addLog(message);
            publishClients(message);
        }
    }
    private void publishClients(String message) {
        for (ClientGUI clientGUI : clientGUIList) {
            clientGUI.showMessage(message);
        }
    }

    private void addHistory(String message) {
        try (FileWriter writer = new FileWriter(historyPatch, true)){
            writer.write(message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addLog(String message) {
        history.append(message + "\n");
    }

    public void disconnect(ClientGUI clientGUI) {
        clientGUIList.remove(clientGUI);
    }

}
