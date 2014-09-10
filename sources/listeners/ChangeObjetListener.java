package listeners;

import java.util.EventListener;

import map.objets.Objet;

public interface ChangeObjetListener extends EventListener {
	public void change(Objet ancien, Objet nouveau);
}
