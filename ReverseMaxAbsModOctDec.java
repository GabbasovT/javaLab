//import java.util.Scanner;
import java.util.Arrays;

public class ReverseMaxAbsModOctDec {
    static int MOD = 1_000_000_007;

    public static int mod(int x) {
        return (x % MOD + MOD) % MOD;
    }

    public static boolean comp(int a, int b) {
        return mod(Math.abs(a)) > mod(Math.abs(b));
    }

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
                vector[currentNumber++] = lineScanner.nextInt();
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

        int lenOfStrings = 0;
        int[] maxHoriz = new int[currentString];
        for (int i = 0; i < currentString; i++) {
            maxHoriz[i] = Integer.MIN_VALUE;
        }

        for (int i = 0; i < currentString; i++) {
            lenOfStrings = Math.max(lenOfStrings, matrix[i].length);
        }

        int[] maxVert = new int[lenOfStrings];
        for (int i = 0; i < lenOfStrings; i++) {
            maxVert[i] = Integer.MIN_VALUE;
        }

        for (int i = 0; i < currentString; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (maxHoriz[i] == Integer.MIN_VALUE || comp(matrix[i][j], maxHoriz[i])) {
                    maxHoriz[i] = matrix[i][j];
                }
                if (maxVert[j] == Integer.MIN_VALUE || comp(matrix[i][j], maxVert[j])) {
                    maxVert[j] = matrix[i][j];
                }
            }
        }
        for (int i = 0; i < currentString; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (comp(maxHoriz[i], maxVert[j])) {
                    System.out.print(Integer.toOctalString(maxHoriz[i]) + "o ");
                } else {
                    System.out.print(Integer.toOctalString(maxVert[j]) + "o ");
                }
            }
            System.out.println();
        }
    }
}
