package listeners;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import vision.Camera;
import divers.Taille;

public class ZoomListener implements MouseWheelListener {
	private final Taille taille;
	private int unite;
	
	
	public ZoomListener(Camera camera) {
		this(camera.getTaille());
	}
	
	public ZoomListener(Taille taille) {
		this.taille = taille;
		unite = -1;
	}
	
	public ZoomListener setUnite(int unite) {
		this.unite = unite;
		return this;
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		taille.agrandir(unite == -1 ? e.getUnitsToScroll() : (e.getUnitsToScroll() > 0 ? unite : -unite));
	}
	

}
