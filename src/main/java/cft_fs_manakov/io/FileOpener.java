package cft_fs_manakov.io;

import cft_fs_manakov.io.reader.IntReader;
import cft_fs_manakov.io.reader.Reader;
import cft_fs_manakov.io.reader.StringReader;
import cft_fs_manakov.sort.SortingDataType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class FileOpener{
    private List<Reader> readers = null;
    private Writer writer = null;

    public FileOpener(
            String outPutFileName,
            Stack<String> inputFileNames,
            SortingDataType type
    ) throws IOException {
        try{
            this.readers = new ArrayList<>();
            if (type == SortingDataType.Integer) {
                for (String inputFileName : inputFileNames) {
                    this.readers.add(
                            new IntReader(inputFileName)
                    );
                }
            } else {
                for (String inputFileName : inputFileNames) {
                    this.readers.add(
                            new StringReader(inputFileName)
                    );
                }
            }

            this.writer = new Writer(outPutFileName);
        } catch (IOException e){
            System.out.println("Exception + " + e.getMessage());
            throw e;
        }
    }

    public List<Reader> getReaders() {
        return this.readers;
    }

    public Writer getWriter() {
        return this.writer;
    }

    public void closeWriter(){
        this.writer.close();
    }
    public void closeReaders(){
        for (Reader reader : readers){
            reader.close();
        }
    }
    public void closeAll(){
        closeWriter();
        closeReaders();
    }



}