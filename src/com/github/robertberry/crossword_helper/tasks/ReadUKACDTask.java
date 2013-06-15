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
import java.util.HashMap;
import java.util.Map;

public class ReadUKACDTask extends AsyncTask<InputStream, Void, Either<Throwable, Map<Integer, ArrayList<String>>>> {
    @Override
    protected Either<Throwable, Map<Integer, ArrayList<String>>> doInBackground(InputStream... params) {
        InputStream inputStream = params[0];

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;

        // Skip the UKACD copyright notice
        try {
            while ((line = reader.readLine()) != null && !line.startsWith("-"))
                if (isCancelled())
                    break;

            Map<Integer, ArrayList<String>> wordsByLength = new HashMap<Integer, ArrayList<String>>();

            while ((line = reader.readLine()) != null) {
                if (isCancelled())
                    break;
                if (!wordsByLength.containsKey(line.length())) {
                    wordsByLength.put(line.length(), new ArrayList<String>());
                }
                wordsByLength.get(line.length()).add(line);
            }

            return new Right<Throwable, Map<Integer, ArrayList<String>>>(wordsByLength);
        } catch (IOException error) {
            return new Left<Throwable, Map<Integer, ArrayList<String>>>(error);
        }
    }
}
