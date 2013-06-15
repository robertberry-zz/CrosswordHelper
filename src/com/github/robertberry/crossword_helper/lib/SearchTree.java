package com.github.robertberry.crossword_helper.lib;

import android.util.Log;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchTree {
    private static final String TAG = "SearchTree";

    /** Simple immutable node for the tree */
    final private static class Node {
        final public String word;
        final public Optional<Node> left;
        final public Optional<Node> right;

        public Node(String word, Optional<Node> left, Optional<Node> right) {
            this.word = word;
            this.left = left;
            this.right = right;
        }
    }

    private static Optional<Node> nodeFor(List<String> words, Integer left, Integer right) {
        if (left > right) {
            return Optional.absent();
        } else {
            // The central word should be the root node
            Integer middle = (left + right) / 2;
            String word = words.get(middle);
            return Optional.of(new Node(word, nodeFor(words, left, middle - 1),
                    nodeFor(words, middle + 1, right)));
        }
    }

    private final Map<Integer, Optional<Node>> treesByWordLength = new HashMap<Integer, Optional<Node>>();

    /**
     * Given an array of sorted words, constructs the search tree
     *
     * @param wordsByLength Map of words by their length in ascending alphabetical order
     */
    public SearchTree(Map<Integer, ArrayList<String>> wordsByLength) {
        for (Integer length: wordsByLength.keySet()) {
            List<String> words = wordsByLength.get(length);

            treesByWordLength.put(length, nodeFor(words, 0, words.size() - 1));
        }
    }

    public ImmutableSet<String> search(List<Optional<Character>> term) {
        return wordsFor(term, treesByWordLength.get(term.size()));
    }

    public ImmutableSet<String> search(String searchTerm) {
        ArrayList<Optional<Character>> newSearchTerm = new ArrayList<Optional<Character>>();

        for (Character ch : searchTerm.toCharArray()) {
            if (ch == '?') {
                newSearchTerm.add(Optional.<Character>absent());
            } else {
                newSearchTerm.add(Optional.of(ch));
            }
        }

        return search(newSearchTerm);
    }

    private ImmutableSet<String> wordsFor(List<Optional<Character>> term, Optional<Node> node) {
        if (node.isPresent()) {
            Node n = node.get();

            Log.i(TAG, "Matching against " + n.word);

            MatchInformation matchInfo = match(term, n.word);

            Log.i(TAG, "Matched [word: " + matchInfo.matchWord +
                    ", left: " + matchInfo.matchLeft + ", right: " + matchInfo.matchRight + "]");

            ImmutableSet.Builder<String> words = ImmutableSet.builder();

            if (matchInfo.matchWord) {
                words.add(n.word);
            }
            if (matchInfo.matchLeft) {
                words.addAll(wordsFor(term, n.left));
            }
            if (matchInfo.matchRight) {
                words.addAll(wordsFor(term, n.right));
            }

            return words.build();
        } else {
            return ImmutableSet.of();
        }
    }

    final private static class MatchInformation {
        public final boolean matchWord;
        public final boolean matchLeft;
        public final boolean matchRight;

        public MatchInformation(boolean matchWord, boolean matchLeft, boolean matchRight) {
            this.matchWord = matchWord;
            this.matchLeft = matchLeft;
            this.matchRight = matchRight;
        }
    }

    private MatchInformation match(List<Optional<Character>> term, String word) {
        Integer i = 0;
        int wordLength = word.length();

        // If ever encounters a ? in the search term, should search both left and right subtrees
        boolean hasSeenAbsent = false;

        for (Optional<Character> ch : term) {
            boolean isPresent = ch.isPresent();

            if (!isPresent) hasSeenAbsent = true;

            if (i == wordLength) {
                // term is larger than word - continue to search right subtree
                return new MatchInformation(false, hasSeenAbsent, true);
            } else if (isPresent && ch.get() != word.charAt(i)) {
                boolean lt = ch.get() < word.charAt(i);

                // term is not equal to word - search left or right subtree depending on clashing character
                return new MatchInformation(false, hasSeenAbsent || lt, hasSeenAbsent || !lt);
            }
            i++;
        }

        return new MatchInformation(true, hasSeenAbsent, hasSeenAbsent);
    }
}
