package com.github.robertberry.crossword_helper.tasks;

import android.os.AsyncTask;
import com.github.robertberry.crossword_helper.lib.SearchTree;

import java.util.Set;

public class SearchTask extends AsyncTask<String, Void, Set<String>> {
    private final SearchTree searchTree;

    public SearchTask(SearchTree searchTree) {
        this.searchTree = searchTree;
    }

    @Override
    protected Set<String> doInBackground(String... params) {
        return searchTree.search(params[0]);
    }
}
