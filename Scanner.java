import java.io.IOException;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;

public class Scanner {
    private static final int BUFFER_SIZE = 1024;
    private Reader reader;
    private char[] buffer;
    private int bufferSize;
    private int positionInBuffer;
    private int positionInBuilder;
    private int tokenStarts;
    private StringBuilder builder;
    private String lineSeparator;
    private int separatorLength;

    public Scanner(InputStream inputStream) {
        this.reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        this.lineSeparator = System.lineSeparator();
        initialize();
    }

    public Scanner(String inputStream) {
        this.reader = new StringReader(inputStream);
        this.lineSeparator = System.lineSeparator();
        initialize();
    }

    private void initialize() {
        builder = new StringBuilder();
        buffer = new char[BUFFER_SIZE];
        bufferSize = 0;
        positionInBuffer = 0;
        positionInBuilder = 0;
        tokenStarts = 0;
        separatorLength = lineSeparator.length();
        fillBuffer();
    }

    private void fillBuffer() {
        if (positionInBuffer < bufferSize) {
            return;
        }
        try {
            bufferSize = reader.read(buffer);
            positionInBuffer = 0;
        } catch (IOException e) {
            throw new RuntimeException("Exception reading from Reader", e);
        }
    }

    private void resetBuilder() {
        builder.setLength(0);
        positionInBuilder = 0;
        tokenStarts = 0;
    }

    private boolean hasNextChar() {
        fillBuffer();
        return positionInBuffer < bufferSize;
    }

    private char nextChar() {
        if (!hasNextChar()) {
            throw new NoSuchElementException("No more chars");
        }

        fillBuffer();
        return buffer[positionInBuffer++];
    }

    private StringBuilder getToken() {
        StringBuilder token = new StringBuilder();
        for (; tokenStarts < builder.length(); tokenStarts++) {
            char c = builder.charAt(tokenStarts);
            if (Character.isWhitespace(c)) {
                resetBuilder();
                builder.append(c);
                return token;
            }
            token.append(c);
        }
        while (hasNextChar()) {
            char c = nextChar();
            if (Character.isWhitespace(c)) {
                resetBuilder();
                builder.append(c);
                return token;
            }
            token.append(c);
        }
        return token;
    }

    public boolean hasNextLine() {
        fillBuffer();
        return (positionInBuffer < bufferSize || positionInBuilder < builder.length());
    }

    public String nextLine() {
        if (!hasNextLine()) {
            throw new NoSuchElementException("No more lines");
        }
        StringBuilder line = new StringBuilder();
        for (; positionInBuilder < builder.length(); positionInBuilder++) {
            char c = builder.charAt(positionInBuilder);
            line.append(c);
            if (line.length() >= separatorLength) {
                if (lineSeparator.equals(line.substring(line.length() - separatorLength))) {
                    positionInBuilder++;
                    if (positionInBuilder == builder.length()) {
                        positionInBuilder = 0;
                        builder.setLength(0);
                        tokenStarts = 0;
                    }
                    return line.toString();
                }
            }
        }
        resetBuilder();
        while (hasNextChar()) {
            char c = nextChar();
            line.append(c);
            if (line.length() >= separatorLength) {
                if (lineSeparator.equals(line.substring(line.length() - separatorLength))) {
                    break;
                }
            }
        }
        return line.toString();
    }

    public boolean hasNext() {
        for (; tokenStarts < builder.length(); tokenStarts++) {
            if (!Character.isWhitespace(builder.charAt(tokenStarts))) {
                return true;
            }
        }
        while (hasNextChar()) {
            char c = nextChar();
            builder.append(c);
            if (!Character.isWhitespace(c)) {
                tokenStarts = builder.length() - 1;
                return true;
            }
        }
        return false;
    }

    public String next() {
        if (!hasNextLine()) {
            throw new NoSuchElementException("No more strings");
        }
        StringBuilder str = getToken();
        return str.toString();
    }

    public boolean hasNextInt() {
        if (!hasNext()) {
            return false;
        }
        StringBuilder str = new StringBuilder();
        for (int i = tokenStarts; i < builder.length(); i++) {
            char c = builder.charAt(i);
            if (Character.isWhitespace(c)) {
                break;
            }
            str.append(c);
            if (i == builder.length() - 1) {
                while (hasNextChar()) {
                    char v = nextChar();
                    builder.append(v);
                    if (Character.isWhitespace(v)) {
                        break;
                    }
                    str.append(v);
                }
                break;
            }
        }

        String s = str.toString();
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            if (!s.endsWith("o") && !s.endsWith("O")) {
                return false;
            }

            try {
                Integer.parseUnsignedInt(s.substring(0, s.length() - 1), 8);
            } catch (NumberFormatException ex) {
                return false;
            }
        }
        return true;
    }

    public int nextInt() {
        if (!hasNextInt()) {
            throw new NoSuchElementException("No more ints");
        }

        String s = getToken().toString();

        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            if (!s.endsWith("o") && !s.endsWith("O")) {
                throw new NumberFormatException("Invalid integer format: " + s);
            }

            try {
                return Integer.parseUnsignedInt(s.substring(0, s.length() - 1), 8);
            } catch (NumberFormatException ex) {
                throw new NumberFormatException("Invalid octal format: " + s);
            }
        }
    }

    public boolean hasNextIntInLine() {
        if (!hasNextInt()) {
            return false;
        }
        for (int i = positionInBuilder; i < tokenStarts; i++) {
            if (i - positionInBuilder + 1 >= separatorLength
                    && lineSeparator.equals(builder.substring(i - separatorLength + 1, i + 1))) {
                return false;
            }
        }
        return true;
    }

    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException("Exception closing the Reader", e);
        }
    }
}