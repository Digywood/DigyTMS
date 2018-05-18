package com.digywood.tms.AsynTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import com.digywood.tms.DBHelper.DBHelper;
import com.digywood.tms.IDownloadStatus;
import com.digywood.tms.URLClass;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by NARESH on 28-09-2016.
 */

public class DownloadFileAsync extends AsyncTask<Void,String, String> {

    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    Context context;
    private ProgressDialog mProgressDialog;
    ArrayList<String> file_paths,file_names,dwd_paths;
    ArrayList<String> unknownFiles=new ArrayList();
    InputStream input;
    OutputStream output;
    HttpURLConnection con;
    String status;
    DownloadFileAsync dwdtask;
    IDownloadStatus listner;
    String temp_location= URLClass.temppath;
    int original_lenghtOfFile;
    String log_status = null;
    DBHelper helper;

    public DownloadFileAsync(Context c,ArrayList<String> dwdpaths,ArrayList<String> urlslist,ArrayList<String> nameslist,IDownloadStatus iDownloadStatus) {
        super();
        this.context=c;
        this.dwd_paths=dwdpaths;
        this.dwdtask=this;
        mProgressDialog= new ProgressDialog(context);
        mProgressDialog.setCanceledOnTouchOutside(false);
        this.file_paths=urlslist;
        this.file_names=nameslist;
        listner=iDownloadStatus;

    }

    @Override
    protected String doInBackground(Void... aurl) {
        int count;
        String filename = null;
        File folder = new File(temp_location);
        if(!folder.exists())
        {
            folder.mkdirs();
        }

        for(int i=0; i<file_paths.size();i++)
        {
            status="Connecting to server..";
            publishProgress(""+(int)0,status);
            try {

                URL url = new URL(file_paths.get(i));
                con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(30000);
                con.connect();
                int responseCode= con.getResponseCode();
                switch (responseCode)
                {
                    case 200 :  status="Connection success..";
                        publishProgress(""+(int)0,status);
                        break;
                    case 401 :  status="Unauthorized Connection..";
                        publishProgress(""+(int)0,status);
                        break;
                    case 404 :  status="Connection Not Found.."+file_names.get(i);
                        publishProgress(""+(int)0,status);
                        break;
                    case 408 :  status="Connection Time-Out please try again later.."+file_names.get(i);
                        publishProgress(""+(int)0,status);
                        break;
                    case 500 :  status="Internal Server Error try again later..";
                        publishProgress(""+(int)0,status);
                        break;
                    case 503 :  status="Service Unavailable try again later..";
                        publishProgress(""+(int)0,status);
                        break;
                    case 505 :  status="HTTP Version Not Supported..";
                        publishProgress(""+(int)0,status);
                        break;
                    default:    status="";
                        publishProgress(""+(int)0,status);
                        break;
                }

                original_lenghtOfFile = con.getContentLength();

                input = new BufferedInputStream(url.openStream());

                output = new FileOutputStream(temp_location+file_names.get(i));

                status= "File : "+file_names.get(i)+"                          "+(i+1)+" / "+file_paths.size();

                Log.e("DownloadFileAsync", "Lenght of file: " + original_lenghtOfFile +"File path + File Name: "+ temp_location+file_names.get(i));


                filename=file_names.get(i);
                byte data[] = new byte[1024*1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress(""+(int)((total*100)/original_lenghtOfFile),status);
                    output.write(data, 0, count);
                }
            }
            catch (Exception e)
            {
                unknownFiles.add(file_names.get(i));
                Log.e("DownloadFileAsync", "Error in Downloading AsynTask.. " + e.toString());
            }
            finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("DownloadFile----",e.toString());
                }
                if (con != null)
                    con.disconnect();
            }
            moveFile(temp_location,filename,dwd_paths.get(i));
        }

        return filename;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        mProgressDialog.setMessage("Connecting to server..");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mProgressDialog.dismiss();
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException|NetworkOnMainThreadException ignored) {
                    Log.e("DownloadFile----",ignored.toString());
                }
                try {
                    if (con != null)
                        con.disconnect();
                } catch (Exception e) {
                    Log.e("DownloadFile----",e.toString());
                }
            }
        });
        mProgressDialog.show();
    }

    @Override
    protected void onProgressUpdate(String... progress) {
        Log.d("DownloadFileAsync",progress[0]);
        mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        mProgressDialog.setMessage(progress[1]);
    }

    @Override
    protected void onPostExecute(String filename)
    {
        super.onPostExecute(filename);
        //((MainActivity)context). playCommertialAdds();
        mProgressDialog.dismiss();
        listner.downloadStatus("Completed");
    }

    private void moveFile(String inputPath,String inputFile,String outputPath) {

        long lenghtOfFile1=new File(inputPath + inputFile).length();
        Log.d("DownloadFileAsync", "Size of downloaded file in Temp location: " + lenghtOfFile1 + "Original File Size in server " + original_lenghtOfFile);
        if(lenghtOfFile1==original_lenghtOfFile) {
            InputStream in = null;
            OutputStream out = null;
            try {
                //create output directory if it doesn't exist
                File dir = new File(outputPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                in = new FileInputStream(inputPath + inputFile);
                out = new FileOutputStream(outputPath + inputFile);

                byte[] buffer = new byte[1024 * 1024];
                int read;
                long total = 0;
                while ((read = in.read(buffer)) != -1) {
                    total += read;
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile1), "File Moving..." + inputFile);
                    out.write(buffer, 0, read);
                }
                in.close();
                in = null;

                // write the output file
                out.flush();
                out.close();
                out = null;

                // delete the original file
                new File(inputPath + inputFile).delete();


            } catch (FileNotFoundException fnfe1) {
                Log.e("DownloadFile----",fnfe1.toString());
            } catch (Exception e) {
                Log.e("DownloadFile----",e.toString());
            } finally {
                try {
                    if (out != null)
                        out.close();
                    if (in != null)
                        in.close();
                } catch (IOException e) {
                    Log.e("DownloadFile----",e.toString());
                }
            }
        }

    }
}
