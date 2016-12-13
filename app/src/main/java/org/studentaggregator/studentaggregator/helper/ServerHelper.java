package org.studentaggregator.studentaggregator.helper;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

public class ServerHelper extends AsyncTask<Void, Void, Boolean> {

    private String url;
    private ContentValues contentValues;
    GetUserCallback getUserCallback;

    private static final String LOG_TAG = "mylog";
    private String reply;

    public ServerHelper(String url, ContentValues contentValues, GetUserCallback getUserCallback) {
        this.contentValues = contentValues;
        this.url = url;
        this.reply = null;
        this.getUserCallback = getUserCallback;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        reply = executePostRequest(url, contentValues);
        return true;
    }//doInBackground

    @Override
    protected void onPostExecute(final Boolean success) {
        getUserCallback.done();
    }//onPostExecute

    public String executePostRequest(String targetURL, ContentValues params) {

        params.put("institute", 1);
        String urlParameters;
        urlParameters = paramsToUrlString(params);

        Log.d(LOG_TAG, "ServerHelper - post data = " + urlParameters);

        HttpURLConnection connection = null;
        try {
            //Create connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length", "" +
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));

            String line, reply = "";
            StringBuilder response;
            response = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
                reply += line;
            }

            Log.d("mylog", "ServerHelper - reply from server = " + reply);

            rd.close();
            return reply;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }//executePostRequest

    public String paramsToUrlString(ContentValues params) {

        String data = "";
        Set<Map.Entry<String, Object>> s = params.valueSet();

        for (Object obj : s) {
            Map.Entry me = (Map.Entry) obj;
            String key = me.getKey().toString();
            String value = me.getValue().toString();

            try {
                data += (URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8") + "&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        // Removing last char from data:
        return (data.substring(0, data.length() - 1));
    }//paramsToUrlString

    public String getReply() {
        return reply;
    }//getReply


}//ServerHelper