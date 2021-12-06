package game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.Scanner;

public class Test {
    public static Character getFirstChar(String word) throws RemoteException {
        return word.charAt(0);
    }

    public static Character getLastChar(String word) throws RemoteException{
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

    public static boolean isInFile(String word, String fileName) {
        Path filePath = Paths.get("D:/", "projects/susu_distributed_systems/3-RPS-application/game", "words.txt");
        try
        {
            String content = Files.readString(filePath);
            if(content.contains(word)) {
                return true;
            }
            System.out.println(String.format("No such word: %s", word));
            return false;
        }
        catch (IOException e)
        {
            System.out.println(String.format("File %s not found.", fileName));
            return false;
        }
    }

    public static void main(String[] args) throws RemoteException {
        System.out.println(isInFile("Белгород", "D:\\projects\\susu_distributed_systems\\3-RPS-application\\game\\words.txt"));
        System.out.println(getLastChar("Москва") == getFirstChar("Астрахань"));
    }
}
