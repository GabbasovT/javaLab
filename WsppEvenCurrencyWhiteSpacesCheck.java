public class WsppEvenCurrencyWhiteSpacesCheck implements WhitespaceChecker {
    @Override
    public boolean isWhitespace(char c) {
        return !(Character.getType(c) == Character.DASH_PUNCTUATION)
                && !(Character.isLetter(c)) && !(c == '\'') && !(Character.getType(c) == Character.CURRENCY_SYMBOL);
    }
}