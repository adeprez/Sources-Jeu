package reseau.donnees;

import interfaces.Sauvegardable;
import interfaces.StyleListe;
import io.IO;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JComponent;
import javax.swing.JPanel;

import divers.Outil;
import exceptions.AnnulationException;

public class AdressePartie implements Sauvegardable, StyleListe {
	private final String adresse;
	private String memo;


	public AdressePartie(String adresse, String memo) {
		this.adresse = adresse;
		this.memo = memo;
	}

	public AdressePartie(IO io) {
		this(io.nextShortString(), io.nextShortString());
	}

	public InetAddress getAdresse() throws UnknownHostException {
		return InetAddress.getByName(adresse);
	}
	
	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getNomAdresse() {
		return adresse;
	}

	public String getMemo() {
		return memo;
	}

	@Override
	public IO sauvegarder(IO io) {
		return io.addShort(adresse).addShort(memo);
	}

	@Override
	public JComponent creerVue() {
		JPanel p = new JPanel(new GridLayout());
		p.setPreferredSize(new Dimension(0, 40));
		p.add(Outil.getTexte(getMemo(), true));
		p.add(Outil.getTexte(getNomAdresse(), true, new Color(0, 100, 0)));
		return p;
	}

	public static AdressePartie creer() throws AnnulationException {
		String adresse = Outil.demander("Adresse du serveur ou de la machine ?");
		try {
			InetAddress.getByName(adresse);
		} catch(UnknownHostException err) {
			throw new AnnulationException("Impossible de joindre le serveur " + adresse + ".\n" +
					"Verifiez que l'adresse saisie est correcte,\n" +
					"et que votre connexion internet est active.\n" +
					"(" + err.getMessage() + ")");
		}
		String nom = adresse;
		try {
			nom = Outil.demander("Donnez un nom pour ce serveur (facultatif)");
		} catch(AnnulationException e) {}
		return new AdressePartie(adresse, nom);
	}

}
