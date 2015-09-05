package listeners;

import java.util.EventListener;

import perso.Perso;


public interface AjoutPersoListener extends EventListener {
    public void ajout(Perso perso);
}
