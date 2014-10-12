package serveur;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import reseau.objets.InfoServeur;
import reseau.serveur.Serveur;
import statique.Style;
import base.Ecran;
import base.Fenetre;

import composants.styles.Bouton;

public class EcranAdministrationServeur extends Ecran implements ActionListener {
    private static final long serialVersionUID = 1L;
    private final EcranConfigurationServeur config;
    private final InfoServeur infos;
    private final JTabbedPane tab;
    private JButton run;


    public EcranAdministrationServeur(String createur) throws UnknownHostException {
	setName("Serveur");
	setLayout(new BorderLayout());
	add(tab = new JTabbedPane(), BorderLayout.CENTER);
	tab.setFont(Style.POLICE);
	addTab(config = new EcranConfigurationServeur(infos = new InfoServeur(createur)));
	add(run = new Bouton("Lancer le serveur").large(), BorderLayout.SOUTH);
	run.setForeground(Color.WHITE);
	run.addActionListener(this);
	afficheServeur();
    }

    public void addTab(Ecran ecran) {
	tab.addTab(ecran.getName(), ecran);
    }

    public void ouvreServeur() {
	Serveur serv = null;
	try {
	    serv = new Serveur(infos);
	    serv.configurer(config.getMap());
	    serv.lancer();
	    afficheServeur();
	    validate();
	    repaint();
	} catch(Exception e) {
	    if(serv != null)
		serv.fermer();
	    e.printStackTrace();
	    JOptionPane.showMessageDialog(null, "Creation du serveur impossible");
	}
    }

    public void afficheServeur() {
	run.setVisible(Serveur.getInstance() == null);
	if(Serveur.getInstance() != null && tab.getTabCount() <= 1) {
	    addTab(new EcranClientsServeur(Serveur.getInstance()));
	    addTab(new EcranRessources(Serveur.getInstance().getRessources()));
	}
    }

    @Override
    public void afficher(Fenetre fenetre) {
	super.afficher(fenetre);
	afficheServeur();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	run.setEnabled(false);
	ouvreServeur();
	run.setEnabled(true);
    }

}
