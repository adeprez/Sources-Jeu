package reseau.ressources.listeners;

import java.util.EventListener;

import reseau.ressources.RessourceReseau;

public interface AddRessourceListener extends EventListener {
	public void add(RessourceReseau<?> r);
}
