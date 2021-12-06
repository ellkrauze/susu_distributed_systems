package game;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.rmi.*;
import java.util.*;

public interface ServerInterface extends Remote {
    public boolean login(ClientInterface user) throws IOException;
    public boolean logout(ClientInterface user) throws RemoteException;
    public boolean wordCheck(String word) throws IOException;
    public void publish(String userInput) throws RemoteException;
    public void appendToFile(String fileName, String data) throws IOException;
    public Character getFirstChar(String word) throws RemoteException;
    public Character getLastChar(String word) throws RemoteException;
    public Vector getConnected() throws RemoteException;
}
