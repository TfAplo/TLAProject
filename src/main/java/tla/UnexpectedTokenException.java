/*
DUBOULOY Grégory
FOUQUET Tom
DELAMARE Bastien
*/

package tla;

public class UnexpectedTokenException extends Exception {
	private int position;
	private String expectedToken;

	public UnexpectedTokenException(int position, String expectedToken) {
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