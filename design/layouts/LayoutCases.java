package layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

public class LayoutCases implements LayoutManager {
	private final Dimension taille;
	private int espaceX, espaceY;
	
	
	public LayoutCases(Dimension taille) {
		this.taille = taille;
	}
	
	public LayoutCases(int taille) {
		this(new Dimension(taille, taille));
	}
	
	public LayoutCases(int taille, int espaceX, int espaceY) {
		this(taille);
		setEspace(espaceX, espaceY);
	}
	
	public LayoutCases setEspace(int espaceX, int espaceY) {
		this.espaceX = espaceX;
		this.espaceY = espaceY;
		return this;
	}

	@Override public void addLayoutComponent(String name, Component comp) {}
	@Override public void removeLayoutComponent(Component comp) {}	

	@Override
	public void layoutContainer(Container parent) {
		int x = espaceX, y = espaceY;
		for(final Component c : parent.getComponents()) {
			c.setBounds(x, y, taille.width, taille.height);
			x += taille.width + espaceX;
			if(x + taille.width > parent.getWidth()) {
				y += taille.height + espaceY;
				x = espaceX;
			}
		}
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return new Dimension(taille.width, taille.height);
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		Dimension d = new Dimension(espaceX, espaceY);
		for(int i=0 ; i<parent.getComponentCount() ; i++) {
			d.width += taille.width + espaceX;
			if(d.width + taille.width > parent.getWidth()) {
				d.height += taille.height + espaceY;
			}
		}
		return d;
	}
}
