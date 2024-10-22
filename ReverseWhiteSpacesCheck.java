public class ReverseWhiteSpacesCheck implements WhitespaceChecker {
    @Override
    public boolean isWhitespace(char c) {
        return Character.isWhitespace(c);
    }
}