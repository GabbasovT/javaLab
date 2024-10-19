import java.util.Scanner;
import java.util.Arrays;

public class ReverseSumAbsMod {
    static int MOD = 1_000_000_007;

    public static void main(String[] args) {
        int[][] matrix = new int[1][0];
        Scanner input = new Scanner(System.in);
        int currentString = 0;

        while (input.hasNextLine()) {
            int[] vector = new int[1];
            int currentNumber = 0;
            String line = input.nextLine();
            Scanner lineScanner = new Scanner(line);
            while (lineScanner.hasNextInt()) {
                if (currentNumber == vector.length) {
                    vector = Arrays.copyOf(vector, 2 * vector.length);
                }
                vector[currentNumber++] = Math.abs(lineScanner.nextInt()) % MOD;
            }
            lineScanner.close();
            vector = Arrays.copyOf(vector, currentNumber);
            if (currentString == matrix.length) {
                matrix = Arrays.copyOf(matrix, 2 * matrix.length);
            }
            matrix[currentString++] = vector;
        }
        matrix = Arrays.copyOf(matrix, currentString);
        input.close();

        int[] sums = new int[currentString];
        int lenOfStrings = 0;

        for (int i = 0; i < currentString; i++) {
            lenOfStrings = Math.max(lenOfStrings, matrix[i].length);
        }

        int[] sumsVert = new int[lenOfStrings];

        for (int i = 0; i < currentString; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                sums[i] = (matrix[i][j] + sums[i]) % MOD;
                sumsVert[j] = (matrix[i][j] + sumsVert[j]) % MOD;
            }
        }
        for (int i = 0; i < currentString; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                int result = ((sums[i] + sumsVert[j]) % MOD - matrix[i][j] + MOD) % MOD;
                System.out.print(result + " ");
            }
            System.out.println();
        }
    }
}
