package listeners;

import java.util.EventListener;

import perso.Perso;



public interface PartieListener extends EventListener {
    public void finPartie(boolean equipe, int gagnant, Perso source);
}
