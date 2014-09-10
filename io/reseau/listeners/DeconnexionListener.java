package reseau.listeners;


import java.util.EventListener;

import reseau.AbstractClient;

public interface DeconnexionListener<E extends AbstractClient> extends EventListener {
	public void deconnexion(E client);
}
