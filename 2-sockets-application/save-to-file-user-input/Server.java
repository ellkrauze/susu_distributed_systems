import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.commons.io;


public class Server {
    public static final int PORT = 19000;

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        File database = new File("database.txt");

        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("[INFO] Server is available. Waiting for client...");
            Socket socket = serverSocket.accept(); // Wait for client to connect

            System.out.println("Accepted. " + socket.getInetAddress());

            while(true){
                // If client connects, read their data and display it on console
                InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream();
                byte[] buf = new byte[32*1024];
                int readBytes = in.read(buf);
                String line = new String(buf, 0, readBytes);

                if (line == "LIST") {
                    try(FileInputStream inputStream = new FileInputStream(database)) {     
                        String everything = IOUtils.toString(inputStream);
                        out.write(everything.getBytes());
                    }
                    
                }
                else {
                    try(FileWriter writer = new FileWriter(database, true);) { // true - Append to existing file
                        writer.write(line + ";");
                        writer.close();
                        out.write(String.format("[INFO] Message added: %s \n", line).getBytes());
                    }
                    catch(FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                out.flush();
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}