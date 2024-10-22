import java.util.Arrays;

public class IntList {
    private int[] vector;
    private int currentNumber;
    private int countOfValues;
    private int last;
    private int countInlast;

    public IntList() {
        this.vector = new int[1];
        this.currentNumber = 0;
        this.countOfValues = 0;
        this.last = 0;
        this.countInlast = 0;
    }

    public void add(int value) {
        if (currentNumber == vector.length) {
            vector = Arrays.copyOf(vector, 2 * vector.length);
        }
        vector[currentNumber++] = value;
        increaseCountOfValues();
    }

    public void increaseCountOfValues() {
        countOfValues++;
    }

    public void updateLast(int last, int index, String s) {
        if (this.last == last) {
            countInlast++;
            if (countInlast % 2 == 0) {
                add(index);
            } else {
                increaseCountOfValues();
            }
        } else {
            this.last = last;
            countInlast = 1;
            increaseCountOfValues();
        }
    }

    public int get(int pos) {
        if (pos < 0 || pos >= currentNumber) {
            throw new IndexOutOfBoundsException("Invalid position: " + pos);
        }
        return vector[pos];
    }

    public int getLast() {
        return last;
    }

    public int getCountInlast() {
        return countInlast;
    }

    public int getCountOfValues() {
        return countOfValues;
    }

    public int size() {
        return currentNumber;
    }

    public int[] getPositions() {
        return vector;
    }
}