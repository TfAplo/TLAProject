/*
DUBOULOY Grégory
FOUQUET Tom
DELAMARE Bastien
*/

package tla;

import java.util.List;

public class AnalyseSyntaxique {

	private int pos;
	private List<Token> tokens;

	/*
	effectue l'analyse syntaxique à partir de la liste de tokens
	et retourne le noeud racine de l'arbre syntaxique abstrait
	 */
	public Noeud analyse(List<Token> tokens) throws Exception {
		pos = 0;
		this.tokens = tokens;
		Noeud s = S();
		if (pos != tokens.size()) {
			System.out.println("L'analyse syntaxique s'est terminé avant l'examen de tous les tokens");
			throw new IncompleteParsingException();
		}
		return s;
	}

	/*

	Traite la dérivation du symbole non-terminal S

	S -> A S'

	 */

	private Noeud S() throws UnexpectedTokenException {

		if (getTypeDeToken() == TypeDeToken.intv ||
				getTypeDeToken() == TypeDeToken.kPow ||
				getTypeDeToken() == TypeDeToken.ident ||
				getTypeDeToken() == TypeDeToken.leftPar ||
				getTypeDeToken() == TypeDeToken.doublev) {

			// production S -> A S'

			Noeud a = A();
			return S_prime(a);
		}
		throw new UnexpectedTokenException("intv ou ( attendu");
	}

	/*

	Traite la dérivation du symbole non-terminal Expr'

	S' -> + S | epsilon

	 */

	private Noeud S_prime(Noeud i) throws UnexpectedTokenException {

		if (getTypeDeToken() == TypeDeToken.add) {

			// production S' -> + S

			Token t = lireToken();
			Noeud n = new Noeud(TypeDeNoeud.add);
			n.ajout(i);
			n.ajout(S());
			return n;
		}

		if (getTypeDeToken() == TypeDeToken.rightPar ||
				getTypeDeToken() == TypeDeToken.kInput ||
				getTypeDeToken() == TypeDeToken.kPrint ||
				getTypeDeToken() == TypeDeToken.comma ||
				finAtteinte()) {

			// production S' -> epsilon

			return i;
		}

		throw new UnexpectedTokenException("+ ou ) attendu");
	}

	/*

	Traite la dérivation du symbole non-terminal A

	A -> C A'

	 */

	private Noeud A() throws UnexpectedTokenException {

		if (getTypeDeToken() == TypeDeToken.intv ||
				getTypeDeToken() == TypeDeToken.kPow ||
				getTypeDeToken() == TypeDeToken.ident ||
				getTypeDeToken() == TypeDeToken.leftPar ||
				getTypeDeToken() == TypeDeToken.doublev) {
			Noeud n = C();
			return A_prime(n);

		}
		throw new UnexpectedTokenException("( int ou double attendu");
	}

	/*

	Traite la dérivation du symbole non-terminal A'

	A' -> * A | epsilon

	 */

	private Noeud A_prime(Noeud i) throws UnexpectedTokenException {
//		System.out.println(tokens.get(pos).getValeur());
//		System.out.println(pos);
//		System.out.println(getTypeDeToken());
		if (getTypeDeToken() == TypeDeToken.multiply) {

			// production A' -> * A

			Token t = lireToken();
			Noeud n = new Noeud(TypeDeNoeud.multiply);
			n.ajout(i);
			n.ajout(A());
			return n;
		}

		if (getTypeDeToken() == TypeDeToken.add ||
				getTypeDeToken() == TypeDeToken.rightPar ||
				getTypeDeToken() == TypeDeToken.kInput ||
				getTypeDeToken() == TypeDeToken.kPrint ||
				getTypeDeToken() == TypeDeToken.comma ||
				finAtteinte()) {

			// production A' -> epsilon

			return i;
		}
		throw new UnexpectedTokenException("* + , ou ) attendu");

	}

	/*

	Traite la dérivation du symbole non-terminal B

	B -> ( S ) | intv | doublev | ident | pow ( S , S )

	 */

	private Noeud B() throws UnexpectedTokenException {

		if (getTypeDeToken() == TypeDeToken.leftPar) {

			// production B -> ( S )

			lireToken();
			Noeud s = S();

			if (getTypeDeToken() == TypeDeToken.rightPar) {
				lireToken();
				return s;
			}
			throw new UnexpectedTokenException(") attendu");
		}

		if (getTypeDeToken() == TypeDeToken.intv) {

			// production B -> intv

			Token t = lireToken();
			return new Noeud(TypeDeNoeud.intv, t.getValeur());
		}

		if (getTypeDeToken() == TypeDeToken.doublev) {

			// production B -> doublev

			Token t = lireToken();
			return new Noeud(TypeDeNoeud.doublev, t.getValeur());
		}

		if (getTypeDeToken() == TypeDeToken.ident) {

			// production B -> ident

			Token t = lireToken();
			return new Noeud(TypeDeNoeud.ident, t.getValeur());
		}

		if (getTypeDeToken() == TypeDeToken.kPow) {

			// production B -> pow ( S , S )

			lireToken(); // avance au token suivant

			if (lireToken().getTypeDeToken() != TypeDeToken.leftPar) {
				throw new UnexpectedTokenException("( attendu");
			}

			Noeud n = new Noeud(TypeDeNoeud.kPow);
			n.ajout(S());

			if (lireToken().getTypeDeToken() != TypeDeToken.comma) {
				throw new UnexpectedTokenException(", attendu");
			}

			n.ajout(S());

			if (lireToken().getTypeDeToken() != TypeDeToken.rightPar) {
				throw new UnexpectedTokenException(") attendu");
			}

			return n;
		}

		throw new UnexpectedTokenException("intv, double, (, pow, ou ident attendu");
	}

	/*

	Traite la dérivation du symbole non-terminal C

	C -> D C'

	 */
	private Noeud C() throws UnexpectedTokenException{

		if (getTypeDeToken() == TypeDeToken.intv ||
				getTypeDeToken() == TypeDeToken.kPow ||
				getTypeDeToken() == TypeDeToken.ident ||
				getTypeDeToken() == TypeDeToken.leftPar ||
				getTypeDeToken() == TypeDeToken.doublev) {
			Noeud n = D();
			return C_prime(n);
		}
		throw new UnexpectedTokenException("( int ou double attendu");
	}

	/*

	Traite la dérivation du symbole non-terminal C'

	C' -> ε | - C

	 */
	private Noeud C_prime(Noeud i) throws UnexpectedTokenException{
		if (getTypeDeToken() == TypeDeToken.sub) {

			// production C' -> - C

			Token t = lireToken();
			Noeud n = new Noeud(TypeDeNoeud.sub);
			n.ajout(i);
			n.ajout(C());
			return n;
		}

		if (getTypeDeToken() == TypeDeToken.add ||
				getTypeDeToken() == TypeDeToken.multiply ||
				getTypeDeToken() == TypeDeToken.rightPar ||
				getTypeDeToken() == TypeDeToken.kInput ||
				getTypeDeToken() == TypeDeToken.kPrint ||
				getTypeDeToken() == TypeDeToken.comma ||
				finAtteinte()) {

			// production C' -> epsilon

			return i;
		}
		throw new UnexpectedTokenException("* + , - ou ) attendu");
	}

	/*

	Traite la dérivation du symbole non-terminal D

	D -> B D'

	 */

	private Noeud D() throws UnexpectedTokenException {

		if (getTypeDeToken() == TypeDeToken.intv ||
				getTypeDeToken() == TypeDeToken.kPow ||
				getTypeDeToken() == TypeDeToken.ident ||
				getTypeDeToken() == TypeDeToken.leftPar ||
				getTypeDeToken() == TypeDeToken.doublev) {
			Noeud n = B();
			return D_prime(n);
		}
		throw new UnexpectedTokenException("( int ou double attendu");
	}

	/*

	Traite la dérivation du symbole non-terminal D'

	D' -> / D | epsilon

	 */

	private Noeud D_prime(Noeud i) throws UnexpectedTokenException {
		//System.out.println(tokens.get(pos).getValeur());     >CASSE TOUT
		System.out.println(pos);
		System.out.println(getTypeDeToken());
		if (getTypeDeToken() == TypeDeToken.divide) {

			// production D' -> / D

			Token t = lireToken();
			Noeud n = new Noeud(TypeDeNoeud.divide);
			n.ajout(i);
			n.ajout(D());
			return n;
		}

		if (getTypeDeToken() == TypeDeToken.add ||
				getTypeDeToken() == TypeDeToken.rightPar ||
				getTypeDeToken() == TypeDeToken.kInput ||
				getTypeDeToken() == TypeDeToken.kPrint ||
				getTypeDeToken() == TypeDeToken.comma ||
				getTypeDeToken() == TypeDeToken.multiply ||
				getTypeDeToken() == TypeDeToken.sub ||
				finAtteinte()) {

			// production D' -> epsilon

			return i;
		}
		throw new UnexpectedTokenException("/ * , - ou ) attendu");

	}



	/*

	méthodes utilitaires

	 */

	private boolean finAtteinte() {
		return pos >= tokens.size();
	}

	/*
	 * Retourne la classe du prochain token à lire
	 * SANS AVANCER au token suivant
	 */
	private TypeDeToken getTypeDeToken() {
		if (pos >= tokens.size()) {
			return null;
		} else {
			return tokens.get(pos).getTypeDeToken();
		}
	}

	/*
	 * Retourne le prochain token à lire
	 * ET AVANCE au token suivant
	 */
	private Token lireToken() {
		if (pos >= tokens.size()) {
			return null;
		} else {
			Token t = tokens.get(pos);
			pos++;
			return t;
		}
	}

}
