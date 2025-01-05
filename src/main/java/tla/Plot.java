/*
DUBOULOY Grégory
FOUQUET Tom
DELAMARE Bastien
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

    Noeud racine;

    /**
     * point d'entrée d'ecriture d'une fonction par l'IHM<br/>
     *
     * effectue l'analyse lexicale et syntaxique de la chaine entree,
     * affiche et interprète l'arbre syntaxique abstrait
     */
    void setFunction(String entree) throws LexicalErrorException, SyntaxErrorException {
        this.function = entree;
        try{
            List<Token> tokens = new AnalyseLexicale().analyse(entree);
            this.racine = new AnalyseSyntaxique().analyse(tokens);
            Noeud.afficheNoeud(racine, 0);
        }catch (LexicalErrorException e) {
            throw new LexicalErrorException(e.getPosition(), e.getErrorType());
        } catch (SyntaxErrorException e) {
            throw new SyntaxErrorException(e.getPosition(), e.getExpectedToken());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
            //creation de l'interpréteur
            Interpretation inter = new Interpretation();
            for (double x = -range; x<= range; x += step) {

                double y=0;

            /*
            calcule de la valeur y=f(x) suivant la fonction tapée par l'utilisateur
            */
                //y = testInterpretation(function.replace("x", String.valueOf(x)));
                inter.updateMap(x);
                y = inter.interpreter(racine);

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
}
