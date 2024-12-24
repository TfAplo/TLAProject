/*
NOM Prénom(s)
NOM Prénom(s)
NOM Prénom(s)
*/

package tla;

import java.awt.*;
import java.util.List;

/**
 * Plot : calcul du tracé de différentes fonctions en dur, sur un intervalle
 */
public class Plot {

    /**
     * Nombre de point calculés dans l'intervalle
     */
    final static double STEPS = 1000;

    /**
     * le calcul des valeurs de la fonction se fait sur l'intervalle [-range...range]
     */
    double range = 2;

    String function = "";

    /**
     * point d'entrée d'ecriture d'une fonction par l'IHM<br/>
     */
    void setFunction(String entree) {
        this.function = entree;
    }

    /**
     * point d'entrée d'affectation du range par l'IHM
     */
    void setRange(double range) {
        this.range = range;
    }

    /**
     * Méthode appelée par PlotPanel pour effectuer le tracé du graphique,
     * selon le range et la fonction selectionnée
     * @param g objet permettant de dessiner sur un JPanel
     * @param w largeur du JPanel
     * @param h hauteur du JPanel
     */
    void paint(Graphics2D g, double w, double h) {
        if (!function.equals("")){
            double step = range / STEPS;

            double centerX = w / 2;
            double centerY = h / 2;

            double halfMinSize = Math.max(w, h) / 2;

            // affiche le repère
            g.setColor(Color.GRAY);
            g.drawLine((int)centerX, 0, (int)centerX, (int)h);
            g.drawLine(0, (int)centerY, (int)w, (int)centerY);

            // affiche différents points représentant la fonction sélectionnée
            g.setColor(Color.BLACK);
            for (double x = -range; x<= range; x += step) {

                double y=0;

            /*
            calcule de la valeur y=f(x) suivant la fonction tapée par l'utilisateur
            */
                y = testInterpretation(function.replace("x", String.valueOf(x)));

            /*
            Affichage du point de coordonnées (x,y), coordonnées ajustées à la dimension
            de la zone d'affichage du tracé
            */
                if (Double.isFinite(y)) {
                    g.drawRect(
                            (int) (centerX + x * halfMinSize / range),
                            (int) (centerY + -y * halfMinSize / range),
                            1,
                            1
                    );
                }
            }
        }


    }

    /*
effectue l'analyse lexicale de la chaine entree,
affiche la liste des tokens reconnus
 */
    private static void testAnalyseLexicale(String entree) {
        System.out.println("test analyse lexicale");
        try {
            java.util.List<Token> tokens = new AnalyseLexicale().analyse(entree);
            for (Token t : tokens) {
                System.out.println(t);
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        System.out.println();
    }

    /*
    effectue l'analyse lexicale et syntaxique de la chaine entree
     */
    private static void testAnalyseSyntaxique(String entree) {
        System.out.println("test analyse syntaxique");
        try {
            java.util.List<Token> tokens = new AnalyseLexicale().analyse(entree);
            Noeud racine = new AnalyseSyntaxique().analyse(tokens);
            Noeud.afficheNoeud(racine, 0);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        System.out.println();
    }

    /*
    effectue l'analyse lexicale et syntaxique de la chaine entree,
    affiche et interprète l'arbre syntaxique abstrait
     */
    private static double testInterpretation(String entree) {
        System.out.println("test interpretation");
        try {
            List<Token> tokens = new AnalyseLexicale().analyse(entree);
            Noeud racine = new AnalyseSyntaxique().analyse(tokens);
            Noeud.afficheNoeud(racine, 0);
            return new Interpretation().interpreter(racine);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return 0;
    }
}
