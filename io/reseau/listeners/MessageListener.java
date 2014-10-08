package reseau.listeners;

import io.IO;

import java.util.EventListener;

import perso.Perso;

public interface MessageListener extends EventListener {
    public void message(int type, String message, Perso expediteur, IO io);
}
