package com.digywood.tms.AsynTasks;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.digywood.tms.IBagroundListener;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by NARESH on 11-11-2016.
 */

public class BagroundTask extends AsyncTask<Void, String, String> {
    private ProgressDialog dialog;
    String urlAddress;
    HashMap<String, String> hmap;
    HttpPost httpPost;
    HttpResponse response = null;
    String status;
    String resultString=null;
    IBagroundListener listener;

    public BagroundTask(String url, HashMap<String, String> hmap1, Context activity, IBagroundListener iListener) {
        dialog = new ProgressDialog(activity);
        dialog.setCanceledOnTouchOutside(false);
        this.urlAddress=url;
        this.hmap=hmap1;
        this.listener=iListener;
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage("Loading, please wait.");
        dialog.show();
    }

    @Override
    protected void onPostExecute(String result) {
        if ((dialog !=null) && (dialog.isShowing())) {
            dialog.dismiss();
            listener.bagroundData(result);
        }
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        status = "Connecting to server..";
        publishProgress(status);

        HttpClient httpClient = new DefaultHttpClient();
        httpPost = new HttpPost(urlAddress);
        List<? super NameValuePair> nvps = new ArrayList<>();

		/* Adding Arguments to List Of Name value Pairs  */
        Set<Map.Entry<String, String>> set = hmap.entrySet();
        Iterator<Map.Entry<String, String>> iterator = set.iterator();
        while (iterator.hasNext()) {
            @SuppressWarnings("rawtypes")
            Map.Entry mentry = (Map.Entry) iterator.next();
            nvps.add(new BasicNameValuePair(mentry.getKey().toString(), mentry.getValue().toString()));
        }

        try {
            httpPost.setEntity(new UrlEncodedFormEntity((List<? extends NameValuePair>) nvps));
            response = httpClient.execute(httpPost);

            int responCode = response.getStatusLine().getStatusCode();
            status = getHttpStatusDescription(responCode);
            publishProgress(status);

            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {

                sb.append(line);

                status = "Loading the data..";
                publishProgress(status);
            }

            Log.e("LoginActivity---",sb.toString());
            System.out.println("JSONArray--" + sb.toString());
            is.close();
            resultString = sb.toString();


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
//            HttpEntity entity = response.getEntity();
//            if (entity != null) {
//                httpPost.abort();
//            }
        }

        return resultString;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        dialog.setMessage(values[0]);

    }

    public static String getHttpStatusDescription(int statusCode) {
        switch (statusCode) {
            case HttpStatus.SC_ACCEPTED:
                return "Accepted";
            case HttpStatus.SC_BAD_GATEWAY:
                return "Bad Gateway";
            case HttpStatus.SC_BAD_REQUEST:
                return "Bad Request";
            case HttpStatus.SC_CONFLICT:
                return "Conflict";
            case HttpStatus.SC_CONTINUE:
                return "Continue";
            case HttpStatus.SC_CREATED:
                return "Created";
            case HttpStatus.SC_EXPECTATION_FAILED:
                return "Expectation failed";
            case HttpStatus.SC_FAILED_DEPENDENCY:
                return "Failed dependency";
            case HttpStatus.SC_FORBIDDEN:
                return "Forbidden";
            case HttpStatus.SC_GATEWAY_TIMEOUT:
                return "Gateway timeout";
            case HttpStatus.SC_GONE:
                return "Gone";
            case HttpStatus.SC_HTTP_VERSION_NOT_SUPPORTED:
                return "HTTP version not supported";
            case HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE:
                return "Insufficient space on resource";
            case HttpStatus.SC_INSUFFICIENT_STORAGE:
                return "Insufficient storage";
            case HttpStatus.SC_INTERNAL_SERVER_ERROR:
                return "Internal server error";
            case HttpStatus.SC_LENGTH_REQUIRED:
                return "Length required";
            case HttpStatus.SC_LOCKED:
                return "Locked";
            case HttpStatus.SC_METHOD_FAILURE:
                return "Method failure";
            case HttpStatus.SC_METHOD_NOT_ALLOWED:
                return "Method not allowed";
            case HttpStatus.SC_MOVED_PERMANENTLY:
                return "Moved permanently";
            case HttpStatus.SC_MOVED_TEMPORARILY:
                return "Moved temporarily";
            case HttpStatus.SC_MULTI_STATUS:
                return "Multi status";
            case HttpStatus.SC_MULTIPLE_CHOICES:
                return "Multiple choices";
            case HttpStatus.SC_NO_CONTENT:
                return "No content";
            case HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION:
                return "Non authoritive information";
            case HttpStatus.SC_NOT_ACCEPTABLE:
                return "Not acceptable";
            case HttpStatus.SC_NOT_FOUND:
                return "Not found";
            case HttpStatus.SC_NOT_IMPLEMENTED:
                return "Not implemented";
            case HttpStatus.SC_NOT_MODIFIED:
                return "Not modified";
            case HttpStatus.SC_OK:
                return "Connected";
            case HttpStatus.SC_PARTIAL_CONTENT:
                return "Partial content";
            case HttpStatus.SC_PAYMENT_REQUIRED:
                return "Payment required";
            case HttpStatus.SC_PRECONDITION_FAILED:
                return "Precondition failed";
            case HttpStatus.SC_PROCESSING:
                return "Procecssing";
            case HttpStatus.SC_PROXY_AUTHENTICATION_REQUIRED:
                return "Proxy authentication required";
            case HttpStatus.SC_REQUEST_TIMEOUT:
                return "Request timeout";
            case HttpStatus.SC_REQUEST_TOO_LONG:
                return "Request too long";
            case HttpStatus.SC_REQUEST_URI_TOO_LONG:
                return "Request URI too long";
            case HttpStatus.SC_REQUESTED_RANGE_NOT_SATISFIABLE:
                return "Requested range not satisfiable";
            case HttpStatus.SC_RESET_CONTENT:
                return "Reset content";
            case HttpStatus.SC_SEE_OTHER:
                return "See other";
            case HttpStatus.SC_SERVICE_UNAVAILABLE:
                return "Service unavailable";
            case HttpStatus.SC_SWITCHING_PROTOCOLS:
                return "Switching protocols";
            case HttpStatus.SC_TEMPORARY_REDIRECT:
                return "Temporary redirect";
            case HttpStatus.SC_UNAUTHORIZED:
                return "Unauthorized";
            case HttpStatus.SC_UNPROCESSABLE_ENTITY:
                return "Unprocessable entity";
            case HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE:
                return "Unsupported media type";
            case HttpStatus.SC_USE_PROXY:
                return "Use proxy";
            default:
                return "[status code: " + statusCode + "]";
        }
    }
}
