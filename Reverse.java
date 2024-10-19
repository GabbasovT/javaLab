import java.util.Scanner;
import java.util.ArrayList;

public class Reverse {
    public static void main(String[] args) {
        ArrayList<int[]> matrix = new ArrayList<>();
        Scanner input = new Scanner(System.in);

        while (input.hasNextLine()) {
            String line = input.nextLine();

            if (line.isEmpty()) {
                matrix.add(new int[0]);
            } else {
                ArrayList<Integer> vector = new ArrayList<>();
                int currentSign = 1, currentDigit = 0;
                boolean containInt = false;

                for (char u : line.toCharArray()) {
                    if (u == '-') {
                        if (containInt) {
                            vector.add(currentSign * currentDigit);
                        }
                        currentSign = -1;
                        currentDigit = 0;
                        containInt = false;
                    } else if (Character.isWhitespace(u)) {
                        if (containInt) {
                            vector.add(currentSign * currentDigit);
                        }
                        currentSign = 1;
                        currentDigit = 0;
                        containInt = false;
                    } else if (Character.isDigit(u)) {
                        currentDigit = 10 * currentDigit + Character.getNumericValue(u);
                        containInt = true;
                    }
                }
                if (containInt) {
                    vector.add(currentSign * currentDigit);
                }

                int[] currentLine = new int[vector.size()];
                int it = 0;

                for (Integer x : vector) {
                    currentLine[it] = x;
                    it++;
                }
                matrix.add(currentLine);
            }
        }
        input.close();

        for (int i = matrix.size() - 1; i >= 0; i--) {
            int[] line = matrix.get(i);
            for (int j = line.length - 1; j >= 0; j--) {
                System.out.print(line[j]);
                if (j != 0) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }
}
