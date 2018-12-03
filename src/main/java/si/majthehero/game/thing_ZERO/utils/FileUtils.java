package si.majthehero.game.thing_ZERO.utils;

import org.lwjgl.system.CallbackI;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

class FileUtils {

    private FileUtils() {}

    static String loadAsString(String file) {
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String buffer = "";
            while ((buffer = reader.readLine()) != null) {
                result.append(buffer).append("\n");
            }
        } catch (IOException e) {
            System.out.println("ERROR: could not read file.");
            e.printStackTrace();
        }
        return result.toString();
    }

}
