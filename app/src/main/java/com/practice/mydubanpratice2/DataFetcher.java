package com.practice.mydubanpratice2;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DataFetcher {

    private static final String READ_DATA = "readData";

    public static JSONObject readDataFromFile(Context context) {

        final InputStream inputStream = context.getResources().openRawResource(R.raw.data);
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        final StringBuffer stringBuffer = new StringBuffer();
        String line;
        try {
            while((line =bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            final JSONObject bookData = new JSONObject(stringBuffer.toString());
            Log.d(READ_DATA, bookData.toString());
            return bookData;
        } catch (IOException e) {
            Log.d(READ_DATA, "read error");
        } catch (JSONException e) {
            Log.d(READ_DATA, "convert to Json error");
        }
        return null;

    }

    public static JSONObject fetcherDataFromInternet(String urlStr) {
        JSONObject json = null;
        try {
            final URL url = new URL(urlStr);

            final HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = null;
            final StringBuffer stringBuffer = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            bufferedReader.close();
            json = new JSONObject(stringBuffer.toString());

        } catch (MalformedURLException e) {
            Log.e(READ_DATA, e.getLocalizedMessage(), e);
        } catch (IOException e) {
            Log.e(READ_DATA, e.getLocalizedMessage(), e);
        } catch (JSONException e) {
            Log.e(READ_DATA, e.getLocalizedMessage(), e);
        }
        return json;
    }
}
