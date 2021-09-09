import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client {
    public static final int PORT = 19000;
    public static final String HOST = "localhost";

    public static void main(String[] args) throws Exception {
        Socket socket = null;
        try {
            socket = new Socket(HOST, PORT);
            try (InputStream in = socket.getInputStream(); OutputStream out = socket.getOutputStream()) {
                System.out.println(String.format("[INFO] Successfully connected to server %s:%s.", HOST, PORT));
                
                    System.out.println("Enter your text. To stop user input, enter empty line:");

                    String line = "";
                    Scanner reader = new Scanner(System.in);
                    while((line = reader.nextLine()) != null)
                    {
                        out.write(line.getBytes());
                        out.flush();
                        System.out.println("[INFO] Client text is sent.");
                        byte[] buf = new byte[32 * 1024];
                        int readBytes = in.read(buf);
                        System.out.printf("Server > %s", new String(buf, 0, readBytes));
                        
                        if(line.equals(System.lineSeparator())) break;
                    }
                    
                    
                    /*if (line.length() == 0) {
                        System.out.println("[INFO] Empty line entered. Closing connection.");
                        break;
                    }
                    System.out.println(String.format("[INFO] Your text: %s", line));
                    
                    out.write(line.getBytes());
                    out.flush();
                    System.out.println("[INFO] Client text is sent.");
                    byte[] buf = new byte[32 * 1024];
                    int readBytes = in.read(buf);
                    System.out.printf("Server > %s", new String(buf, 0, readBytes));*/
                
                 
            }
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

}
