/*
DUBOULOY Grégory
FOUQUET Tom
DELAMARE Bastien
*/

package tla;

import java.util.ArrayList;
import java.util.List;

public class AnalyseLexicale {

	/*
	Table de transition de l'analyse lexicale
	 */
	private static Integer TRANSITIONS[][] = {
			//            espace    +    *    (    )    ,	.	-	  /	  	|	  ^	  chiffre  letter
			/*  0 */    {      0, 101, 102, 103, 104, 105,   3,	4,    110,  111,  112,	1,      2  },
			/*  1 */    {    106, 106, 106, 106, 106, 106,   3,	106,  106,  106,  106, 	1,    106  },
			/*  2 */    {    107, 107, 107, 107, 107, 107, 107,	107,  107,  107,  107, 	2,      2  },
			/*  3 */    {    108, 108, 108, 108, 108, 108, 108,	108,  108,	108,  108, 	3,    108  },
			/*  4 */    {    109, 109, 109, 109, 109, 109, 109,	109,  109,	109,  109, 	1,    109  }

			// 101 acceptation d'un +
			// 102 acceptation d'un *
			// 103 acceptation d'un (
			// 104 acceptation d'un )
			// 105 acceptation d'un ,
			// 106 acceptation d'un entier                   (retourArriere)
			// 107 acceptation d'un identifiant ou mot clé   (retourArriere)
			// 108 acceptation d'un double
			// 109 acceptation d'un -
			// 110 acceptation d'un /
			// 111 acceptation d'un |
			// 112 acceptation d'un kPow

	};

	private String entree;
	private int pos;

	private static final int ETAT_INITIAL = 0;

	/*
	effectue l'analyse lexicale et retourne une liste de Token
	 */
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
				System.out.println("pas de transition depuis état " + etat + " avec symbole " + c);
				throw new LexicalErrorException(pos,"pas de transition depuis état " + etat + " avec symbole " + c);
			}
			// cas particulier lorsqu'un état d'acceptation est atteint
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
					}else if (buf.equals("cos")) {
						tokens.add(new Token(TypeDeToken.kCos, pos - buf.length()));
					}else if (buf.equals("sin")) {
						tokens.add(new Token(TypeDeToken.kSin, pos - buf.length()));
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
				}else if (e == 110) {
					tokens.add(new Token(TypeDeToken.divide, buf,pos - buf.length()));
				}else if (e == 111) {
					tokens.add(new Token(TypeDeToken.absolute, buf,pos - buf.length()));
				} else if (e == 112) {
					tokens.add(new Token(TypeDeToken.kPow, pos - buf.length()));
				}
				// un état d'acceptation ayant été atteint, retourne à l'état 0
				etat = 0;
				// reinitialise buf
				buf = "";
			} else {
				// enregistre le nouvel état
				etat = e;
				// ajoute le symbole qui vient d'être examiné à buf
				// sauf s'il s'agit un espace ou assimilé
				if (etat>0) buf = buf + c;
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

	/*
	Pour chaque symbole terminal acceptable en entrée de l'analyse syntaxique
	retourne un indice identifiant soit un symbole, soit une classe de symbole :
	 */
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
		if (c == '/') return 8;
		if (c == '|') return 9;
		if (c == '^') return 10;
		if (Character.isDigit(c)) return 11;
		if (Character.isLetter(c)) return 12;
		System.out.println("Symbole inconnu : " + c);
		throw new IllegalCharacterException(c.toString());
	}

}
