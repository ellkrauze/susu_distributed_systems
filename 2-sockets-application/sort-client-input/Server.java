import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;


public class Server {
    public static final int PORT = 19000;

    public static ArrayList<String> sortData(String input_text) {
        String[] splited_text = input_text.split("\\W+");
        ArrayList<String> output_text = new ArrayList<String>(Arrays.asList(splited_text));
        Collections.sort(output_text);
        return output_text;
    }

    public static void main(String[] args) {
        ServerSocket serverSocket = null;

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

                // Send sorted output with unique values line by line to user
                LinkedHashSet<String> output_text = new LinkedHashSet<String>(sortData(line));

                System.out.println(String.format("[INFO] Client Input: %s", line));
                System.out.println("[INFO] Server Output:");
                /*for (String output_line : output_text) {
                    System.out.println(output_line);
                }*/

                out.write(String.join("\n", output_text).getBytes());
                out.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}