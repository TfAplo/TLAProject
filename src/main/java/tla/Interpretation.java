package tla;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class Interpretation {

	private static boolean erreurDejaAffichee = false; // Drapeau pour éviter les multiples fenêtres
	private HashMap<String, Double> m;

	public Interpretation() {
		m = new HashMap<>();
	}

	public Double interpreter(Noeud n) {
		if (n.getTypeDeNoeud() == TypeDeNoeud.intv) {
			return Double.parseDouble(n.getValeur());
		}
		if (n.getTypeDeNoeud() == TypeDeNoeud.add) {
			return interpreter(n.enfant(0)) + interpreter(n.enfant(1));
		}
		if (n.getTypeDeNoeud() == TypeDeNoeud.sub) {
			return interpreter(n.enfant(0)) - interpreter(n.enfant(1));
		}
		if (n.getTypeDeNoeud() == TypeDeNoeud.kPow) {
			return Math.pow(interpreter(n.enfant(0)), interpreter(n.enfant(1)));
		}
		if (n.getTypeDeNoeud() == TypeDeNoeud.multiply) {
			return interpreter(n.enfant(0)) * interpreter(n.enfant(1));
		}
		if (n.getTypeDeNoeud() == TypeDeNoeud.ident) {
			if (!m.containsKey(n.getValeur())) {
				// Message d'erreur
				String message = "Identifiant non défini : " + n.getValeur();
				System.err.println(message);

				// Affichage unique de la boîte de dialogue
				if (!erreurDejaAffichee) {
					erreurDejaAffichee = true; // Marquer l'erreur comme affichée
					SwingUtilities.invokeLater(() -> {
						JOptionPane.showMessageDialog(null, message, "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
						erreurDejaAffichee = false; // Réinitialiser le drapeau une fois la fenêtre fermée
					});
				}

				throw new IllegalStateException(message);
			}
			return m.get(n.getValeur());
		}
		if (n.getTypeDeNoeud() == TypeDeNoeud.doublev) {
			return Double.parseDouble(n.getValeur());
		}
		return null; // Retour par défaut (ne devrait jamais être atteint)
	}
}