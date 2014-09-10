package xp;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import layouts.LayoutPerso;
import layouts.PlaceurComposants;
import listeners.CliquePanelCompetencesListener;
import specialite.Specialite;

public class PanelSpecialite extends JPanel implements PlaceurComposants {
	public static final Color COULEUR_CUIVRE = new Color(240, 160, 40);
	private static final long serialVersionUID = 1L;
	private final PanelCompetences comp;
	private final JProgressBar xp;

	
	public PanelSpecialite(CliquePanelCompetencesListener l, Specialite spe) {
		setName(spe.toString());
		setOpaque(false);
		setLayout(new LayoutPerso(this));
		comp = new PanelCompetences(l, spe);
		xp = new JProgressBar(SwingConstants.VERTICAL);
		xp.setStringPainted(true);
		xp.setString(spe.toString());
		add(comp);
		add(xp);
		setXP(spe.getSource().getXP(spe.getType()));
	}
	
	public void setXP(int nxp) {
		comp.setXP(nxp);
		xp.setValue((nxp * 100)/Specialite.getXPPalier(comp.getPanelsCompetences().length));
		xp.setToolTipText(getName() + " (" + nxp + " expérience)");
	}
	
	@Override
	public void paintComponent(Graphics g) {
		dessineFondCuivre(this, g);
		super.paintComponent(g);
	}

	@Override
	public void layout(Container parent) {
		xp.setBounds(0, getHeight()/4, getHeight()/8, getHeight()/2);
		comp.setBounds(0, 0, parent.getWidth(), parent.getHeight());
	}
	
	public static void dessineFondCuivre(Component c, Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setPaint(new GradientPaint(0, 0, Color.BLACK, 0, c.getHeight()/6, COULEUR_CUIVRE));
		g.fillRect(0, 0, c.getWidth(), c.getHeight()/2);
		g2.setPaint(new GradientPaint(0, c.getHeight()/2, COULEUR_CUIVRE, 0, c.getHeight(), Color.BLACK));
		g.fillRect(0, c.getHeight()/2, c.getWidth(), c.getHeight()/2);
	}

}
