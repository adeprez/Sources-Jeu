package jeu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import perso.Perso;
import divers.Outil;

public class PanelScore extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final Color COULEUR = new Color(50, 50, 55, 200);
    private final JLabel score;


    public PanelScore(Perso perso) {
	setOpaque(false);
	setLayout(new BorderLayout());
	add(new JLabel(new ImageIcon(perso.getIcone())), BorderLayout.WEST);
	add(Outil.getTexte(perso.getNom(), false, Color.WHITE), BorderLayout.CENTER);
	add(score = Outil.getTexte(" 0 ", true, Color.ORANGE), BorderLayout.EAST);
    }

    public void setScore(int score) {
	this.score.setText(" " + score + " ");
    }

    @Override
    protected void paintComponent(Graphics g) {
	((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	g.setColor(COULEUR);
	g.fillRoundRect(0, 0, getWidth() + getHeight(), getHeight(), getHeight(), getHeight());
	g.setColor(Color.BLACK);
	g.drawRoundRect(0, 0, getWidth() + getHeight(), getHeight() - 1, getHeight(), getHeight());
	super.paintComponent(g);
    }

}
