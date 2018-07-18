package com.digywood.tms;

import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
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

        try {
            File enc_file = new File(path);
            if (enc_file.exists()) {
                byte[] enc_bytes=EncryptDecrypt.encrypt(new FileInputStream(enc_file));
                if(enc_file.delete()) {
                    enc_file.createNewFile();
                    FileOutputStream enc_out = new FileOutputStream(path);
                    enc_out.write(enc_bytes);
                    enc_out.close();
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return path;
    }

    public static byte[] bytesFromFile(String path) throws IOException, ClassNotFoundException {
        byte[] buffer = null;
        InputStream is=null;
        try {
            File f=new File(path);
            if(f.exists()) {
                is= EncryptDecrypt.decryptJson(new FileInputStream(f));

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int len;
                byte[] buffer1 = new byte[1024];
                while ((len = is.read(buffer1, 0, buffer1.length)) != -1) {
                    baos.write(buffer1, 0, len);
                }
                baos.flush();
                buffer = baos.toByteArray(); // get the byte array
            }else
            {
                Log.e("PracticeTestAdapter","file is not found:"+path);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return buffer;
    }


}
