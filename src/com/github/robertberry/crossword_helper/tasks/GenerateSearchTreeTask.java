package com.github.robertberry.crossword_helper.tasks;

import android.os.AsyncTask;
import com.github.robertberry.crossword_helper.lib.SearchTree;

import java.util.ArrayList;

public class GenerateSearchTreeTask extends AsyncTask<ArrayList<String>, Void, SearchTree> {
    @Override
    protected SearchTree doInBackground(ArrayList<String>... params) {
        return new SearchTree(params[0]);
    }
}
