package tla;

import java.util.List;

public class AnalyseSyntaxique {

	private int pos;
	private List<Token> tokens;

	public Noeud analyse(List<Token> tokens) throws Exception {
		pos = 0;
		this.tokens = tokens;
		Noeud s = S();
		if (pos != tokens.size()) {
			throw new IncompleteParsingException();
		}
		return s;
	}

	private Noeud S() throws Exception {
		if (getTypeDeToken() == TypeDeToken.intv ||
				getTypeDeToken() == TypeDeToken.kPow ||
				getTypeDeToken() == TypeDeToken.ident ||
				getTypeDeToken() == TypeDeToken.leftPar ||
				getTypeDeToken() == TypeDeToken.doublev) {
			Noeud a = A();
			return S_prime(a);
		}
		throw new SyntaxErrorException(tokens.get(pos).getPosition(), "intv, (, pow, or ident");
	}

	private Noeud S_prime(Noeud i) throws Exception {
		if (getTypeDeToken() == TypeDeToken.add) {
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
			return i;
		}
		throw new SyntaxErrorException(tokens.get(pos).getPosition(), "+ or )");
	}

	private Noeud A() throws Exception {
		Noeud n = C();
		return A_prime(n);
	}

	private Noeud A_prime(Noeud i) throws Exception {
		if (getTypeDeToken() == TypeDeToken.multiply) {
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
			return i;
		}
		throw new SyntaxErrorException(tokens.get(pos).getPosition(), "* + or )");
	}

	private Noeud B() throws Exception {
		if (getTypeDeToken() == TypeDeToken.leftPar) {
			lireToken();
			Noeud s = S();
			if (getTypeDeToken() == TypeDeToken.rightPar) {
				lireToken();
				return s;
			}
			throw new SyntaxErrorException(tokens.get(pos).getPosition(), ")");
		}
		if (getTypeDeToken() == TypeDeToken.intv) {
			Token t = lireToken();
			return new Noeud(TypeDeNoeud.intv, t.getValeur());
		}
		if (getTypeDeToken() == TypeDeToken.doublev) {
			Token t = lireToken();
			return new Noeud(TypeDeNoeud.doublev, t.getValeur());
		}
		if (getTypeDeToken() == TypeDeToken.ident) {
			Token t = lireToken();
			return new Noeud(TypeDeNoeud.ident, t.getValeur());
		}
		if (getTypeDeToken() == TypeDeToken.kPow) {
			lireToken();
			if (lireToken().getTypeDeToken() != TypeDeToken.leftPar) {
				throw new SyntaxErrorException(tokens.get(pos).getPosition(), "( ");
			}
			Noeud n = new Noeud(TypeDeNoeud.kPow);
			n.ajout(S());
			if (lireToken().getTypeDeToken() != TypeDeToken.comma) {
				throw new SyntaxErrorException(tokens.get(pos).getPosition(), ", ");
			}
			n.ajout(S());
			if (lireToken().getTypeDeToken() != TypeDeToken.rightPar) {
				throw new SyntaxErrorException(tokens.get(pos).getPosition(), ")");
			}
			return n;
		}
		throw new SyntaxErrorException(tokens.get(pos).getPosition(), "intv, (, pow, or ident");
	}

	private Noeud C() throws Exception {
		Noeud n = B();
		return C_prime(n);
	}

	private Noeud C_prime(Noeud i) throws Exception {
		if (getTypeDeToken() == TypeDeToken.sub) {
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
			return i;
		}
		throw new SyntaxErrorException(tokens.get(pos).getPosition(), "* + or )");
	}

	private boolean finAtteinte() {
		return pos >= tokens.size();
	}

	private TypeDeToken getTypeDeToken() {
		if (pos >= tokens.size()) {
			return null;
		} else {
			return tokens.get(pos).getTypeDeToken();
		}
	}

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