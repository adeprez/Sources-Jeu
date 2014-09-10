package layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

import divers.Outil;

public class LayoutCarreHorizontal implements LayoutManager {
	private final int eX, eY;


	public LayoutCarreHorizontal(int eX, int eY) {
		this.eX = eX;
		this.eY = eY;
	}

	@Override public void addLayoutComponent(String name, Component comp) {}
	@Override public void removeLayoutComponent(Component comp) {}	

	@Override
	public void layoutContainer(Container parent) {
		if(parent.getComponentCount() > 0) {
			int espace = Outil.getValeur(eY, parent.getHeight()), dx = Outil.getValeur(eX, parent.getHeight()), 
					x = dx, h = parent.getHeight() - espace * 2;
			for(final Component c : parent.getComponents()) {
				c.setBounds(x, espace, h, h);
				x += h + dx;
			}
		}
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return new Dimension(50, 50);
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		Dimension d = new Dimension(eX, eY * 10);
		d.width = parent.getComponentCount() * (d.height + eX);
		return d;
	}
}
