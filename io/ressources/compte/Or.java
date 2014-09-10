package ressources.compte;

import interfaces.Sauvegardable;
import io.IO;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JPanel;

import territoire.OrGraphique;

import composants.styles.Bouton;

import divers.Listenable;

public class Or extends Listenable implements Sauvegardable {
	private int or;


	public Or() {
		this(0);
	}

	public Or(int or) {
		this.or = or;
	}

	public Or(IO io) {
		this(io.nextInt());
	}

	public Or set(int or) throws ManqueRessourceException {
		if(or < 0)
			throw new ManqueRessourceException(this.or, this.or - or);
		this.or = or;
		notifyActualiseListener();
		return this;
	}

	public Or setRessources(Or ressources) {
		or = ressources.or;
		notifyActualiseListener();
		return this;
	}

	public Or vider() {
		or = 0;
		notifyActualiseListener();
		return this;
	}

	public Or utiliser(Or ress) throws ManqueRessourceException {
		testeUtilisation(ress);
		retire(ress.get());
		return this;
	}

	public Or ajouter(Or ress) {
		ajout(ress.get());
		return this;
	}

	public Or transfererVers(Or r) {
		r.ajouter(this);
		return vider();
	}

	public Or prendre(Or r) {
		r.transfererVers(this);
		return this;
	}

	public Or transfererVers(Or qte, Or r) throws ManqueRessourceException {
		utiliser(qte);
		r.ajouter(qte);
		return this;
	}

	public Or prendre(Or qte, Or r) throws ManqueRessourceException {
		r.transfererVers(qte, this);
		return this;
	}

	public boolean peutUtiliser(int qte) {
		return or - Math.abs(qte) >= 0;
	}

	public void testeUtilisation(Or ress) throws ManqueRessourceException {
		if(!peutUtiliser(ress.get()))
			throw new ManqueRessourceException(or, ress.get());
	}

	public boolean peutUtiliser(Or ress) {
		return peutUtiliser(ress.get());
	}

	public int get() {
		return or;
	}

	public Or incr(int qte) throws ManqueRessourceException {
		return set(or + qte);
	}

	public Or retire(int qte) throws ManqueRessourceException {
		return incr(-Math.abs(qte));
	}

	public Or ajout(int qte) {
		try {
			incr(Math.abs(qte));
		} catch(ManqueRessourceException e) {
			e.printStackTrace();
		}
		return this;
	}

	public JPanel getInterface(boolean editable, boolean ligne) {
		JPanel p = new JPanel(ligne ? new GridLayout() : new GridLayout(0, 1));
		p.setOpaque(false);
		if(editable || get() > 0) {
			OrGraphique ress = new OrGraphique(this, editable);
			addActualiseListener(ress);
			p.add(ress);
		}
		return p;
	}

	public JPanel getInterface(boolean editable, boolean ligne, Component c) {
		JPanel p = getInterface(editable, ligne);
		p.add(c, 0);
		return p;
	}

	public JPanel getInterface(boolean editable, boolean ligne, String texte, ActionListener l) {
		AbstractButton b = new Bouton(texte);
		JPanel p = getInterface(editable, ligne, b);
		b.addActionListener(l);
		return p;
	}

	@Override
	public String toString() {
		return or + " d'Or";
	}

	@Override
	public IO sauvegarder(IO io) {
		return io.add(or);
	}

}
