package server.repository;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileData implements Repository {
    private static String historyPatch = "src/server/repository/history.txt";
    @Override
    public String loadHistory() {
            StringBuilder stringBuilder = new StringBuilder();

            try (FileReader reader = new FileReader(historyPatch);) {
                int c;
                while ((c = reader.read()) != -1) {
                    stringBuilder.append((char) c);
                }
                stringBuilder.delete(stringBuilder.length()-1, stringBuilder.length());
                return  stringBuilder.toString();
            } catch (IOException e){
                e.printStackTrace();
                return null;
            }
        }

    @Override
    public void saveHistory(String text) {
        try (FileWriter writer = new FileWriter(historyPatch, true)){
            writer.write(text + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

