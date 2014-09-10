package listeners;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SurvolListener extends MouseAdapter {
	private PositionSurvolListener l;
	
	
	public SurvolListener() {}
	
	public SurvolListener(PositionSurvolListener l) {
		this.l = l;
	}
	
	public void setPositionSurvolListener(PositionSurvolListener l) {
		this.l = l;
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if(l != null)
			l.sort(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(l != null)
			l.survol(e);
	}
	
}
