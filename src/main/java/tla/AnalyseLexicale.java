package tla;

import java.util.ArrayList;
import java.util.List;

public class AnalyseLexicale {

	/*
    Table de transition de l'analyse lexicale
    */
	private static Integer TRANSITIONS[][] = {
			//            espace    +    *    (    )    ,	.	-	chiffre  letter
			/*  0 */    {      0, 101, 102, 103, 104, 105,   3,	4,       1,      2  },
			/*  1 */    {    106, 106, 106, 106, 106, 106,   3,	106,     1,    106  },
			/*  2 */    {    107, 107, 107, 107, 107, 107, 107,	107, 	 2,      2  },
			/*  3 */    {    108, 108, 108, 108, 108, 108, 108,	108, 	 3,      108},
			/*  4 */    {    109, 109, 109, 109, 109, 109, 109,	109, 	 1,      109}
	};

	private String entree; // Chaîne à analyser
	private int pos; // Position actuelle dans la chaîne

	private static final int ETAT_INITIAL = 0;

	/*
    Effectue l'analyse lexicale et retourne une liste de Token
    */
	public List<Token> analyse(String entree) throws Exception {
		this.entree = entree; // Initialise l'entrée
		pos = 0; // Position initiale
		List<Token> tokens = new ArrayList<>(); // Liste des tokens extraits
		String buf = ""; // Buffer temporaire pour construire les tokens
		Integer etat = ETAT_INITIAL; // État initial

		try {
			Character c;
			do {
				c = lireCaractere(); // Lire le caractère courant
				Integer e = TRANSITIONS[etat][indiceSymbole(c, pos - 1)]; // Transition dans la table
				if (e == null) {
					throw new LexicalErrorWithPositionException(
							"Pas de transition depuis l'état " + etat + " avec le symbole '" + c + "'", pos - 1
					);
				}

				if (e >= 100) { // État d'acceptation
					switch (e) {
						case 101:
							tokens.add(new Token(TypeDeToken.add));
							break;
						case 102:
							tokens.add(new Token(TypeDeToken.multiply));
							break;
						case 103:
							tokens.add(new Token(TypeDeToken.leftPar));
							break;
						case 104:
							tokens.add(new Token(TypeDeToken.rightPar));
							break;
						case 105:
							tokens.add(new Token(TypeDeToken.comma));
							break;
						case 106:
							tokens.add(new Token(TypeDeToken.intv, buf));
							retourArriere();
							break;
						case 107:
							if (buf.equals("input")) {
								tokens.add(new Token(TypeDeToken.kInput));
							} else if (buf.equals("print")) {
								tokens.add(new Token(TypeDeToken.kPrint));
							} else if (buf.equals("pow")) {
								tokens.add(new Token(TypeDeToken.kPow));
							} else {
								tokens.add(new Token(TypeDeToken.ident, buf));
							}
							retourArriere();
							break;
						case 108:
							tokens.add(new Token(TypeDeToken.doublev, buf));
							retourArriere();
							break;
						case 109:
							tokens.add(new Token(TypeDeToken.sub, buf));
							retourArriere();
							break;
						default:
							throw new LexicalErrorWithPositionException(
									"État d'acceptation inconnu : " + e, pos - 1
							);
					}
					etat = ETAT_INITIAL; // Retour à l'état initial
					buf = ""; // Réinitialisation du buffer
				} else {
					etat = e; // Passage au nouvel état
					if (!Character.isWhitespace(c)) {
						buf += c; // Ajout du caractère au buffer
					}
				}
			} while (c != null); // Continue jusqu'à la fin de la chaîne
		} catch (LexicalErrorWithPositionException ex) {
			System.err.println("Erreur détectée à la position " + ex.getPosition() + ": " + ex.getMessage());
			throw ex;
		}

		return tokens; // Retourne la liste des tokens
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
    Retourne l'indice correspondant à un symbole donné.
    */
	private static int indiceSymbole(Character c, int position) throws LexicalErrorWithPositionException {
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
		throw new LexicalErrorWithPositionException("Symbole inconnu : " + c, position);
	}
}
