package tla;

import java.awt.*;
import java.util.List;

public class Plot {

    final static double STEPS = 1000;
    double range = 2;
    String function = "";

    void setFunction(String entree) throws Exception {
        try {
            List<Token> tokens = new AnalyseLexicale().analyse(entree);
            new AnalyseSyntaxique().analyse(tokens); // Check for syntax errors
            this.function = entree;
        } catch (LexicalErrorException e) {
            throw new LexicalErrorException(e.getPosition(), e.getErrorType());
        } catch (SyntaxErrorException e) {
            throw new SyntaxErrorException(e.getPosition(), e.getExpectedToken());
        }
    }

    void setRange(double range) {
        this.range = range;
    }

    void paint(Graphics2D g, double w, double h) {
        if (!function.equals("")) {
            double step = range / STEPS;
            double centerX = w / 2;
            double centerY = h / 2;
            double halfMinSize = Math.max(w, h) / 2;

            g.setColor(Color.GRAY);
            g.drawLine((int) centerX, 0, (int) centerX, (int) h);
            g.drawLine(0, (int) centerY, (int) w, (int) centerY);

            g.setColor(Color.BLACK);
            for (double x = -range; x <= range; x += step) {
                double y = testInterpretation(function.replace("x", String.valueOf(x)));
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

    private static void testAnalyseLexicale(String entree) {
        System.out.println("test analyse lexicale");
        try {
            List<Token> tokens = new AnalyseLexicale().analyse(entree);
            for (Token t : tokens) {
                System.out.println(t);
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        System.out.println();
    }

    private static void testAnalyseSyntaxique(String entree) {
        System.out.println("test analyse syntaxique");
        try {
            List<Token> tokens = new AnalyseLexicale().analyse(entree);
            Noeud racine = new AnalyseSyntaxique().analyse(tokens);
            Noeud.afficheNoeud(racine, 0);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        System.out.println();
    }

    private static double testInterpretation(String entree) {
        System.out.println("test interpretation");
        try {
            List<Token> tokens = new AnalyseLexicale().analyse(entree);
            Noeud racine = new AnalyseSyntaxique().analyse(tokens);
            Noeud.afficheNoeud(racine, 0);
            return new Interpretation().interpreter(racine);
        } catch (SyntaxErrorException e) {
            System.err.println("Syntax error at position " + e.getPosition() + ": expected " + e.getExpectedToken());
            return 0;
        } catch (Exception e) {
            e.printStackTrace(System.out);
            return 0;
        }
    }
}
