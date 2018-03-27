package com.digywood.tms;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * Created by Shashank on 23-02-2018.
 */

public class EncryptDecrypt {
    File file;
    final static String key = "squirrel123";

    public static void encrypt(String data, OutputStream os) throws Throwable {
        encryptOrDecrypt(data,Cipher.ENCRYPT_MODE, null, os);
        //for(int i=0;i<=3;i++){
//        System.out.println("File Encrypted.....");
        //
    }

    public static byte[] decrypt(InputStream is, OutputStream os) throws Throwable {
        byte[] bytes = encryptOrDecrypt(null,Cipher.DECRYPT_MODE, is, os);
//        System.out.println("File decrypted.....");
        return bytes;
    }

    public static byte[] encryptOrDecrypt(String data,int mode, InputStream is, OutputStream os) throws Throwable {

        DESKeySpec dks = new DESKeySpec(key.getBytes());
        SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
        SecretKey desKey = skf.generateSecret(dks);
        byte[] bytes = new byte[64];
        Cipher cipher = Cipher.getInstance("DES"); // DES/ECB/PKCS5Padding for SunJCE

        if (mode == Cipher.ENCRYPT_MODE) {
            cipher.init(Cipher.ENCRYPT_MODE, desKey);
            CipherOutputStream cos = new CipherOutputStream(os, cipher);
//            doCopy(cis, os);
            cos.write(data.getBytes());

        } else if (mode == Cipher.DECRYPT_MODE) {
            cipher.init(Cipher.DECRYPT_MODE, desKey);
            CipherOutputStream cos = new CipherOutputStream(os, cipher);
             bytes = doCopy(is, cos);
        }
        return bytes;
    }

    public static byte[] doCopy(InputStream is, OutputStream os) throws IOException {
        byte[] bytes = new byte[64];
        int numBytes;
        /*while ((numBytes = is.read(bytes)) != -1) {
            os.write(bytes, 0, numBytes);
        }*/
        numBytes = is.read(bytes);
        os.flush();
        os.close();
        is.close();
        return bytes;
    }

    public void getFileToEncrypt(String data,String path){
        try {
//            FileInputStream fis = new FileInputStream(file.getName());
//            System.out.println("fis="+fis);
            String name_enc= path+"enc_attempt.json";
            File f = new File(name_enc);
            FileOutputStream fos = new FileOutputStream(f);
            FileInputStream fis = new FileInputStream(name_enc);
            fis.read(data.getBytes());
            Log.e("EncryptDataSize--->",""+data.getBytes().length);
            encrypt(data,fos);
//            fos.write(data.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (Throwable t){
            t.printStackTrace();
        }
    }

    public byte[] getFileToDecrypt(File file){
        Log.e("Encrypt--->", file.getAbsolutePath());
        Bitmap bitmap = null;
        byte[] bytes = new byte[64];
        try {

            FileInputStream fis = new FileInputStream(file);
            String MyDecImgs=android.os.Environment.getExternalStorageDirectory().toString()+ "/DigyTMS/Decrypted/";
            File f = new File(MyDecImgs+file.getName());
            FileOutputStream fos = new FileOutputStream(f);
            bytes = decrypt(fis, fos);
/*            Log.e("Encrypt--->bitmap",""+bytes.length );
            fos.write(bytes);*/
//            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            /*String name_enc=MyDecImgs+f.getName();
            if(!f.exists()){
                file = new File(MyDecImgs,name_enc);
                System.out.println("fis="+fis);
                //FileOutputStream fos = new FileOutputStream("D:"+ File.separator +"Pavitra_Java_WorkSpace"+ File.separator +"imageendrypt"+ File.separator +"WebContent"+ File.separator +"Encrypted images");
                FileOutputStream fos = new FileOutputStream(file);
                bitmap = decrypt(key, fis, fos);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            }*/
            fos.flush();
            fos.close();
    } catch (Throwable e) {
        e.printStackTrace();
    }
        return bytes;
    }
}
