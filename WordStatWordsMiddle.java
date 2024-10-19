import java.io.*;
import java.util.Map;
import java.util.Collections;
import java.util.TreeMap;

public class WordStatWordsMiddle {

    public static boolean isInWord(char c) {
        return Character.getType(c) == Character.DASH_PUNCTUATION || Character.isLetter(c) || c == '\'';
    }

    public static String getMiddle(String word) {
        if (word.length() < 7) {
            return word;
        } else {
            return word.substring(3, word.length() - 3);
        }
    }

    public static void addString(Map<String, Integer> wordCount, StringBuilder wordBuilder) {
        if (wordBuilder.length() <= 0) {
            return;
        }

        String word = wordBuilder.toString().toLowerCase();
        String middle = getMiddle(word);
        wordCount.put(middle, wordCount.getOrDefault(middle, 0) + 1);
        wordBuilder.setLength(0);
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Error: Input and output files must be specified.");
            return;
        }

        Map<String, Integer> wordCount = new TreeMap<>(Collections.reverseOrder());

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(args[0]), "UTF-8"))) {
            StringBuilder wordBuilder = new StringBuilder();
            char[] buffer = new char[1024];
            int size;

            while ((size = reader.read(buffer)) != -1) {
                for (int i = 0; i < size; i++) {
                    char currentChar = buffer[i];
                    if (isInWord(currentChar)) {
                        wordBuilder.append(currentChar);
                    } else {
                        addString(wordCount, wordBuilder);
                    }
                }
            }
            addString(wordCount, wordBuilder);
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(args[1]), "UTF-8"))) {
            for (Map.Entry<String, Integer> entry : wordCount.entrySet()) {
                writer.write(entry.getKey() + " " + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing output file: " + e.getMessage());
        }
    }
}
