package controles;

import interfaces.Sauvegardable;
import io.IO;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import ressources.Fichiers;
import divers.Listenable;
import divers.Outil;
import exception.InvalideException;

public class ControlesClavier extends Listenable implements KeyListener, Sauvegardable {
	public static final String PATH = "preferences/controles/";
	private final Set<Integer> enfoncees;
	private final int[] controles;
	private final String nom;


	public ControlesClavier(String nom) {
		this.nom = nom;
		enfoncees = new HashSet<Integer>();
		controles = new int[TypeAction.values().length];
		TypeAction[] actions = TypeAction.values();
		IO io = Fichiers.lire(PATH + nom);
		for(int i=0 ; i<controles.length ; i++)
			controles[i] = io.aPositif() ? io.nextPositif() : actions[i].getToucheDefaut();
	}

	public ControlesClavier(String nom, int... controles) {
		this.nom = nom;
		enfoncees = new HashSet<Integer>();
		if(controles.length != TypeAction.values().length)
			this.controles = Arrays.copyOf(controles, TypeAction.values().length);
		else this.controles = controles;
		enregistrer();
	}

	public ControlesClavier(int... controles) {
		nom = "Controles";
		this.controles = controles;
		enfoncees = new HashSet<Integer>();
	}

	public ControlesClavier(boolean fleches) {
		this(fleches ? KeyEvent.VK_LEFT : KeyEvent.VK_Q, 
				fleches ? KeyEvent.VK_RIGHT : KeyEvent.VK_D,
						fleches ? KeyEvent.VK_UP : KeyEvent.VK_Z,
								fleches ? KeyEvent.VK_DOWN : KeyEvent.VK_S,
										KeyEvent.VK_SPACE,
										fleches ? KeyEvent.VK_PAUSE :KeyEvent.VK_A);
	}

	public ControlesClavier() {
		this(false);
	}

	public int[] getControles() {
		return controles;
	}

	public void setControle(byte id, int key) {
		controles[id] = key;
		enregistrer();
	}

	public void enregistrer() {
		Outil.save(this, PATH + nom);
	}

	public void addControleListener(ControleListener l) {
		addListener(ControleListener.class, l);
	}

	public void removeControleListener(ControleListener l) {
		removeListener(ControleListener.class, l);
	}

	public TypeAction getAction(int key) throws InvalideException {
		for(byte i=0 ; i<controles.length ; i++)
			if(controles[i] == key)
				return TypeAction.get(i);
		throw new InvalideException();
	}

	private void notifyListeners(TypeAction action, boolean appuie) {
		if(appuie) for(final ControleListener l : getListeners(ControleListener.class))
			l.appuie(action);
		else for(final ControleListener l : getListeners(ControleListener.class))
			l.relache(action);
	}

	@Override
	public IO sauvegarder(IO io) {
		return io.addBytesPositif(controles);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(enfoncees.add(e.getKeyCode())) try {
			notifyListeners(getAction(e.getKeyCode()), true);
		} catch(Exception err) {}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(enfoncees.remove(e.getKeyCode())) try {
			notifyListeners(getAction(e.getKeyCode()), false);
			if(!enfoncees.isEmpty())
				notifyListeners(getAction(enfoncees.iterator().next()), true);
		} catch(Exception err) {}
	}

	@Override
	public void keyTyped(KeyEvent e) {}

}
