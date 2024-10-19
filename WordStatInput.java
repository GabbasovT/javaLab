import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class WordStatInput {

    public static boolean isInWord(char c) {
        return Character.getType(c) == Character.DASH_PUNCTUATION || Character.isLetter(c) || c == '\'';
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Error: Input and output files must be specified.");
            return;
        }

        Map<String, Integer> wordCount = new LinkedHashMap<>();

        try (Scanner scanner = new Scanner(new FileInputStream(args[0]))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                int begin = 0;
                for (int i = 0; i <= line.length(); i++) {
                    if (i == line.length() || !isInWord(line.charAt(i))) {
                        if (begin != i) {
                            String word = line.substring(begin, i).toLowerCase();
                            wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
                        }
                        begin = i + 1;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace(System.out);
        }

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]), "UTF-8"))) {
            for (Map.Entry<String, Integer> entry : wordCount.entrySet()) {
                writer.write(entry.getKey() + " " + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
            e.printStackTrace(System.out);
        }
    }
}
