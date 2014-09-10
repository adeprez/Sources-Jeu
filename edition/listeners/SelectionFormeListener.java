package listeners;

import java.util.EventListener;

import physique.forme.Forme;

public interface SelectionFormeListener extends EventListener {
	public void selection(Forme forme);
}
