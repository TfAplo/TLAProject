package tla;

public class SyntaxErrorException extends Exception {
    private int position;
    private String expectedToken;

    public SyntaxErrorException(int position, String expectedToken) {
        super("Syntax error at position " + position + ": expected " + expectedToken);
        this.position = position;
        this.expectedToken = expectedToken;
    }

    public int getPosition() {
        return position;
    }

    public String getExpectedToken() {
        return expectedToken;
    }
}
