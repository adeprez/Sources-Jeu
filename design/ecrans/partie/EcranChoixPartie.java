package ecrans.partie;

import interfaces.Actualisable;
import interfaces.DoubleCliquable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.AbstractButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import listeners.DoubleCliqueListener;
import reseau.client.ChargementPartie;
import reseau.client.TentativeConnexion;
import reseau.donnees.ListeAdresseParties;
import reseau.donnees.ListeParties;
import reseau.objets.InfoServeur;
import ressources.compte.Compte;
import base.Ecran;
import base.Fenetre;

import composants.styles.Bouton;
import composants.styles.EcranAttente;
import composants.styles.RenduListeStyle;
import composants.styles.ScrollPaneTransparent;

import divers.Outil;
import ecrans.connexion.EcranPersos;

public class EcranChoixPartie extends Ecran implements Actualisable, ActionListener, ListSelectionListener, DoubleCliquable {
	private static final long serialVersionUID = 1L;
	private final AbstractButton retour, adresses, refresh, rejoindre;
	private final JList<InfoServeur> list;
	private final ListeParties liste;
	private final Compte compte;


	public EcranChoixPartie(Compte compte) {
		super("fond/parchemin.jpg");
		this.compte = compte;
		setName("Choix partie");
		setLayout(new BorderLayout());
		JPanel haut = new JPanel();
		haut.setOpaque(false);
		add(haut, BorderLayout.NORTH);
		list = new JList<InfoServeur>(liste = new ListeParties());
		list.setCellRenderer(new RenduListeStyle());
		list.addListSelectionListener(this);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.addMouseListener(new DoubleCliqueListener(this));
		add(new ScrollPaneTransparent(list));
		add(liste.getAvancement(), BorderLayout.SOUTH);
		haut.add(retour = new Bouton("<- Retour").large());
		haut.add(adresses = new Bouton().large());
		haut.add(refresh = new Bouton("Rafraichir").large());
		haut.add(rejoindre = new Bouton("Rejoindre").large());
		try {
			haut.add(Outil.getTexteGras(" Votre adresse : " + InetAddress.getLocalHost().getHostAddress(), new Color(0, 100, 0)));
		} catch(UnknownHostException e) {
			e.printStackTrace();
		}
		retour.addActionListener(this);
		adresses.addActionListener(this);
		refresh.addActionListener(this);
		rejoindre.addActionListener(this);
	}

	public void rejoindre() {
		if(list.getSelectedValue() != null) {
			InfoServeur info = list.getSelectedValue();
			try {
				TentativeConnexion t = new TentativeConnexion(info, compte);
				new EcranAttente("Connexion...", t);
				ChargementPartie c = new ChargementPartie(t);
				changer(new EcranChargementPartie(c));
				c.executer();
			} catch(Exception err) {
				Outil.erreur(err.getMessage());
				err.printStackTrace();
			}
		}
	}

	@Override
	public void afficher(Fenetre fenetre) {
		super.afficher(fenetre);
		actualise();
	}

	@Override
	public void actualise() {
		adresses.setText("Adresses connues (" + ListeAdresseParties.getInstance().size() + ")");
		liste.executer();
		valueChanged(null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == retour)
			changer(new EcranPersos(compte));
		if(e.getSource() == adresses)
			changer(new EcranListeAdresses(this));
		else if(e.getSource() == refresh)
			actualise();
		else if(e.getSource() == rejoindre)
			rejoindre();
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(e == null || !e.getValueIsAdjusting())
			rejoindre.setVisible(list.getSelectedValue() != null);
	}

	@Override
	public void doubleClique(MouseEvent e) {
		rejoindre();
	}


}
