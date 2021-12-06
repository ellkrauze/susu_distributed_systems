package game;

import java.io.File;
import java.io.PrintWriter;
import java.rmi.*;
import java.rmi.server.*;

public class StartServer {
    public static void main(String[] args) {
        try {
            System.out.println("Hi");
            // System.setSecurityManager(new RMISecurityManager());
            java.rmi.registry.LocateRegistry.createRegistry(1099);

            ServerInterface b = new Server();
            Naming.rebind("rmi://localhost/ellkrauze", b);
            System.out.println("[System] Server is ready.");
            File cache = File.createTempFile("cache", ".txt");
            PrintWriter writer = new PrintWriter("cache.txt", "UTF-8");
            writer.append("");
            writer.close();
            cache.deleteOnExit();
            
        } catch (Exception e) {
            System.out.println("Game Server failed: " + e);
        }
    }
}