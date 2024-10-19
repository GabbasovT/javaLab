import java.io.IOException;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.StringReader;
import java.util.NoSuchElementException;
import java.nio.charset.StandardCharsets;

public class Scanner implements AutoCloseable {
    private static final int BUFFER_SIZE = 1024;
    private Reader reader;
    private char[] buffer;
    private int bufferSize;
    private int positionBuffer;
    private int positionBuilder;
    private boolean isEOF;
    private StringBuilder builder;
    private String lineSeparator;
    private int sepLength;

    public Scanner(String inputStream) {
        this.reader = new StringReader(inputStream);
        this.lineSeparator = System.lineSeparator();
        initialize();
    }

    public Scanner(InputStream inputStream) {
        this.reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        this.lineSeparator = System.lineSeparator();
        initialize();
    }

    private void initialize() {
        isEOF = false;
        builder = new StringBuilder();
        buffer = new char[BUFFER_SIZE];
        bufferSize = 0;
        positionBuffer = 0;
        positionBuilder = 0;
        sepLength = lineSeparator.length();
        fillBuffer();
    }

    private void fillBuffer() {
        if (positionBuffer < bufferSize) {
            return;
        }
        try {
            bufferSize = reader.read(buffer);
            positionBuffer = 0;
            isEOF = (bufferSize == -1);
        } catch (IOException e) {
            throw new RuntimeException("Exception reading from Reader", e);
        }
    }

    public void resetBuilder() {
        if (positionBuilder == builder.length()) {
            builder.setLength(0);
            positionBuilder = 0;
        }
    }

    private boolean hasNextChar() {
        if (isEOF) {
            return false;
        }
        fillBuffer();
        return bufferSize > positionBuffer;
    }

    private char nextChar() {
        if (!hasNextChar()) {
            throw new NoSuchElementException("No more chars");
        }
        fillBuffer();
        if (sepLength == 2) {
            if (buffer[positionBuffer] == '\r') {
                positionBuffer++;
                return nextChar();
            }
        }
        return buffer[positionBuffer++];
    }

    private StringBuilder getToken() {
        StringBuilder token = new StringBuilder();
        for (; positionBuilder < builder.length(); positionBuilder++) {
            char c = builder.charAt(positionBuilder);
            if (!Character.isWhitespace(c)) {
                token.append(c);
            }
        }
        char c = builder.charAt(builder.length() - 1);
        resetBuilder();
        if (Character.isWhitespace(c)) {
            builder.append(c);
        }
        return token;
    }

    public boolean hasNextLine() {
        if (positionBuilder < builder.length()) {
            return true;
        }
        if (isEOF && builder.length() == 0) {
            return false;
        }
        fillBuffer();

        return bufferSize > 0 || builder.length() > 0;
    }

    public String nextLine() {
        if (!hasNextLine()) {
            throw new NoSuchElementException("No more lines");
        }
        StringBuilder line = new StringBuilder();
        for (; positionBuilder < builder.length(); positionBuilder++) {
            char c = builder.charAt(positionBuilder);
            line.append(c);
            if (c == '\n' || c == '\r') {
                positionBuilder++;
                resetBuilder();
                return line.toString();
            }
        }
        resetBuilder();
        while (hasNextChar()) {
            char c = nextChar();
            line.append(c);
            if (c == '\n' || c == '\r') {
                break;
            }
        }
        return line.toString();
    }

    public boolean hasNext() {
        if (builder.length() > 1) {
            return true;
        }
        if (isEOF) {
            return false;
        }
        boolean wordEnds = false;
        while (hasNextChar()) {
            char c = nextChar();
            builder.append(c);
            if (Character.isWhitespace(c) && wordEnds) {
                return true;
            }
            if (!Character.isWhitespace(c)) {
                wordEnds = true;
            }
        }
        return wordEnds;
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
        for (int i = positionBuilder; i < builder.length(); i++) {
            char c = builder.charAt(i);
            if (!Character.isWhitespace(c)) {
                str.append(c);
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

        StringBuilder str = getToken();
        String s = str.toString();

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

    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException("Exception closing the Reader", e);
        }
    }
}
