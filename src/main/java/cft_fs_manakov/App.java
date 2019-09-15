package cft_fs_manakov;

import cft_fs_manakov.io.FileOpener;
import cft_fs_manakov.io.reader.IntReader;
import cft_fs_manakov.io.reader.Reader;
import cft_fs_manakov.sort.*;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class App extends Application {
    public static long arrayListSize = 1024*8-1;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage){
        Stack<String> assistant = new Stack<>();
        assistant.addAll(getParameters().getRaw());

        Stack<String> parameters = new Stack<>();

        while(!assistant.isEmpty()) {
            String line = assistant.pop();
            parameters.push(line);
        }

        SortingMode mode = SortingMode.Ascending;
        SortingDataType type = null;
        String outPutFileName = "";

        FileOpener fileOpener;
        Sorter sorter;

        boolean trigger = true;
        try {
            while (!parameters.isEmpty() && trigger) {
                String parameter = parameters.pop();
                switch (parameter) {
                    case ("-a"):
                        mode = SortingMode.Ascending;
                        break;
                    case ("-d"):
                        mode = SortingMode.Descending;
                        break;
                    case ("-s"):
                        type = SortingDataType.String;
                        break;
                    case ("-i"):
                        type = SortingDataType.Integer;
                        break;
                    default:
                        outPutFileName = parameter;
                        trigger = false;
                        break;
                }
            }
            if (parameters.isEmpty() || type == null) {
                throw new IncorrectInputException("wrong parameters input");
            } else {
                fileOpener = new FileOpener(
                        outPutFileName,
                        parameters,
                        type
                );
                if (type == SortingDataType.Integer) {
                    sorter = new IntSorter(
                            fileOpener.getReaders(),
                            fileOpener.getWriter(),
                            mode
                    );
                } else {
                    sorter = new StringSorter(
                            fileOpener.getReaders(),
                            fileOpener.getWriter(),
                            mode
                    );
                }
                sorter.sort();
                fileOpener.closeAll();
            }

        } catch (IncorrectInputException | IOException e){
            System.out.println(e.getMessage());
        } finally {

        }
        System.exit(0);
    }
}
