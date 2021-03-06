package cft_fs_manakov.io.reader;

import cft_fs_manakov.App;
import cft_fs_manakov.io.reader.Reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class IntReader extends Reader {
    private BufferedReader reader = null;
    private String name = "";

    public IntReader(String line) throws IOException {
        try {
            reader = new BufferedReader(
                    new FileReader(
                            line
                    )
            );
        } catch (IOException e){
            reader = null;
            throw new IOException(line, e);
        }
        name = line;
    }

    @Override
    public void fill(LinkedList list){
        String line = null;
        try {
            line = reader.readLine();
        } catch (IOException e){
            return;
        }

        int i = list.size();
        try {
            while (line != null && i < App.arrayListSize) {
                list.add(
                        Integer.parseInt(line)
                );
                line = reader.readLine();
                i++;
            }
        } catch (IOException | NumberFormatException e){
            return;
        }
    }

    public void close() {
        try {
            reader.close();
        } catch (Exception e) {
            reader = null;
        }
    }
}
