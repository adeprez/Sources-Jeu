package serveur;

import io.IO;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import layouts.LayoutLignes;
import map.Map;
import map.objets.Objet;
import reseau.objets.InfoServeur;
import ressources.compte.Compte;
import statique.Style;
import base.Ecran;
import carte.ListeLieu;
import carte.element.Lieu;

import composants.styles.ChampTexte;

import divers.Outil;
import ecrans.ApercuMap;

public class EcranConfigurationServeur extends Ecran implements ChangeListener, ActionListener {
	private static final long serialVersionUID = 1L;
	private final ApercuMap<Objet> map;
	private final JComboBox<Lieu> lieu;
	private final InfoServeur infos;
	private final JTextField nom;
	private final Compte compte;
	private final JSpinner nbr;

	
	public EcranConfigurationServeur(InfoServeur infos) {
		super("fond/parchemin.jpg");
		setName("Configuration serveur");
		this.infos = infos;
		setLayout(new LayoutLignes());
		add(Outil.creerPanel("Adresse du serveur", infos.getNomAdresse()));
		add(Outil.creerPanel("Createur", infos.getCreateur()));
		add(Outil.creerPanel("Nom de partie", nom = new ChampTexte("Serveur de jeu")));
		add(Outil.creerPanel("Nombre de joueurs max.", nbr = new JSpinner(new SpinnerNumberModel(infos.getMaxJoueurs(), 
				0, IO.LIMITE_BYTE_POSITIF, 1))));
		add(Outil.creerPanel("Lieu", lieu = new JComboBox<Lieu>(new ListeLieu(compte = new Compte(infos.getCreateur())))));
		add(Box.createVerticalStrut(10));
		add(map = new ApercuMap<Objet>(true));
		nbr.setFont(Style.POLICE);
		nom.addActionListener(this);
		nbr.addChangeListener(this);
		lieu.addActionListener(this);
		changeLieu();
	}

	public void changeLieu() {
		if(lieu.getSelectedItem() != null) {
			Lieu l = (Lieu) lieu.getSelectedItem();
			if(l.aMap(compte.path())) {
				Map m = l.chargeMap(compte.path());
				map.setMap(m);
			}
		}
	}
	
	public Map getMap() {
		return (Map) map.getMap();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == infos)
			infos.setNomPartie(nom.getText());
		else if(e.getSource() == lieu)
			changeLieu();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		infos.setMaxJoueurs((Integer) nbr.getValue());
	}

}
