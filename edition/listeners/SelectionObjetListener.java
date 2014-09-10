package listeners;

import java.util.EventListener;

import map.objets.Objet;

public interface SelectionObjetListener extends EventListener {
	public void selection(Objet objet, boolean cliqueDroit);
}
