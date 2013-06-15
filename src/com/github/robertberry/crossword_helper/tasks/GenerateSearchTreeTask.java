package com.github.robertberry.crossword_helper.tasks;

import android.os.AsyncTask;
import com.github.robertberry.crossword_helper.lib.SearchTree;

import java.util.ArrayList;
import java.util.Map;

public class GenerateSearchTreeTask extends AsyncTask<Map<Integer, ArrayList<String>>, Void, SearchTree> {
    @Override
    protected SearchTree doInBackground(Map<Integer, ArrayList<String>>... params) {
        return new SearchTree(params[0]);
    }
}
