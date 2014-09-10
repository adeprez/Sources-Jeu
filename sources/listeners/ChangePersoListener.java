package listeners;

import java.util.EventListener;

import perso.Perso;

public interface ChangePersoListener extends EventListener {
	public void change(Perso ancien, Perso nouveau);
}
