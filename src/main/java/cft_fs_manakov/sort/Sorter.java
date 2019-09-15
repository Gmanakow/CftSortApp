package cft_fs_manakov.sort;

import cft_fs_manakov.*;
import com.sun.istack.internal.NotNull;

import java.io.IOException;
import java.util.ArrayList;

public abstract class Sorter<T> {

    public abstract void sort() throws IncorrectInputException, IOException;

    class Couple<P> {
        P criticalValue;
        int pos;

        Couple(
                P criticalValue,
                int pos
        ) {
            this.criticalValue = criticalValue;
            this.pos = pos;
        }
    }

    abstract Couple<T> findCriticalValue(@NotNull ArrayList<T> list) throws IncorrectInputException;
}
