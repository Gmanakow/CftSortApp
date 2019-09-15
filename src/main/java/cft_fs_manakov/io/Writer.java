package cft_fs_manakov.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Writer<T> {
    private BufferedWriter writer;
    private String name = "";

    public Writer(String line) throws IOException{
        try{
            writer = new BufferedWriter(
                    new FileWriter(
                            line
                    )
            );
        } catch (IOException e){
            writer = null;
            throw new IOException(line, e);
        }
        name = line;
    }

    public void flush(List<T> list) throws IOException{
        try{
            for (T obj : list){
                writer.write(obj + "\n");
            }
        } catch (IOException e){
            throw new IOException("err with flushing", e);
        }
    }

    public void close(){
        try {
            this.writer.close();
        } catch (Exception e){
            this.writer = null;
        }
    }

    public String getName(){
        return name;
    }
}
