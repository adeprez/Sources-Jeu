package perso;

import interfaces.Nomme;
import interfaces.Sauvegardable;
import io.IO;

import java.awt.Dimension;

public class InformationsPerso implements Sauvegardable, Nomme {
	public static final String PATH = "infos";
	private final int largeur;
	private final String nom;
	private final Sexe sexe;
	
	
	public InformationsPerso(int largeur, String nom, Sexe sexe) {
		this.largeur = largeur;
		this.nom = nom;
		this.sexe = sexe;
	}
	
	public InformationsPerso(IO io) {
		this(io.nextPositif(), io.nextShortString(), Sexe.get(io.nextShortInt()));
	}

	public Sexe getSexe() {
		return sexe;
	}
	
	public int getLargeur() {
		return largeur;
	}
	
	public int getHauteur() {
		return largeur * 4;
	}
	
	public Dimension getTaille() {
		return new Dimension(getLargeur(), getHauteur());
	}

	@Override
	public String toString() {
		return nom + " " + sexe;
	}

	@Override
	public IO sauvegarder(IO io) {
		return io.addBytePositif(largeur).addShort(nom).addShort(sexe.getID());
	}

	@Override
	public String getNom() {
		return nom;
	}
	
	
}
