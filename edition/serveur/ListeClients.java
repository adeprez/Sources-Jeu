package serveur;

import java.util.Collection;

import reseau.AbstractClient;
import divers.Liste;

public class ListeClients extends Liste<AbstractClient> {
	private static final long serialVersionUID = 1L;

	
	public <E extends AbstractClient> ListeClients(Collection<E> clients) {
		for(final AbstractClient a : clients)
			add(a);
	}
	
	
}
