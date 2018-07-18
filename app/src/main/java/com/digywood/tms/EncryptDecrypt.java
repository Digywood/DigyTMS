package com.digywood.tms;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * Created by Shashank on 23-02-2018.
 */

public class EncryptDecrypt {
    File file;
    final static String key = "Digy@123";


    public static Bitmap decrypt(InputStream is) throws Throwable {
        byte[] bytes = encryptOrDecrypt(Cipher.DECRYPT_MODE, is);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//        System.out.println("File decrypted.....");
        return bitmap;
    }

    public static InputStream decryptJson(InputStream is) throws Throwable {
        DESKeySpec dks = new DESKeySpec(key.getBytes());
        SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
        SecretKey desKey = skf.generateSecret(dks);

        Cipher cipher = Cipher.getInstance("DES"); // DES/ECB/PKCS5Padding for SunJCE
        cipher.init(Cipher.DECRYPT_MODE, desKey);
        CipherInputStream cos = new CipherInputStream(is, cipher);

        return cos;
    }

    public static byte[] encrypt(InputStream is) throws Throwable {
        byte[] bytes = encryptOrDecrypt(Cipher.ENCRYPT_MODE, is);

        return bytes;
    }


    public static byte[] encryptOrDecrypt(int mode, InputStream is) throws Throwable {


        DESKeySpec dks = new DESKeySpec(key.getBytes());
        SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
        SecretKey desKey = skf.generateSecret(dks);
        byte[] bytes = new byte[1024];
        Cipher cipher = Cipher.getInstance("DES"); // DES/ECB/PKCS5Padding for SunJCE

        if (mode == Cipher.ENCRYPT_MODE) {
            cipher.init(Cipher.ENCRYPT_MODE, desKey);
            CipherInputStream cos = new CipherInputStream(is, cipher);

            bytes = doCopy(cos);
        }

        if (mode == Cipher.DECRYPT_MODE) {
            cipher.init(Cipher.DECRYPT_MODE, desKey);
            CipherInputStream cos = new CipherInputStream(is, cipher);

             bytes = doCopy(cos);
        }
        return bytes;
    }

    public static byte[] doCopy(CipherInputStream is) throws IOException {
        CipherInputStream cis = is;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len;
        byte[] buffer = new byte[1024];
        while ((len = cis.read(buffer, 0, buffer.length)) != -1) {
            baos.write(buffer, 0, len);
        }
        baos.flush();
        byte[] cipherByteArray = baos.toByteArray(); // get the byte array
        return cipherByteArray;
    }
}
