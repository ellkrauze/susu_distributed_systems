import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static final int PORT = 19000;
    public static final String HOST = "localhost";

    public static String getUserInput() {
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        String text="";
        String line ="";

        while (reader.hasNextLine() == true){
            line = reader.nextLine();
            if(line.equals("-1")) break;
            text += line + "\n"; 
        }
        reader.close();

        return text; 
    }

    public static void main(String[] args) {
        Socket socket = null;
        try {
            socket = new Socket(HOST, PORT);
            try (InputStream in = socket.getInputStream(); OutputStream out = socket.getOutputStream()) {
                System.out.println(String.format("[INFO] Successfully connected to server %s:%s. Enter your text. To stop user input, enter -1: ", HOST, PORT));
                String line = getUserInput();
                System.out.println(String.format("[INFO] Your text: %s", line));
                out.write(line.getBytes());
                out.flush();
                System.out.println("[INFO] Client text is sent.");

                byte[] buf = new byte[32 * 1024];
                int readBytes = in.read(buf);
                System.out.printf("Server > %s", new String(buf, 0, readBytes));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
