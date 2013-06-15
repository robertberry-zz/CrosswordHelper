package com.github.robertberry.crossword_helper.tasks;

import android.os.AsyncTask;
import com.github.robertberry.crossword_helper.util.Either;
import com.github.robertberry.crossword_helper.util.Left;
import com.github.robertberry.crossword_helper.util.Right;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ReadUKACDTask extends AsyncTask<InputStream, Void, Either<Throwable, ArrayList<String>>> {
    @Override
    protected Either<Throwable, ArrayList<String>> doInBackground(InputStream... params) {
        InputStream inputStream = params[0];

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;

        // Skip the UKACD copyright notice
        try {
            while ((line = reader.readLine()) != null && !line.startsWith("-"))
                if (isCancelled())
                    break;

            ArrayList<String> words = new ArrayList<String>();

            while ((line = reader.readLine()) != null) {
                if (isCancelled())
                    break;
                words.add(line);
            }

            return new Right<Throwable, ArrayList<String>>(words);
        } catch (IOException error) {
            return new Left<Throwable, ArrayList<String>>(error);
        }
    }
}
