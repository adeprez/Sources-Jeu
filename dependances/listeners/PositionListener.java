package listeners;

import java.util.EventListener;

public interface PositionListener extends EventListener {
	public void clique(int x, int y);
	public void survol(int x, int y);
}
