package ressources.sprites.animation.sequence;

import interfaces.Sauvegardable;
import io.IO;

import java.util.ArrayList;
import java.util.List;

import ressources.Fichiers;
import divers.Outil;

public class ModeleSequence implements Sauvegardable {
	public static final String PATH = "anim/";
	private final List<EtapeSequence> etapes;
	private final int nombre;

	
	public ModeleSequence(String nom) {
		this(Fichiers.lire(PATH + nom));
	}
	
	public ModeleSequence(IO io) {
		this(io.nextPositif());
		while(io.aBytes(2))
			etapes.add(new EtapeSequence(nombre, io));
	}
	
	public ModeleSequence(int nombre) {
		this.nombre = nombre;
		etapes = new ArrayList<EtapeSequence>();
	}
	
	public EtapeSequence getEtape(int etape) {
		return etapes.get(etape);
	}
	
	public void addEtape() {
		etapes.add(etapes.isEmpty() ? new EtapeSequence(nombre): new EtapeSequence(etapes.get(etapes.size() - 1)));
	}

	public void addEtape(int etape) {
		if(etapes.isEmpty() || etape >= etapes.size() - 1)
			addEtape();
		else etapes.add(etape, new EtapeSequence(etapes.get(etape - 1), etapes.get(etape)));
	}
	
	public void enregistrer(String nom) {
		Outil.save(this, PATH + nom);
	}

	public int getNombreEtapes() {
		return etapes.size();
	}

	public int getNombre() {
		return nombre;
	}
	
	public List<EtapeSequence> getEtapes() {
		return etapes;
	}

	public int removeEtape(int index) {
		if(!etapes.isEmpty())
			etapes.remove(index);
		if(etapes.isEmpty())
			addEtape();
		return Math.min(etapes.size() - 1, index);
	}
	
	@Override
	public IO sauvegarder(IO io) {
		io.addBytePositif(nombre);
		for(final EtapeSequence e : etapes)
			e.sauvegarder(io);
		return io;
	}

}
