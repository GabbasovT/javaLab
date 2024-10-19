public class Sum {

    public static void main(String[] args) {
        StringBuilder input = concatArgs(args);
        System.out.println( getTotalSum(input));
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
        if (Character.isWhitespace(c) || c == '+' ||
                Character.getType(c) == Character.START_PUNCTUATION ||
                Character.getType(c) == Character.END_PUNCTUATION) {
            return true;
        }
        return false;
    }

    public static int getTotalSum(StringBuilder s) {
        StringBuilder buffer = new StringBuilder();
        int sum = 0;

        for (int i = 0; i < s.length(); i++) {
            if (isBadChar(s.charAt(i))) {
                if (buffer.isEmpty()) {
                    continue;
                }

                String digit = buffer.toString();
                if (digit.startsWith("0x") || digit.startsWith("0X")) {
                    sum += Long.parseUnsignedLong(digit.substring(2), 16);
                } else {
                    sum += Long.parseLong(digit);
                }
                buffer.setLength(0);
            } else {
                buffer.append(s.charAt(i));
            }
        }
        return (int) sum;
    }
}
