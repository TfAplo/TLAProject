/*
DUBOULOY Grégory
FOUQUET Tom
DELAMARE Bastien
*/

package tla;

public class Token {

	private TypeDeToken typeDeToken;
	private String valeur;
	private int position;

	public Token(TypeDeToken typeDeToken, String value, int position) {
		this.typeDeToken = typeDeToken;
		this.valeur = value;
		this.position = position;
	}

	public Token(TypeDeToken typeDeToken, int position) {
		this.typeDeToken = typeDeToken;
		this.position = position;
	}

	public TypeDeToken getTypeDeToken() {
		return typeDeToken;
	}

	public String getValeur() {
		return valeur;
	}

	public int getPosition() {
		return position;
	}

	public String toString() {
		String res = typeDeToken.toString();
		if (valeur != null) res = res + "(" + valeur + ")";
		return res;
	}
}
