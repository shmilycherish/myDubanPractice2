package com.practice.mydubanpratice2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by cyang on 10/20/14.
 */
public class ImageLoader {

    public interface ImageLoaderListener {
        void onImageLoad(Bitmap bitmap);
    }

    public static void loadImage(String url, final ImageLoaderListener imageLoaderListener) {

        new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... params) {
                final String url = params[0];
                try {
                    final URL imageUrl = new URL(url);
                    final HttpURLConnection urlConnection = (HttpURLConnection) imageUrl.openConnection();
                    final InputStream inputStream = urlConnection.getInputStream();
                    return BitmapFactory.decodeStream(inputStream);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                if (imageLoaderListener != null) {
                    imageLoaderListener.onImageLoad(bitmap);
                }
            }
        }.execute(url);
    }
}
