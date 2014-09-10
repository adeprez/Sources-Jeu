package listeners;

import java.util.EventListener;

public interface ChangeEtatListener extends EventListener {
	public void changeEtat(boolean etat);
}
