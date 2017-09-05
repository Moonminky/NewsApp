package com.example.android.booklists;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static com.example.android.booklists.BookListActivity.LOG_TAG;

/**
 * Created by Eleonore on 12/07/2017.
 */

public class QueryUtils {

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return an {@link ArrayList} object by parsing out information
     * about the first book from the input bookJSON string.
     */
    static ArrayList<Book> extractBooks(String bookJSON) {

        // Create an empty ArrayList that we can start adding books to
        ArrayList<Book> books = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(bookJSON);

            if (baseJsonResponse.has("items")) {
                JSONArray itemsArray = baseJsonResponse.getJSONArray("items");

                // looping through All items
                for (int i = 0; i < itemsArray.length(); i++) {
                    String title;
                    String author;
                    String publishedDate;

                    // Extract out the first item (which is a book)
                    JSONObject currentItem = itemsArray.getJSONObject(i);
                    JSONObject volumeInfo = currentItem.getJSONObject("volumeInfo");

                    // Extract out the title, author, average rating and publishedDate
                    title = volumeInfo.getString("title");
                    if (volumeInfo.has("authors")) {
                        // parse the authors field
                        JSONArray authorArray = volumeInfo.getJSONArray("authors");

                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(authorArray.getString(0));
                        for (int j = 1; j < authorArray.length(); j++) {
                            String currentString = authorArray.getString(j);
                            stringBuilder.append(", " + currentString);
                        }
                        author = stringBuilder.toString();
                    } else {
                        // Authors placeholder text (e.g. "Author N/A")
                        author = "No author specified";
                    }

                    if (volumeInfo.has("publishedDate")) {
                        publishedDate = volumeInfo.getString("publishedDate");
                    } else {
                        publishedDate = "";
                    }

                    // Create a new {@link Book} object
                    Book book = new Book(title, author, publishedDate);
                    books.add(book);
                }
            } else {
                //nothing to do here
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the book JSON results", e);
        }
        return books;

    }

    /**
     * Query the USGS dataset and return an {@link ArrayList<Book>} object
     */
    public static ArrayList<Book> fetchBookData(String requestUrl) {
        Log.i(LOG_TAG, "fetchBookData() has been called");
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        ArrayList<Book> books = extractBooks(jsonResponse);

        // Return the {@link Event}
        return books;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    public static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
