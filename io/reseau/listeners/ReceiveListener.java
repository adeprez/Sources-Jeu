package reseau.listeners;


import io.IO;

import java.util.EventListener;

import reseau.paquets.TypePaquet;

public interface ReceiveListener extends EventListener {
	public void recu(TypePaquet type, IO io);
}
