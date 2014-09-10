package reseau.client;

import io.IO;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import reseau.AbstractClient;
import reseau.objets.InfoServeur;
import reseau.paquets.TypePaquet;
import reseau.ressources.RessourcesReseau;
import reseau.serveur.Serveur;
import exceptions.AnnulationException;

public class RechercheServeur extends AbstractClient {
	public static final int TEMPS_LIMITE_RECEPTION = 500;
	private InfoServeur infos;

	
	public RechercheServeur(InetAddress adresse) throws IOException {
		super(creerSocket(adresse), new RessourcesReseau());
	}

	public InfoServeur getInfoServeur() throws AnnulationException {
		if(infos == null)
			throw new AnnulationException("Ce serveur n'a pas transmis ses informations");
		return infos;
	}
	
	@Override
	public boolean estServeur() {
		return false;
	}

	@Override
	protected boolean traiter(TypePaquet type, IO in) {
		if(type == TypePaquet.INFO_SERVEUR) {
			infos = new InfoServeur(in);
			fermer();
		}
		return false;
	}
	
	private static Socket creerSocket(InetAddress adresse) throws IOException {
		Socket socket = new Socket();
		socket.setSoTimeout(TEMPS_LIMITE_RECEPTION);
		socket.connect(new InetSocketAddress(adresse, Serveur.DEFAULT_PORT), TEMPS_LIMITE_RECEPTION);
		return socket;
	}
	
}
