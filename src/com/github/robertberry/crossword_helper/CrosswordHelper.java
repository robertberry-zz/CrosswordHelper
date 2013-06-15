package com.github.robertberry.crossword_helper;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import com.github.robertberry.crossword_helper.lib.SearchTree;
import com.github.robertberry.crossword_helper.tasks.ReadUKACDTask;
import com.github.robertberry.crossword_helper.util.Either;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class CrosswordHelper extends Activity {
    private static final String TAG = "MainActivity";

    private SearchTree searchTree;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        InputStream ukacd = getResources().openRawResource(R.raw.ukacd);

        new ReadUKACDTask() {
            @Override
            protected void onPostExecute(Either<Throwable, ArrayList<String>> result) {
                if (result.isLeft()) {
                    Log.e(TAG, "Error loading dictionary: " + result.left().leftValue);
                } else {
                    ArrayList<String> dictionary = result.right().rightValue;
                    Log.i(TAG, "Finished loading dictionary with " + dictionary.size() + " words");
                    onLoadDictionary(dictionary);
                }
            }
        }.execute(ukacd);
    }

    public void onLoadDictionary(ArrayList<String> dictionary) {

    }
}
