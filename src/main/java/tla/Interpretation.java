/*
DUBOULOY Grégory
FOUQUET Tom
DELAMARE Bastien
*/

package tla;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class Interpretation {

	// permet la lecture de chaîne au clavier
	private static BufferedReader stdinReader = new BufferedReader(new InputStreamReader(System.in));
	private HashMap<String, Double>  m;

	public Interpretation() {
		/* A COMPLETER */
		this.m = new HashMap<String, Double>();
	}

	/*
	interprete le noeud n
	et appel récursif sur les noeuds enfants de n
	 */
	public Double interpreter(Noeud n) {
		if(n.getTypeDeNoeud() == TypeDeNoeud.intv){
			return Double.parseDouble(n.getValeur());
		}if(n.getTypeDeNoeud() == TypeDeNoeud.add){
			return interpreter(n.enfant(0)) + interpreter(n.enfant(1));
		}if(n.getTypeDeNoeud() == TypeDeNoeud.sub){
			return interpreter(n.enfant(0)) - interpreter(n.enfant(1));
		}if(n.getTypeDeNoeud() == TypeDeNoeud.kPow){
			return Math.pow(interpreter(n.enfant(0)).doubleValue(), interpreter(n.enfant(1)).doubleValue());
		}if (n.getTypeDeNoeud() == TypeDeNoeud.multiply){
			return interpreter(n.enfant(0)) * interpreter(n.enfant(1));
		}if (n.getTypeDeNoeud() == TypeDeNoeud.divide){
			return interpreter(n.enfant(0)) / interpreter(n.enfant(1));
		}if (n.getTypeDeNoeud() == TypeDeNoeud.absolute) {
			return Math.abs(interpreter(n.enfant(0)));
		}if (n.getTypeDeNoeud() == TypeDeNoeud.ident){
			return m.get(n.getValeur());
		}if (n.getTypeDeNoeud() == TypeDeNoeud.doublev){
			return Double.parseDouble(n.getValeur());
		}
	return null;
	}

	/*
	met a jour x dans le hashmap
	 */
	public void updateMap(double x) {
		m.put("x",x);
	}
}
