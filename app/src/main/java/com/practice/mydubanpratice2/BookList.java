package com.practice.mydubanpratice2;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class BookList extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.book_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private static final String READ_DATA = "readData";

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_book_list, container, false);
            final JSONObject books = readDataFromFile();

            final ListView bookList = (ListView) rootView.findViewById(R.id.bookList);

            bookList.setAdapter(new BaseAdapter() {
                @Override
                public int getCount() {
                    return books.optJSONArray("books").length();
                }

                @Override
                public JSONObject getItem(int position) {
                    return (JSONObject) books.optJSONArray("books").opt(position);
                }

                @Override
                public long getItemId(int position) {
                    return 0;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    final View bookView = inflater.inflate(R.layout.list_item_book, parent, false);
                    final ImageView bookCover = (ImageView) bookView.findViewById(R.id.bookCover);
                    final TextView bookName = (TextView) bookView.findViewById(R.id.bookName);
                    final RatingBar rating = (RatingBar) bookView.findViewById(R.id.rating);
                    final TextView bookInfo = (TextView) bookView.findViewById(R.id.bookInfo);

                    final JSONObject book = getItem(position);

                    bookCover.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_default_cover));
                    bookName.setText(book.optString("title"));
                    rating.setRating((float) (book.optJSONObject("rating").optDouble("average") / 2));
                    bookInfo.setText(TextUtils.join("/", new String[]{
                            book.optJSONArray("author").optString(0),
                            book.optString("publisher"),
                            book.optString("pubdate")}));
                    return bookView;
                }
            });
            return rootView;
        }

    public JSONObject readDataFromFile() {

        final InputStream inputStream = getActivity().getResources().openRawResource(R.raw.data);
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
    }
}
