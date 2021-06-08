import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static final int TOP_WORDS_LIMIT = 10;

    public static void main(String[] args) throws IOException {
        String data = new String(Files.readAllBytes(Paths.get("passage.txt")));

        StringTokenizer wordTokenizer = new StringTokenizer(data);
        int totalWordCount = wordTokenizer.countTokens();
        System.out.println("totalWordCount = " + totalWordCount);

        if (totalWordCount > 0) {
            List<String> tokens = new ArrayList<>();
            while (wordTokenizer.hasMoreTokens()) {
                tokens.add(wordTokenizer.nextToken());
            }

            List<Map.Entry<String, Long>> top10words = findTopWords(tokens);

            System.out.println("The top 10 words = " + top10words);

            System.out.println();
            System.out.println("The last sentence on the file that contains the most used word:");
            System.out.println(findLastSentenceWithTopWord(data, top10words));
        }
    }

    private static List<Map.Entry<String, Long>> findTopWords(List<String> tokens) {
        Map<String, Long> topWords = tokens.stream()
                .collect(Collectors.groupingBy(w -> w, Collectors.counting()));
        return topWords.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(TOP_WORDS_LIMIT)
                .collect(Collectors.toList());
    }

    private static String findLastSentenceWithTopWord(String data, List<Map.Entry<String, Long>> top10words) {
        String topWord = top10words.get(0).getKey().toLowerCase();

        StringTokenizer sentenceTokenizer = new StringTokenizer(data, ".");
        List<String> sentenceTokens = new ArrayList<>();
        while (sentenceTokenizer.hasMoreTokens()) {
            sentenceTokens.add(sentenceTokenizer.nextToken());
        }


        List<String> sentenceWithMostUsedWord = new ArrayList<>();
        for (String sentence : sentenceTokens) {
            StringTokenizer wordInSentenceTokenizer = new StringTokenizer(sentence);
            List<String> tokens = new ArrayList<>();
            while (wordInSentenceTokenizer.hasMoreTokens()) {
                tokens.add(wordInSentenceTokenizer.nextToken().toLowerCase());
            }

            if (tokens.contains(topWord.toLowerCase())) {
                sentenceWithMostUsedWord.add(sentence);
            }
        }
        return sentenceWithMostUsedWord.get(sentenceWithMostUsedWord.size() - 1);
    }

}
