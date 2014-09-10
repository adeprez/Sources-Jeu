package reseau.listeners;


import java.util.EventListener;

import reseau.AbstractClient;

public interface ConnexionListener<E extends AbstractClient> extends EventListener {
	public void connexion(E client);
}
