package com.gateway.dashboard.predictor;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class AutoComplete {

    private final static String trainingData =
            "Mary had a little lamb its fleece was white as snow;\n" +
            "And everywhere that Mary went, the lamb was sure to go.\n" +
            "It followed her to school one day, which was against the rule;\n" +
            "It made the children laugh and play, to see a lamb at school.\n" +
            "And so the teacher turned it out, but still it lingered near,\n" +
            "And waited patiently about till Mary did appear.\n" +
            "\"Why does the lamb love Mary so?\" the eager children cry; " +
                    "\"Why, Mary loves the lamb, you know\" the teacher did reply.\"";

    // Process training data and create ngrams
    public static Map<String, Map<String, Double>> createNgramMap(String[] words) {
        Map<String, Map<String, Double>> nGram = new HashMap<>();
        for(int i = 0; i <= words.length - 2; i++) {
            if (nGram.containsKey(words[i])) {
                if (nGram.get(words[i]).containsKey(words[i + 1])) {
                    double v = nGram.get(words[i]).get(words[i + 1]);
                    v++;
                    nGram.get(words[i]).put(words[i + 1], v);
                } else {
                    nGram.get(words[i]).put(words[i + 1], 1.0);
                }
            } else {
                nGram.put(words[i], createResult(words[i + 1]));
            }
        }
        return nGram;
    }

    public static Map<String, Map<String, Double>> processTrainingData() {
        // Replace all non alphanumeric characters + whitespace with nothing and then split on whitespace
        String[] words = trainingData.replaceAll("[^a-zA-Z0-9\\s+]", "").split("\\s+");
        return createNgramMap(words);
    }

    public static void main(String[] args) {

        Map<String, Map<String, Double>> nGram = processTrainingData();

        // The number is the n-gram length. The string is the text from the user.
        String searchTerm = "2,the".split(",")[1];

        Map<String, Double> result = nGram.get(searchTerm);
        int size = result.size();
        for(String key : result.keySet()) {
            result.put(key, result.get(key) / size);
        }

        List<Map.Entry<String, Double>> list = new ArrayList<>(result.entrySet());
        list.sort(Map.Entry.comparingByValue());
        Collections.reverse(list);

        String toPrint = list.stream().map(entry -> entry.getKey() +"," + entry.getValue() + ";").collect(Collectors.joining());

    }

    private static final Map<String, Double> createResult(String s) {
        HashMap<String, Double> result = new HashMap<>();
        result.put(s, 1.0);
        return result;
    }

    private static final String prediction(Map<String, Double> h) {
        String key = "";
        double max = 0;
        for(String s : h.keySet()) {
            if(h.get(s) > max) {
                max = h.get(s);
                key = s;
            }
        }
        return key;
    }
    private static final double predValue(Map<String, Double> h) {
        double max = 0;
        for(String s : h.keySet()) {
            if(h.get(s) > max) {
                max = h.get(s);
            }
        }
        return max;
    }

}