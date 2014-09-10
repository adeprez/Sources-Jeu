package reseau.donnees;

import interfaces.Sauvegardable;
import io.IO;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import ressources.Fichiers;
import divers.Liste;
import divers.Outil;
import exceptions.AnnulationException;

public class ListeAdresseParties extends Liste<AdressePartie> implements Sauvegardable {
	private static final long serialVersionUID = 1L;
	private static final String PATH = "parties";
	private static ListeAdresseParties instance;

	
	public static ListeAdresseParties getInstance() {
		synchronized(ListeAdresseParties.class) {
			if(instance == null)
				instance = new ListeAdresseParties();
			return instance;
		}
	}
	
	private ListeAdresseParties() {
		IO io = Fichiers.lire(PATH);
		while(io.aByte())
			add(new AdressePartie(io));
		try {
			ajout(new AdressePartie(InetAddress.getLocalHost().getHostAddress(), "Adresse locale (le " + Outil.formatCourt(new Date())+ ")"));
		} catch(UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public void ajout(AdressePartie a) {
		boolean add = true;
		for(final AdressePartie ad : this)
			if(ad.getNomAdresse().equals(a.getNomAdresse())) {
				ad.setMemo(a.getMemo());
				notifyChangement();
				add = false;
				break;
			}
		if(add) add(a);
	}
	
	public void nouvelle() {
		try {
			ajout(AdressePartie.creer());
		} catch(AnnulationException e) {
			if(e.getMessage() != null)
				Outil.erreur(e.getMessage());
		}
	}
	
	@Override
	public void notifyChangement() {
		super.notifyChangement();
		Outil.save(this, PATH);
	}
	
	@Override
	public IO sauvegarder(IO io) {
		for(final AdressePartie a : this)
			a.sauvegarder(io);
		return io;
	}

}
