package layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

public class LayoutPerso implements LayoutManager {
	private final PlaceurComposants placeur;
	private Dimension taille;

	
	public LayoutPerso(PlaceurComposants placeur) {
		this(placeur, new Dimension());
	}
	
	public LayoutPerso(PlaceurComposants placeur, Dimension taille) {
		this.taille = taille;
		this.placeur = placeur;
	}
	
	public LayoutPerso(PlaceurComposants placeur, int w, int h) {
		this(placeur, new Dimension(w, h));
	}
	
	public void setTaille(Dimension taille) {
		this.taille = taille;
	}

	@Override
	public void layoutContainer(Container parent) {
		placeur.layout(parent);
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return new Dimension();
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return taille;
	}

	@Override
	public void removeLayoutComponent(Component comp) {}
	
	@Override
	public void addLayoutComponent(String name, Component comp) {}

}
