package listeners;

import java.util.EventListener;

import physique.forme.Forme;

public interface ChangeFormeListener extends EventListener {
	public void change(Forme forme);
}
