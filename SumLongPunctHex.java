public class SumLongPunctHex {
    public static void main(String[] args) {
        StringBuilder input = concatArgs(args);
        System.out.println(getTotalSum(input));
    }

    public static StringBuilder concatArgs(String[] s) {
        StringBuilder builder = new StringBuilder();

        for (String str : s) {
            builder.append(str);
            builder.append(" ");
        }
        return builder;
    }

    public static boolean isBadChar(char c) {
        return Character.isWhitespace(c) ||
                Character.getType(c) == Character.START_PUNCTUATION ||
                Character.getType(c) == Character.END_PUNCTUATION;
    }

    public static long getTotalSum(StringBuilder s) {
        int begin = 0;
        long sum = 0;

        for (int i = 0; i < s.length(); i++) {
            if (isBadChar(s.charAt(i))) {
                if (begin == i) {
                    begin = i + 1;
                    continue;
                }

                String number = s.substring(begin, i);
                if (number.startsWith("0x") || number.startsWith("0X")) {
                    sum += Long.parseUnsignedLong(number.substring(2), 16);
                } else {
                    sum += Long.parseLong(number);
                }
                begin = i + 1;
            }
        }
        return sum;
    }
}
