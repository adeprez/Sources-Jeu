package composants;

import interfaces.Actualisable;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import statique.Style;
import divers.Taille;

public class DimensionGraphique extends JPanel implements ChangeListener {
	private static final long serialVersionUID = 1L;
	private final JSpinner largeur, hauteur;
	private Taille taille;

	
	public DimensionGraphique() {
		this(new Taille(64).setLargeurMin(8).setLargeurMax(512));
	}
	
	public DimensionGraphique(Actualisable l) {
		this();
		addActualiseListener(l);
	}
	
	public DimensionGraphique(Taille taille) {
		super(new GridLayout(1, 2));
		setOpaque(false);
		this.taille = taille;
		largeur = new JSpinner(new SpinnerNumberModel(taille.getLargeur(), taille.getlargeurMin(), taille.getLargeurMax(), 8));
		hauteur = new JSpinner(new SpinnerNumberModel(taille.getHauteur(), taille.getHauteurMin(), taille.getHauteurMax(), 8));
		largeur.setToolTipText("Largeur");
		hauteur.setToolTipText("Hauteur");
		largeur.setFont(Style.POLICE);
		hauteur.setFont(Style.POLICE);
		largeur.addChangeListener(this);
		hauteur.addChangeListener(this);
		add(largeur);
		add(hauteur);
	}
	
	public DimensionGraphique(Taille taille, Actualisable l) {
		this(taille);
		addActualiseListener(l);
	}
	
	public Dimension getDimension() {
		return new Dimension((Integer) largeur.getValue(), (Integer) hauteur.getValue());
	}
	
	public DimensionGraphique setTaille(Taille taille) {
		this.taille = taille;
		largeur.setValue(taille.getLargeur());
		hauteur.setValue(taille.getHauteur());
		return this;
	}
	
	public Taille getTaille() {
		return taille;
	}
	
	public void addActualiseListener(Actualisable l) {
		listenerList.add(Actualisable.class, l);
	}
	
	public void removeActualiseListener(Actualisable l) {
		listenerList.remove(Actualisable.class, l);
	}
	
	protected final void notifyActualiseListeners() {
		for(final Actualisable l : listenerList.getListeners(Actualisable.class))
			l.actualise();
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		if(e.getSource() == largeur)
			taille.forceLargeur((Integer) largeur.getValue());
		else taille.forceHauteur((Integer) hauteur.getValue());
		notifyActualiseListeners();
	}
	
}
