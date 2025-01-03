package tla;

import java.util.ArrayList;
import java.util.List;

public class AnalyseLexicale {

	private static Integer TRANSITIONS[][] = {
			{0, 101, 102, 103, 104, 105, 3, 4, 1, 2},
			{106, 106, 106, 106, 106, 106, 3, 106, 1, 106},
			{107, 107, 107, 107, 107, 107, 107, 107, 2, 2},
			{108, 108, 108, 108, 108, 108, 108, 108, 3, 108},
			{109, 109, 109, 109, 109, 109, 109, 109, 1, 109}
	};

	private String entree;
	private int pos;
	private static final int ETAT_INITIAL = 0;

	public List<Token> analyse(String entree) throws Exception {
		this.entree = entree;
		pos = 0;
		List<Token> tokens = new ArrayList<>();
		String buf = "";
		Integer etat = ETAT_INITIAL;
		Character c;
		do {
			c = lireCaractere();
			Integer e = TRANSITIONS[etat][indiceSymbole(c)];
			if (e == null) {
				throw new LexicalErrorException(pos - 1, "No transition from state " + etat + " with symbol " + c);
			}
			if (e >= 100) {
				if (e == 101) {
					tokens.add(new Token(TypeDeToken.add, pos - 1));
				} else if (e == 102) {
					tokens.add(new Token(TypeDeToken.multiply, pos - 1));
				} else if (e == 103) {
					tokens.add(new Token(TypeDeToken.leftPar, pos - 1));
				} else if (e == 104) {
					tokens.add(new Token(TypeDeToken.rightPar, pos - 1));
				} else if (e == 105) {
					tokens.add(new Token(TypeDeToken.comma, pos - 1));
				} else if (e == 106) {
					tokens.add(new Token(TypeDeToken.intv, buf, pos - buf.length()));
					retourArriere();
				} else if (e == 107) {
					if (buf.equals("input")) {
						tokens.add(new Token(TypeDeToken.kInput, pos - buf.length()));
					} else if (buf.equals("print")) {
						tokens.add(new Token(TypeDeToken.kPrint, pos - buf.length()));
					} else if (buf.equals("pow")) {
						tokens.add(new Token(TypeDeToken.kPow, pos - buf.length()));
					} else {
						tokens.add(new Token(TypeDeToken.ident, buf, pos - buf.length()));
					}
					retourArriere();
				} else if (e == 108) {
					tokens.add(new Token(TypeDeToken.doublev, buf, pos - buf.length()));
					retourArriere();
				} else if (e == 109) {
					tokens.add(new Token(TypeDeToken.sub, pos - 1));
					retourArriere();
				}
				etat = 0;
				buf = "";
			} else {
				etat = e;
				if (etat > 0) buf = buf + c;
			}
		} while (c != null);
		return tokens;
	}

	private Character lireCaractere() {
		Character c;
		try {
			c = entree.charAt(pos);
			pos = pos + 1;
		} catch (StringIndexOutOfBoundsException ex) {
			c = null;
		}
		return c;
	}

	private void retourArriere() {
		pos = pos - 1;
	}

	private static int indiceSymbole(Character c) throws IllegalCharacterException {
		if (c == null) return 0;
		if (Character.isWhitespace(c)) return 0;
		if (c == '+') return 1;
		if (c == '*') return 2;
		if (c == '(') return 3;
		if (c == ')') return 4;
		if (c == ',') return 5;
		if (c == '.') return 6;
		if (c == '-') return 7;
		if (Character.isDigit(c)) return 8;
		if (Character.isLetter(c)) return 9;
		throw new IllegalCharacterException("Unknown symbol: " + c);
	}
}
