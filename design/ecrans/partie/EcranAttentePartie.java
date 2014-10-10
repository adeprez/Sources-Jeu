package ecrans.partie;

import interfaces.Actualisable;
import io.IO;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JPanel;

import jeu.EcranJeu;
import partie.PartieClient;
import partie.modeJeu.DeathMatchEquipe;
import reseau.client.Client;
import reseau.listeners.ReceiveListener;
import reseau.objets.InfoServeur;
import reseau.paquets.Paquet;
import reseau.paquets.TypePaquet;
import base.Ecran;

import composants.HorlogeProgression;
import composants.styles.Bouton;
import composants.styles.ScrollPaneTransparent;

import divers.Outil;

public class EcranAttentePartie extends Ecran implements ReceiveListener, Actualisable, ActionListener {
	private static final long serialVersionUID = 1L;
	private final HorlogeProgression horloge;
	private final AbstractButton commencer;
	private final PartieClient partie;
	private final Client client;
	private final JPanel c;


	public EcranAttentePartie(Client client) {
		super("fond/parchemin.jpg");
		this.client = client;
		setName("Patientez...");
		setLayout(new BorderLayout());
		add(horloge = new HorlogeProgression(client.getHorloge()).setTexte("Attente..."), BorderLayout.NORTH);
		add(commencer = new Bouton("Pret a commencer !").large(), BorderLayout.SOUTH);
		add(new ScrollPaneTransparent(c = new JPanel(new GridLayout())));
		c.setOpaque(false);
		partie = new PartieClient(client);
		client.getRessources().addActualiseListener(this);
		client.addReceiveListener(this);
		client.write(new Paquet(TypePaquet.ETAT_PARTIE));
		actualise();
		commencer.addActionListener(this);
	}

	@Override
	public boolean fermer() {
		if(super.fermer()) {
			client.getRessources().removeActualiseListener(this);
			partie.getClient().getHorloge().removeHorlogeListener(horloge);
			partie.getClient().removeReceiveListener(this);
		}
		return false;
	}
	
	public void traiteEtatPartie(int etat) {
		switch(etat) {
		case InfoServeur.ETAT_JEU:
			changer(new EcranJeu(partie));
			break;
		case InfoServeur.ETAT_ATTENTE:
			client.write(new Paquet(TypePaquet.TEMPS));
			break;
		case InfoServeur.ETAT_OFF:
			Outil.erreur("Cette partie est deja terminee");
			changer(new EcranChoixPartie(client.getCompte()));
			break;
		default:
			break;
		}
	}

	@Override
	public void recu(TypePaquet type, IO io) {
		switch(type) {
		case ETAT_PARTIE:
			traiteEtatPartie(io.nextPositif());
			break;
		default:
			break;
		}
	}

	@Override
	public void actualise() {
		c.removeAll();
		for(final Component cc : DeathMatchEquipe.creerComposants(client.getRessources()))
			c.add(cc);
		validate();
		repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		client.write(new Paquet(TypePaquet.FIN_CHARGEMENT));
		commencer.setVisible(false);
	}

}
