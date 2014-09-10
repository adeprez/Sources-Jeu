package layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

public class LayoutCentre implements LayoutManager {
	private final int largeur, hauteur;


	public LayoutCentre(int largeur, int hauteur) {
		this.largeur = largeur;
		this.hauteur = hauteur;
	}

	public void layout(Component parent, Component c) {
		c.setBounds((parent.getWidth()-largeur)/2, (parent.getHeight()-hauteur)/2, largeur, hauteur);
	}

	@Override
	public void layoutContainer(Container parent) {
		for(Component c : parent.getComponents())
			layout(parent, c);
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return new Dimension();
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return new Dimension(parent.getWidth(), parent.getHeight());
	}

	@Override
	public void addLayoutComponent(String name, Component comp) {}


	@Override
	public void removeLayoutComponent(Component comp) {}

}
