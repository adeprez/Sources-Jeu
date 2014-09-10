package listeners;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PositionAdapter extends MouseAdapter {
	private final PositionListener l;

	
	public PositionAdapter(PositionListener l) {
		this.l = l;
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		l.survol(e.getX(), e.getY());
	}

	@Override
	public void mousePressed(MouseEvent e) {
		l.clique(e.getX(), e.getY());
	}
	
}
