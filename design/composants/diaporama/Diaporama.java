package composants.diaporama;

import interfaces.Fermable;
import interfaces.Lancable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import listeners.ChangeDiapoListener;
import base.Ecran;
import divers.Outil;

public class Diaporama<E extends Component> extends Ecran implements ChangeDiapoListener {
	private static final long serialVersionUID = 1L;
	private final ModeleDiaporama<E> modele;
	private ControleurDiaporama<E> controleur;
	private Component centre;


	public Diaporama(ModeleDiaporama<E> modele) {
		super("fond/parchemin.jpg");
		this.modele = modele;
		setLayout(new BorderLayout());
		modele.addChangeDiapoListener(this);
		add(centre = Outil.getTexte("(Aucun)", false, Color.DARK_GRAY), BorderLayout.CENTER);
	}

	public Diaporama() {
		this(new ModeleDiaporama<E>());
		controleur = new ControleurBarre<E>(this);
	}

	public Diaporama(ModeleDiaporama<E> modele, ControleurDiaporama<E> controleur) {
		this(modele);
		this.controleur = controleur;
	}

	public Diaporama(boolean droiteGauche) {
		this(new ModeleDiaporama<E>());
		controleur = droiteGauche ? new ControleurGaucheDroite<E>(this) : new ControleurBarre<E>(this);
	}

	public void setControleurDiapo(ControleurDiaporama<E> controleur) {
		this.controleur = controleur;
	}

	public ModeleDiaporama<E> getModele() {
		return modele;
	}

	public ControleurDiaporama<E> getControleur() {
		return controleur;
	}

	public E getSelection() {
		return modele.getElement();
	}

	@Override
	public void changeDiapo(Object diapo) {
		if(centre != null) {
			remove(centre);
			if(centre instanceof Fermable)
				((Fermable) centre).fermer();
		} if(modele.aElement()) {
			centre = modele.getElement();
			setName(centre.getName());
			add(centre, BorderLayout.CENTER);
			if(centre instanceof Lancable)
				((Lancable) centre).lancer();
		}
		if(controleur != null)
			controleur.actualise();
		validate();
		repaint();
	}

}
