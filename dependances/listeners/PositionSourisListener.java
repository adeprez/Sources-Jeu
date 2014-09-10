package listeners;

import java.awt.event.MouseEvent;
import java.util.EventListener;


public interface PositionSourisListener extends EventListener {
	public void clique(MouseEvent e);
	public void deplace(int dx, int dy);
}
