package com.github.robertberry.crossword_helper;

import android.app.Activity;
import android.os.Bundle;
import com.github.robertberry.crossword_helper.lib.SearchTree;

import java.io.IOException;
import java.io.InputStream;

public class CrosswordHelper extends Activity {
    private SearchTree searchTree;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        InputStream ukacd = getResources().openRawResource(R.raw.ukacd);

        try {
            searchTree = SearchTree.ofUKACDStream(ukacd);
        } catch (IOException error) {
            // daw

        }
    }
}
