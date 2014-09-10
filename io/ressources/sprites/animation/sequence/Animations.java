package ressources.sprites.animation.sequence;

import java.util.HashMap;
import java.util.Map;

import ressources.Fichiers;

public class Animations {
	private static Animations instance;
	private final Map<String, ModeleSequence> sequences;
	
	
	public static Animations getInstance() {
		synchronized(Animations.class) {
			if(instance == null)
				instance = new Animations();
			return instance;
		}
	}
	
	private Animations() {
		sequences = new HashMap<>();
		for(final String s : Fichiers.getNomsFichiers(ModeleSequence.PATH))
			sequences.put(s, new ModeleSequence(s));
	}
	
	public Sequence getSequence(String nom, boolean boucle) {
		ModeleSequence m = sequences.get(nom);
		if(m == null)
			m = charge(nom);
		Sequence s = new Sequence(m);
		s.setBoucle(boucle);
		return s;
	}

	public ModeleSequence charge(String nom) {
		ModeleSequence s = new ModeleSequence(nom);
		sequences.put(nom, s);
		return s;
	}

	public void ajout(String nom, ModeleSequence s) {
		sequences.put(nom, s);
	}

	public Map<String, ModeleSequence> getSequences() {
		return sequences;
	}

}
