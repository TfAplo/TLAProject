package tla;

public class LexicalErrorWithPositionException extends Exception {
    private int position;
    public LexicalErrorWithPositionException(String message, int position) {
        super(message);
        this.position = position;
    }
    public int getPosition() {
        return position;
    }
}