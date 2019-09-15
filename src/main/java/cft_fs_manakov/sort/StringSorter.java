package cft_fs_manakov.sort;

import cft_fs_manakov.IncorrectInputException;
import cft_fs_manakov.io.Writer;
import cft_fs_manakov.io.reader.Reader;
import cft_fs_manakov.io.reader.StringReader;
import cft_fs_manakov.util.AutoFlushLinkedList;
import com.sun.istack.internal.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class StringSorter extends Sorter<String> {
    private List<StringReader> readers = null;
    private Writer<String> writer = null;
    private int sortModifier = 1;                                            // this is basically a switch which will determinate ascending or descending sort

    public StringSorter(
            List<Reader> readers,
            Writer<String> writer,
            SortingMode sortingMode
    ){
        this.readers = new ArrayList<>();
        for (Reader reader : readers){
            StringReader stringReader = (StringReader) reader;
            this.readers.add(stringReader);
        }
        this.writer = writer;
        if (sortingMode == SortingMode.Descending) this.sortModifier = -1;
    }
    
    public void sort() throws IncorrectInputException, IOException{
        ArrayList<LinkedList<String>> upList = new ArrayList<>();
        LinkedList<String> currLinkList = null;

        for (StringReader reader : readers) {                                 // so for every input file we have we create a temporary LinkedList
            currLinkList = new LinkedList<>();                                // which will hold a small amount of it's data and will push give it
            reader.fill(currLinkList);                                        // when it's told to.
            upList.add(currLinkList);                                         //
        }

        ArrayList<String> fallList = new ArrayList<>();
        ArrayList<String> checkList = new ArrayList<>();
        AutoFlushLinkedList<String> finalList = new AutoFlushLinkedList<String>(writer);

        for (LinkedList<String> list : upList) {
            String line = list.pop();
            checkList.add(line);                                              // a list of elements to be sure each Linkedlist is sorted. if there is a mistake, drop the broken one
            fallList.add(line);                                               // this one here is a bottom slice of those linked list, and we willuse it to find the critical element of each list.
        }

        Couple<String> couple;

        while (!readers.isEmpty()) {
            couple = findCriticalValue(fallList);                             // here we do so and when we find the lowest value of a slice we
                                                                              // push it into the final list which forms a result data
            finalList.add(couple.criticalValue);

            currLinkList = upList.get(couple.pos);                            //

            if (currLinkList.peek() == null) {
                    readers.get(couple.pos).fill(currLinkList);               // because we can't be sure if we can fit our file into the memory,
            }                                                                 // we won't do it at all. instead we will pinch off small pieces of
            if (currLinkList.peek() == null) {                                // data and refill our lists when needed. when our final list is
                readers.remove(couple.pos);                                   // full, we will flush it's data in the out file. this will work
                fallList.remove(couple.pos);                                  // because our files are sorted before the work starts.
                upList.remove(couple.pos);
            } else {
                String line = currLinkList.pop();
                if (compare(checkList.get(couple.pos), line)) {
                    fallList.set(                                             //we get a new value for a position where the prev crit was
                            couple.pos,
                            line
                    );
                    checkList.set(                                           // we update our list of checks with each value we get
                            couple.pos,
                            line
                    );
                } else {
                    readers.remove(couple.pos);                               // system of brakes if we find unsorted elem in a list
                    fallList.remove(couple.pos);
                    upList.remove(couple.pos);
                }
            }
        }
        if (!finalList.isEmpty()) {                                           // if after the algorythm is finished we will see that we have some leftovers
            writer.flush(finalList.getList());                                // we will flush them into the out file.
        }
    }

    @Override
    public Couple<String> findCriticalValue(@NotNull ArrayList<String> list){

        int pos = 0;

        String criticalValue = list.get(pos);
        String current;

        for (int i = 1; i < list.size(); i++) {
            current = list.get(i);
            if (compare(current, criticalValue)) {
                criticalValue = current;
                pos = i;
            }
        }
        return new Couple<String>(criticalValue, pos);
    }

    private boolean compare(String value1, String value2){
        return (value1.compareTo(value2) * sortModifier < 0);
    }


}
