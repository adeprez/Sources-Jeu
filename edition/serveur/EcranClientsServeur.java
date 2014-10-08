package serveur;

import interfaces.Actualisable;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import reseau.AbstractClient;
import reseau.listeners.ConnexionListener;
import reseau.listeners.DeconnexionListener;
import reseau.paquets.PaquetMessage;
import reseau.serveur.ClientServeur;
import reseau.serveur.Serveur;
import base.Ecran;

import composants.styles.Bouton;
import composants.styles.RenduListeStyle;
import composants.styles.ScrollPaneTransparent;

import divers.Outil;
import exceptions.AnnulationException;

public class EcranClientsServeur extends Ecran implements ConnexionListener<ClientServeur>,
DeconnexionListener<ClientServeur>, Actualisable, ListSelectionListener, ActionListener {
    private static final long serialVersionUID = 1L;
    private final JList<AbstractClient> liste;
    private final JButton message, deco;
    private final Serveur serveur;
    private final JPanel bas;


    public EcranClientsServeur(Serveur serveur) {
	super("fond/parchemin.jpg");
	setName("Clients");
	setLayout(new BorderLayout());
	this.serveur = serveur;
	serveur.addConnexionListener(this);
	serveur.addDeconnexionListener(this);
	add(new ScrollPaneTransparent(liste = new JList<AbstractClient>()), BorderLayout.CENTER);
	liste.setCellRenderer(new RenduListeStyle());
	liste.setOpaque(false);
	liste.addListSelectionListener(this);
	add(bas = new JPanel(), BorderLayout.SOUTH);
	bas.setOpaque(false);
	bas.setVisible(false);
	bas.add(message = new Bouton("Envoyer un message").large());
	bas.add(deco = new Bouton("Deconnecter le client").large());
	bas.setOpaque(false);
	message.addActionListener(this);
	deco.addActionListener(this);
	actualise();
    }

    @Override
    public void actualise() {
	liste.setModel(new ListeClients(serveur.getClients()));
    }

    @Override
    public void deconnexion(ClientServeur client) {
	actualise();
    }

    @Override
    public void connexion(ClientServeur client) {
	actualise();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	for(AbstractClient a : liste.getSelectedValuesList())
	    if(e.getSource() == message) try {
		a.write(new PaquetMessage(PaquetMessage.SERVEUR, Outil.demander("Message a envoyer?")));
	    } catch(AnnulationException err) {}
	    else if(e.getSource() == deco)
		a.fermer();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
	bas.setVisible(liste.getSelectedIndex() != -1);
    }

}
