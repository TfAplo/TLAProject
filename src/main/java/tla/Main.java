/*
DUBOULOY Grégory
FOUQUET Tom
DELAMARE Bastien
*/

package tla;

import java.awt.*;
import javax.swing.*;

public class Main {

    Plot plot;

    /**
     * dimension souhaitée de la zone d'affichage du graphique
     */
    final static int PREF_HEIGHT = 300;
    final static int PREF_WIDTH = 400;

    /**
     * le slider retournant une valeur entière, il est nécessaire de le diviser
     * pour ajuster la propriété range de l'objet plot avec une meilleure précision<br/>
     * range = valeur du slider / RANGE_ADJUST
     */
    final static double RANGE_ADJUST = 10;

    public static void main(String[] args) {
        Main main = new Main();
        SwingUtilities.invokeLater(main::init);
    }

    public void init() {

        plot = new Plot();

        // fenêtre principale
        JFrame frame = new JFrame("Projet TLA 2024");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // zone de tracé
        PlotPanel widgetTrace = new PlotPanel(plot);
        widgetTrace.setPreferredSize(new Dimension(PREF_WIDTH, PREF_HEIGHT));
        frame.add(widgetTrace, BorderLayout.CENTER);

        // panneau de contrôle
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel label = new JLabel("f(x)=");
        topPanel.add(label);

        /*
        JTextField proposant à l'utilisateur de taper une fonction
        */
        JTextField textInput = new JTextField(16);
        topPanel.add(textInput);

        JButton btnOk = new JButton("Ok");
        topPanel.add(btnOk);

        JSlider slider = new JSlider(JSlider.HORIZONTAL, 1, 100, (int)(plot.range*RANGE_ADJUST));
        slider.setPaintLabels(true);
        topPanel.add(slider);

        frame.add(topPanel, BorderLayout.NORTH);

        // gestionnaires des différentes actions sur l'IHM

        textInput.addActionListener(event -> {
            try {
                plot.setFunction(textInput.getText());
                widgetTrace.repaint();
            } catch (UnexpectedTokenException e) {
                JOptionPane.showMessageDialog(frame, "Syntax error at position " + e.getPosition() + ": expected " + e.getExpectedToken(), "Syntax Error", JOptionPane.ERROR_MESSAGE);
            } catch (IndexOutOfBoundsException e){
                JOptionPane.showMessageDialog(frame, "Manque d'un symbole, attendu: ), | ", "Syntax Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "An error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnOk.addActionListener(event -> {
            try {
                plot.setFunction(textInput.getText());
                widgetTrace.repaint();
            } catch (UnexpectedTokenException e) {
                JOptionPane.showMessageDialog(frame, "Syntax error at position " + e.getPosition() + ": expected " + e.getExpectedToken(), "Syntax Error", JOptionPane.ERROR_MESSAGE);
            } catch (IndexOutOfBoundsException e){
                JOptionPane.showMessageDialog(frame, "Manque d'un symbole, attendu: ), | ", "Syntax Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "An error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        slider.addChangeListener(event -> {
            plot.setRange((double)slider.getValue()/RANGE_ADJUST);
            widgetTrace.repaint();
        });

        // rend visible la fenêtre principale
        frame.pack();
        frame.setVisible(true);
    }

}
