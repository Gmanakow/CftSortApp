package cft_fs_manakov.io.reader;

import cft_fs_manakov.App;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public abstract class Reader {

    public abstract void fill(LinkedList list);

    public abstract void close();
}
