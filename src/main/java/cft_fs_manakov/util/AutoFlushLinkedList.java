package cft_fs_manakov.util;

import cft_fs_manakov.App;
import cft_fs_manakov.io.Writer;

import java.util.LinkedList;

public class AutoFlushLinkedList<T> {
    private LinkedList<T> linkedList;
    private Writer<T> writer;

    public AutoFlushLinkedList(
            Writer<T> writer
    ){
        this.writer = writer;
        this.linkedList = new LinkedList<>();
    }

    public void add(T element){
        this.linkedList.add(element);
        if (this.linkedList.size() > App.arrayListSize){
            writer.flush(this.linkedList);
            this.linkedList.clear();
        }
    }

    public int size(){
        return this.linkedList.size();
    }

    public boolean isEmpty(){
        return this.linkedList.isEmpty();
    }

    public LinkedList<T> getList(){
        return this.linkedList;
    }


}
