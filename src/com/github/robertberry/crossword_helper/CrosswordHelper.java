package com.github.robertberry.crossword_helper;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import com.github.robertberry.crossword_helper.lib.SearchTree;
import com.github.robertberry.crossword_helper.tasks.GenerateSearchTreeTask;
import com.github.robertberry.crossword_helper.tasks.ReadUKACDTask;
import com.github.robertberry.crossword_helper.tasks.SearchTask;
import com.github.robertberry.crossword_helper.util.Either;
import com.google.common.base.Optional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class CrosswordHelper extends Activity {
    private static final String TAG = "MainActivity";

    private Optional<SearchTree> searchTree = Optional.absent();

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
            protected void onPostExecute(Either<Throwable, Map<Integer, ArrayList<String>>> result) {
                if (result.isLeft()) {
                    Log.e(TAG, "Error loading dictionary: " + result.left().leftValue);
                } else {
                    Map<Integer, ArrayList<String>> dictionary = result.right().rightValue;
                    Integer size = 0;

                    for (ArrayList<String> wordList : dictionary.values()) {
                        size += wordList.size();
                    }

                    Log.i(TAG, "Finished loading dictionary with " + dictionary.size() +
                            " different lengths and a total of " + size + " words");
                    onLoadDictionary(dictionary);
                }
            }
        }.execute(ukacd);
    }

    public void triggerSearch(View view) {
        EditText queryEdit = (EditText) findViewById(R.id.query);
        final String query = queryEdit.getText().toString();

        if (searchTree.isPresent()) {
            Log.i(TAG, "Triggering search for query " + query);
            new SearchTask(searchTree.get()) {
                @Override
                protected void onPostExecute(Set<String> words) {
                    Log.i(TAG, "Search for " + query + " completed");
                    for (String word: words) {
                        Log.i(TAG, word);
                    }
                }
            }.execute(query);
        } else {
            Log.i(TAG, "Search tree has not been generated yet.");
        }

        queryEdit.setText("");
    }

    public void onLoadDictionary(Map<Integer, ArrayList<String>> dictionary) {
        new GenerateSearchTreeTask() {
            @Override
            protected void onPostExecute(SearchTree tree) {
                Log.i(TAG, "Finished generating search tree.");
                // This is OK and does not need synchronisation as the search tree is isolated to the UI thread
                searchTree = Optional.of(tree);
            }
        }.execute(dictionary);
    }
}
