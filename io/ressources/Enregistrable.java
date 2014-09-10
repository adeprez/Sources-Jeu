package ressources;

import interfaces.Actualisable;
import interfaces.Sauvegardable;
import io.IO;
import divers.Listenable;
import divers.Outil;

public abstract class Enregistrable extends Listenable implements Sauvegardable {
	private final String nom;
	private boolean ignore;
	
	
	public Enregistrable(String nom) {
		this.nom = nom;
	}
	
	public Enregistrable setIgnoreEnregistrement(boolean ignore) {
		this.ignore = ignore;
		return this;
	}
	
	public boolean ignoreEnregistrement() {
		return ignore;
	}
	
	public String getNom() {
		return nom;
	}

	public void enregistrer() {
		if(!ignore)
			Outil.save(this, nom);
		notifyActualiseListeners();
	}
	
	public IO lire() {
		return Fichiers.lire(nom);
	}
	
	public void addActualiseListener(Actualisable l) {
		addListener(Actualisable.class, l);
	}
	
	public void removeActualiseListener(Actualisable l) {
		removeListener(Actualisable.class, l);
	}
	
	protected final void notifyActualiseListeners() {
		for(final Actualisable l : getListeners(Actualisable.class))
			l.actualise();
	}
	
}
