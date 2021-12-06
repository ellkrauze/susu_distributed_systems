package game;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Server extends UnicastRemoteObject implements ServerInterface {

    private Vector users = new Vector();
    private String previousWord = "";

    public Server() throws RemoteException {
    }

    public boolean login(ClientInterface user) throws IOException {
        System.out.println(user.getName() + "  got connected....");
        user.tell("You have Connected successfully.");
        users.add(user);

        if (users.size() < 3) {
            for (int i = 0; i < users.size(); i++) {
                ClientInterface client = (ClientInterface) users.get(i);
                client.tell("Waiting for players...");
            }
        }
        else {
            for (int i = 0; i < users.size(); i++) {
                ClientInterface client = (ClientInterface) users.get(i);
                client.tell("It's your turn now: enter a town.  You have 30 seconds.");
                client.unmute();
            }
        }
        return true;
    }

    public boolean logout(ClientInterface user) throws RemoteException {
        System.out.println(user.getName() + "  has log out....");
        users.remove(user);
        return true;
    }


    public boolean isInFile(String word, Path filePath) throws RemoteException {

        word = word.replace("\n", "").replace("\r", "");
        try
        {
            String content = Files.readString(filePath);
            if(content.contains(word)) {
                System.out.println(String.format("Found %s in file %s", word, filePath.getFileName()));
                return true;
            }
            System.out.println(String.format("%s is not in file %s", word, filePath.getFileName()));
            return false;
        }
        catch (IOException e)
        {
            System.out.println(String.format("File %s not found.", filePath));
            return false;
        }
    }

    public void appendToFile(String fileName, String data) throws IOException {
        BufferedWriter out = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(fileName, true),
                        StandardCharsets.UTF_8
                )
        );
        out.append(data + System.lineSeparator());
        out.close();
    }

    public Character getFirstChar(String word) throws RemoteException {
        return word.charAt(0);
    }

    public Character getLastChar(String word) throws RemoteException{
        int pos = word.length() - 1;
        char lastChar = word.toUpperCase().charAt(pos);
        if (word.toUpperCase().charAt(pos) == 'Й') {
            return 'И';
        }
        else if (lastChar == 'Ь' || lastChar == 'Ы' || lastChar == 'Ъ') {
            pos--;
        }
        return word.toUpperCase().charAt(pos);
    }

    public boolean wordCheck(String word) throws IOException {
        Path words = Paths.get("D:/", "projects/susu_distributed_systems/3-RPS-application/game", "words.txt");
        Path cache = Paths.get("D:/", "projects/susu_distributed_systems/3-RPS-application/game", "cache.txt");

        if (previousWord != "") {
            if (isInFile(word, words) == true
                    & isInFile(word, cache) == false
            & Character.compare(getLastChar(previousWord), getFirstChar(word)) == 0) {
                System.out.println(String.format("Previous word: %s; Entered word: %s.", previousWord, word));
                appendToFile(String.valueOf(cache.getFileName()), word);
                previousWord = word;
                return true;
            }
        }
        else {
            if (isInFile(word, words) == true
                    & isInFile(word, cache) == false)
            {
                appendToFile(String.valueOf(cache.getFileName()), word);
                previousWord = word;
                return true;
            }
        }
        return false;
    }

    @Override
    public void publish(String userInput) throws RemoteException {
        System.out.println(userInput);
        for (int i = 0; i < users.size(); i++) {
            try {
                ClientInterface client = (ClientInterface) users.get(i);
                client.tell(userInput);
            } catch (Exception e) {
                // problem with the client not connected.
                // Better to remove it
            }
        }
    }

    public Vector getConnected() throws RemoteException {
        return users;
    }
}