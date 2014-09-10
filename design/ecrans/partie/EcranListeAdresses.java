package ecrans.partie;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.AbstractButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import reseau.donnees.AdressePartie;
import reseau.donnees.ListeAdresseParties;
import base.Ecran;
import base.Fenetre;

import composants.styles.Bouton;
import composants.styles.RenduListeStyle;
import composants.styles.ScrollPaneTransparent;

import divers.Outil;

public class EcranListeAdresses extends Ecran implements ActionListener, ListSelectionListener {
	private static final long serialVersionUID = 1L;
	private final AbstractButton retour, ajout, suppr;
	private final JList<AdressePartie> liste;
	private final Ecran precedent;

	
	public EcranListeAdresses(Ecran precedent) {
		super("fond/parchemin.jpg");
		setName("Adresses");
		setLayout(new BorderLayout());
		this.precedent = precedent;
		JPanel haut = new JPanel();
		haut.setOpaque(false);
		haut.add(retour = new Bouton("<- Retour").large());
		haut.add(ajout = new Bouton("+ Ajouter...").large());
		haut.add(suppr = new Bouton("- Supprimer").large());
		try {
			haut.add(Outil.getTexteGras(" Votre adresse : " + InetAddress.getLocalHost().getHostAddress(), new Color(0, 100, 0)));
		} catch(UnknownHostException e) {
			e.printStackTrace();
		}
		retour.addActionListener(this);
		ajout.addActionListener(this);
		suppr.addActionListener(this);
		add(haut, BorderLayout.NORTH);
		add(new ScrollPaneTransparent(liste = new JList<AdressePartie>(ListeAdresseParties.getInstance())), BorderLayout.CENTER);
		liste.setCellRenderer(new RenduListeStyle());
		liste.addListSelectionListener(this);
		liste.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
	public void rejoindre() {
		
	}
	
	@Override
	public void afficher(Fenetre fenetre) {
		super.afficher(fenetre);
		valueChanged(null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == retour)
			changer(precedent);
		else if(e.getSource() == ajout) {
			ListeAdresseParties.getInstance().nouvelle();
			valueChanged(null);
		}
		else if(e.getSource() == suppr && liste.getSelectedValue() != null) {
			ListeAdresseParties.getInstance().remove(liste.getSelectedValue());
			valueChanged(null);
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(e == null || !e.getValueIsAdjusting())
			suppr.setVisible(liste.getSelectedValue() != null);
	}

}
