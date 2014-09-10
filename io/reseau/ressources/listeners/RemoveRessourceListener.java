package reseau.ressources.listeners;

import java.util.EventListener;

import reseau.ressources.RessourceReseau;

public interface RemoveRessourceListener extends EventListener {
	public void remove(RessourceReseau<?> r);
}
