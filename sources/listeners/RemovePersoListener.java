package listeners;

import java.util.EventListener;

import perso.Perso;

public interface RemovePersoListener extends EventListener {
    public void remove(Perso perso);
}
