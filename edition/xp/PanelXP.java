package xp;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;

import specialite.competence.Competence;
import statique.Style;

import composants.panel.PanelImage;

public class PanelXP extends PanelImage {
	private static final long serialVersionUID = 1L;
	private String xp;


	public PanelXP(int xp) {
		super(Competence.PATH + "xp.png");
		setLayout(new GridLayout());
		tailleImage();
		setXP(xp);
	}
	
	public void setXP(int xp) {
		this.xp = xp + "";
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setFont(Style.POLICE.deriveFont(getHeight()/2f));
		g.setColor(Color.YELLOW);
		g.drawString(xp, (int) (getWidth()/1.75 - g.getFontMetrics().stringWidth(xp)), (int) (getHeight()/1.9));
	}
	
}
