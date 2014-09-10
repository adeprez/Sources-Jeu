package reseau.ressources.listeners;

import java.util.EventListener;

import reseau.ressources.RessourceReseau;

public interface ChangeRessourceListener<E> extends EventListener {
	public void change(E ancien, RessourceReseau<E> r);
}
