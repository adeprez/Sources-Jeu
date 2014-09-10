package ecrans.partie;

import interfaces.Actualisable;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

import reseau.client.ChargementPartie;
import reseau.listeners.FinChargementListener;
import statique.Style;
import base.Ecran;
import divers.Outil;

public class EcranChargementPartie extends Ecran implements Actualisable, FinChargementListener {
	private static final long serialVersionUID = 1L;
	private final ChargementPartie chargement;
	private final JProgressBar avancement;
	private final JLabel txt;

	
	public EcranChargementPartie(ChargementPartie chargement) {
		super("fond/gris.jpg");
		setName("Chargement...");
		setLayout(new BorderLayout());
		this.chargement = chargement;
		chargement.addActualiseListener(this);
		chargement.addFinChargementListenerListener(this);
		add(avancement = new JProgressBar(), BorderLayout.SOUTH);
		add(txt = Outil.getTexte("", true, Color.ORANGE), BorderLayout.NORTH);
		avancement.setStringPainted(true);
		avancement.setFont(Style.POLICE);
	}

	@Override
	public void actualise() {
		txt.setText(chargement.getNom());
		avancement.setValue(chargement.getAvancement());
	}

	@Override
	public void finChargement() {
		changer(new EcranAttentePartie(chargement.getClient()));
	}

}
