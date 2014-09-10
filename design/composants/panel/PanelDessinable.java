package composants.panel;

import interfaces.Dessinable;
import interfaces.Dessineur;
import interfaces.LocaliseDessinable;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.List;

import javax.swing.JPanel;

public class PanelDessinable extends JPanel implements Dessineur {
	private static final long serialVersionUID = 1L;
	private final List<Dessinable> dessinables;

	
	public PanelDessinable(List<Dessinable> dessinables) {
		this.dessinables = dessinables;
		setOpaque(false);
	}

	public PanelDessinable(Dessinable... dessinables) {
		this(Arrays.asList(dessinables));
	}

	@Override
	public void paintComponent(Graphics g) {
		for(final Dessinable dessinable : dessinables)
			dessinable.predessiner((Graphics2D) g, getBounds(), 0);
		super.paintComponent(g);
		for(final Dessinable dessinable : dessinables)
			dessinable.dessiner((Graphics2D) g, getBounds(), 0);
		for(final Dessinable dessinable : dessinables)
			dessinable.surdessiner((Graphics2D) g, getBounds(), 0);
	}

	@Override
	public void ajoutDessinable(LocaliseDessinable dessinable) {
		dessinables.add(dessinable);
		repaint();
	}

	@Override
	public void removeDessinable(LocaliseDessinable dessinable) {
		dessinables.remove(dessinable);
		repaint();
	}

}
