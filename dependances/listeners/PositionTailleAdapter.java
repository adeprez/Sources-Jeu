package listeners;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PositionTailleAdapter extends MouseAdapter {
	private final PositionListener l;
	private final Dimension taille;
	
	
	public PositionTailleAdapter(PositionListener l, Dimension taille) {
		this.l = l;
		this.taille = taille;
	}
	
	public int getX(int x) {
		return get(x, taille.width);
	}
	
	public int getY(int y) {
		return get(y, taille.height);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		l.survol(getX(e.getX()), getY(e.getY()));
	}

	@Override
	public void mousePressed(MouseEvent e) {
		l.clique(getX(e.getX()), getY(e.getY()));
	}
	
	public static final int get(int p, int taille) {
		return p / taille;
	}

	
	
}
