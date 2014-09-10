package composants.styles;

import java.awt.Graphics;

import javax.swing.JComponent;

public class LignePoints extends JComponent {
	private static final long serialVersionUID = 1L;
	private final int espacement;
	private final int taille;

	
	public LignePoints(int espacement, int taille) {
		this.espacement = espacement;
		this.taille = taille;
	}
	
	public LignePoints() {
		this(5, 3);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for(int x=espacement ; x<getWidth() - espacement ; x+=espacement)
			g.fillOval(x, (int) (getHeight()/1.3f) - taille/2, taille, taille);
	}
	
	
}
