import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class Wspp {

    public static void addString(Map<String, IntList> wordCount, String word, int wordPosition) {
        if (word.isEmpty()) {
            return;
        }
        word = word.toLowerCase();
        IntList positions = wordCount.getOrDefault(word, new IntList());
        positions.add(wordPosition);
        wordCount.put(word, positions);
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Error: Input and output files must be specified.");
            return;
        }
        Map<String, IntList> wordCount = new LinkedHashMap<>();
        int wordIndex = 0;
        WhitespaceChecker customChecker = new WsppWhiteSpacesCheck();

        try (Scanner input = new Scanner(new FileInputStream(args[0]), customChecker)) {
            while (input.hasNext()) {
                String word = input.next();
                if (!word.isEmpty()) {
                    wordIndex++;
                    addString(wordCount, word, wordIndex);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(args[1]), StandardCharsets.UTF_8))) {
            for (Map.Entry<String, IntList> entry : wordCount.entrySet()) {
                String word = entry.getKey();
                IntList positions = entry.getValue();
                writer.write(word + " " + positions.size());
                for (int i = 0; i < positions.size(); i++) {
                    writer.write(" " + positions.get(i));
                }
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing output file: " + e.getMessage());
        }
    }

}
