/*
NOM Prénom(s)
NOM Prénom(s)
NOM Prénom(s)
*/

package tla;

import javax.swing.*;
import java.awt.*;

/**
 * Classe PlotPanel : zone de tracé du graphique basée sur un JPanel
 * avec délégation de l'affichage à la méthode paint d'une instance de Plot
 */
public class PlotPanel extends JPanel {

    private Plot plot;

    public PlotPanel(Plot plot) {
        this.plot = plot;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        plot.paint((Graphics2D)g, this.getWidth(), this.getHeight());
    }

}
