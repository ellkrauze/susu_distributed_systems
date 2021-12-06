package game;

import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.*;

public class UI {
    private Client client;
    private ServerInterface server;

    public void doConnect() {
        if (connect.getText().equals("Connect")) {
            if (name.getText().length() < 2) {
                JOptionPane.showMessageDialog(frame, "You need to type a name.");
                return;
            }
            if (ip.getText().length() < 2) {
                JOptionPane.showMessageDialog(frame, "You need to type an IP.");
                return;
            }
            try {
                client = new Client(name.getText());
                client.setGUI(this);
                server = (ServerInterface) Naming.lookup("rmi://" + ip.getText() + "/ellkrauze");
                server.login(client);
                updateUsers(server.getConnected()); // updateUsers(currentListOfUsers);
                connect.setText("Disconnect");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "ERROR, we wouldn't connect....");
            }
        } else {
            // End of session
            updateUsers(null);
            connect.setText("Connect");
            // Better to implement Logout ....
        }
    }

    public void doDisconnect() throws RemoteException {
        if (server.logout(client)) { // Remove this client from usersList
            updateUsers(server.getConnected()); // Get userList from server and update it in UI
            JOptionPane.showMessageDialog(frame, "You've been disconnected!");
            connect.setText("Connect");
        }
        else JOptionPane.showMessageDialog(frame, "Cannot disconnect user.");
    }

    public void sendText() throws IOException, InterruptedException {
        if (connect.getText().equals("Connect")) {
            JOptionPane.showMessageDialog(frame, "You need to connect first.");
            return;
        }
        String userInput = tf.getText();
        tf.wait(30000);
        try {
            if (server.wordCheck(userInput)){
                String info = "[" + name.getText() + "] ";
                tf.setText("");
                // Remove if you are going to implement for remote invocation
                try {
                    server.publish(info + userInput);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else
            {
                showError("Your word did not pass the rules.");
                doDisconnect();
                tf.setText("");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        tf.setEnabled(false);
    }

    public void writeMsg(String st) {
        tx.setText(tx.getText() + "\n" + st);
    }

    public void updateUsers(Vector listOfUsers) {
        DefaultListModel listModel = new DefaultListModel();
        if (listOfUsers != null)
            for (int i = 0; i < listOfUsers.size(); i++) {
                try {
                    String tmp = ((ClientInterface) listOfUsers.get(i)).getName();
                    listModel.addElement(tmp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        lst.setModel(listModel);
    }

    public void showError(String text) {
        JOptionPane.showMessageDialog(frame, text);
    }


    public static void main(String[] args) {
        System.out.println("Hello World !");
        UI c = new UI();
    }

    // User Interface code.
    public UI() {
        frame = new JFrame("Let's Play!");
        JPanel main = new JPanel();
        JPanel top = new JPanel();
        JPanel cn = new JPanel();
        JPanel bottom = new JPanel();
        ip = new JTextField();
        tf = new JTextField();
        tf.setEnabled(false);
        name = new JTextField();
        tx = new JTextArea();
        tx.append("Welcome to the Word Chain Game!\n" +
                "Write your words as the first letter of each is the same as the last letter of your opponent word!\n");
        connect = new JButton("Connect");
        JButton bt = new JButton("Send");
        lst = new JList();
        main.setLayout(new BorderLayout(5, 5));
        top.setLayout(new GridLayout(1, 0, 5, 5));
        cn.setLayout(new BorderLayout(5, 5));
        bottom.setLayout(new BorderLayout(5, 5));
        top.add(new JLabel("Your name: "));
        top.add(name);
        top.add(new JLabel("Server Address: "));
        top.add(ip);
        top.add(connect);
        cn.add(new JScrollPane(tx), BorderLayout.CENTER);
        cn.add(lst, BorderLayout.EAST);
        bottom.add(tf, BorderLayout.CENTER);
        bottom.add(bt, BorderLayout.EAST);
        main.add(top, BorderLayout.NORTH);
        main.add(cn, BorderLayout.CENTER);
        main.add(bottom, BorderLayout.SOUTH);
        main.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Events
        connect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doConnect();
            }
        });
        
        bt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    sendText();
                } catch (IOException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });
        tf.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    sendText();
                } catch (IOException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });

        frame.setContentPane(main);
        frame.setSize(600, 600);
        frame.setVisible(true);
    }

    JTextArea tx;
    JTextField tf, ip, name;
    JButton connect;
    JList lst;
    JFrame frame;
}