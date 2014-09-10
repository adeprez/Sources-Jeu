package composants;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;

public class PointCouleur extends JComponent {
	private static final long serialVersionUID = 1L;
	private final Color couleur;
	private final int taille;
	private final boolean carre;

	
	public PointCouleur(Color couleur, int taille, boolean carre) {
		this.couleur = couleur;
		this.taille = taille;
		this.carre = carre;
		setOpaque(false);
		setPreferredSize(new Dimension(taille, taille));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		g.setColor(couleur);
		if(carre)
			g.fill3DRect((getWidth() - taille)/2, (getHeight() - taille)/2, taille, taille, true);
		else g.fillOval((getWidth() - taille)/2, (getHeight() - taille)/2, taille, taille);
	}
	
}
