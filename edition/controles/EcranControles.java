package controles;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTabbedPane;

import ressources.Fichiers;
import base.Ecran;

import composants.styles.Bouton;

import divers.Outil;
import exceptions.AnnulationException;

public class EcranControles extends Ecran implements ActionListener {
	private static final long serialVersionUID = 1L;
	private final JTabbedPane onglets;
	private final JButton nouveau;
	
	
	
	public EcranControles() {
		super(new BorderLayout());
		setName("Controles");
		onglets = new JTabbedPane();
		nouveau = new Bouton("Nouveau...");
		nouveau.setForeground(Color.WHITE);
		nouveau.addActionListener(this);
		add(onglets, BorderLayout.CENTER);
		add(nouveau, BorderLayout.SOUTH);
		ajout(Fichiers.getNomsFichiers(ControlesClavier.PATH));
	}
	
	private void ajout(String... nomsFichiers) {
		for(String nom : nomsFichiers)
			ajout(nom);
	}

	public void ajout(String nom) {
		onglets.addTab(nom, new PanelControles(nom));
	}

	@Override
	public boolean fermer() {
		return true;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			ajout(Outil.demander("Nom ?"));
		} catch (AnnulationException err) {}
	}

}
