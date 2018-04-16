package com.digywood.tms;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

/**
 * Created by Shashank on 01-03-2018.
 */

public class SaveJSONdataToFile {

    public static String objectToFile(String path, String object) throws IOException {

        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File data = new File(path);
        if (!data.createNewFile()) {
            data.delete();
            data.createNewFile();
        }
        FileOutputStream outputStream = new FileOutputStream(data);
        outputStream.write(object.getBytes());
        outputStream.close();
        return path;
    }

    public static byte[] bytesFromFile(String path) throws IOException, ClassNotFoundException {
        byte[] buffer = null;
        File data = new File(path);
        if(data.exists()) {
//            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(data));
            Log.e("FileName",data.getName());
            FileInputStream is = new FileInputStream(data);
            int size = is.available();
            buffer = new byte[size];
            Log.e("FileName",""+buffer.length);
            is.read(buffer);
            is.close();

        }
        return buffer;
    }
}
