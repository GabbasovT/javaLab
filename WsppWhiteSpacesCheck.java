public class WsppWhiteSpacesCheck implements WhitespaceChecker {
    @Override
    public boolean isWhitespace(char c) {
        return !(Character.getType(c) == Character.DASH_PUNCTUATION) && !(Character.isLetter(c)) && !(c == '\'');
    }
}