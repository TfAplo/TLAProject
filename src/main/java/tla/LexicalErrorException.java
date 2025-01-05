package tla;

public class LexicalErrorException extends Exception {
	private int position;
	private String errorType;

	public LexicalErrorException(int position, String errorType) {
		super("Lexical error at position " + position + ": " + errorType);
		this.position = position;
		this.errorType = errorType;
	}

	public int getPosition() {
		return position;
	}

	public String getErrorType() {
		return errorType;
	}
}
