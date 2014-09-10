package listeners;

import java.util.EventListener;

public interface ContactListener extends EventListener {
	public void contactSol(int gravite);
	public void contactHaut(int vitesse);
}
