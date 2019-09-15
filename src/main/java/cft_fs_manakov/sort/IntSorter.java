package cft_fs_manakov.sort;

import cft_fs_manakov.IncorrectInputException;
import cft_fs_manakov.io.reader.IntReader;
import cft_fs_manakov.io.reader.Reader;
import cft_fs_manakov.io.Writer;
import cft_fs_manakov.util.AutoFlushLinkedList;
import com.sun.istack.internal.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class IntSorter extends Sorter<Integer> {
    private List<IntReader> readers = null;
    private Writer<Integer> writer = null;
    private int sortModifier = 1;                                            // this is basically a switch which will determinate ascending or descending sort

    public IntSorter(
            List<Reader> readers,
            Writer<Integer> writer,
            SortingMode sortingMode
    ){
        this.readers = new ArrayList<>();
        for (Reader reader : readers){
            IntReader intReader = (IntReader) reader;
            this.readers.add(intReader);
        }
        this.writer = writer;
        if (sortingMode == SortingMode.Descending) this.sortModifier = -1;
    }

    public void sort() throws IncorrectInputException, IOException {
        ArrayList<LinkedList<Integer>> upList = new ArrayList<>();
        LinkedList<Integer> currLinkList = null;

        for (IntReader reader : readers) {                              // so for every input file we have we create a temporary LinkedList
            currLinkList = new LinkedList<>();                                // which will hold a small amount of it's data and will push give it
            reader.fill(currLinkList);                                        // when it's told to.
            upList.add(currLinkList);                                         //
        }

        ArrayList<Integer> fallList = new ArrayList<>();
        ArrayList<Integer> checkList = new ArrayList<>();
        AutoFlushLinkedList<Integer> finalList = new AutoFlushLinkedList<Integer>(writer);

        for (LinkedList<Integer> list : upList) {
            int element = list.pop();
            fallList.add(element);                                              // bottom slice of those linked list, and we will use it to find the critical element of each list.
            checkList.add(element);                                             // a list of elements to be sure each Linkedlist is sorted. if there is a mistake, drop the broken one
        }

        Couple<Integer> couple;

        while (!readers.isEmpty()) {
            out(fallList);

            couple = findCriticalValue(fallList);                             // here we do so and when we find the lowest value of a slice we
                                                                              // push it into the final list which forms a result data
            finalList.add(couple.criticalValue);

            currLinkList = upList.get(couple.pos);                            //

            if (currLinkList.peek() == null) {
                readers.get(couple.pos).fill(currLinkList);                   // because we can't be sure if we can fit our file into the memory,
            }                                                                 // we won't do it at all. instead we will pinch off small pieces of
            if (currLinkList.peek() == null) {                                // data and refill our lists when needed. when our final list is
                readers.remove(couple.pos);                                   // full, we will flush it's data in the out file. this will work
                fallList.remove(couple.pos);
                upList.remove(couple.pos);
            } else {
                int value = currLinkList.pop();
                if (compare(checkList.get(couple.pos), value)) {
                    fallList.set(                                             //we get a new value for a position where the prev crit was
                            couple.pos,
                            value
                    );
                    checkList.set(                                            // we update our list of checks with each value we get
                            couple.pos,
                            value
                    );
                } else {
                    readers.remove(couple.pos);                               // system of brakes if we find unsorted elem in a list
                    fallList.remove(couple.pos);                              //
                    upList.remove(couple.pos);
                }
            }
        }
        if (!finalList.isEmpty()) {                                           // if after the algorythm is finished we will see that we have some leftovers
            writer.flush(finalList.getList());                                // we will flush them into the out file.
        }
    }

    private void out(ArrayList<Integer>list){
        System.out.println("\n");
        for(Integer elem : list){
            System.out.print(elem + " ");
        }
        System.out.println("\n");
    }


    @Override
    public Couple<Integer> findCriticalValue(@NotNull ArrayList<Integer> list){

        int pos = 0;

        int criticalValue = list.get(pos);
        int current;

        for (int i = 1; i < list.size(); i++) {
            current = list.get(i);
            if (compare(current, criticalValue)) {
                criticalValue = current;
                pos = i;
            }
        }
        return new Couple<Integer>(criticalValue, pos);
    }

    private boolean compare(Integer value1, Integer value2){
        return ((value1-value2) * sortModifier < 0);
    }


}
