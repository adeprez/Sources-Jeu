package jeu;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import layouts.LayoutLignes;
import partie.PartieClient;
import perso.Perso;
import reseau.ressources.RessourceReseau;
import reseau.ressources.TypeRessource;
import statique.Style;
import base.Ecran;

import composants.styles.Bouton;
import composants.styles.ScrollPaneTransparent;

import divers.Outil;
import ecrans.partie.EcranChoixPartie;

public class EcranResultatPartie extends Ecran implements ActionListener, Comparator<Component> {
    private static final long serialVersionUID = 1L;
    private final AbstractButton quitter;
    private final PartieClient partie;


    public EcranResultatPartie(PartieClient partie, String resultat) {
	super("fond/parchemin.jpg");
	this.partie = partie;
	setName(resultat);
	setLayout(new BorderLayout(10, 10));
	JPanel scores = new JPanel(new LayoutLignes());
	scores.setOpaque(false);
	List<Component> s = new ArrayList<Component>();
	for(final Entry<Integer, RessourceReseau<?>> rp : partie.getRessources().get(TypeRessource.PERSO).entrySet()) {
	    JPanel pa = new JPanel(new BorderLayout(20, 10));
	    Perso p = (Perso) rp.getValue().getRessource();
	    JLabel l = new JLabel(p.getNom() + (partie.getTypeJeu().enEquipe() ? " (équipe " + p.getEquipe()  + ")" : ""),
		    new ImageIcon(p.getIcone()), SwingConstants.LEFT);
	    l.setFont(Style.TITRE);
	    pa.add(l, BorderLayout.WEST);
	    int score = partie.getScore(rp.getKey());
	    pa.add(Outil.getTexte(score + " points ", true), BorderLayout.EAST);
	    pa.setOpaque(false);
	    pa.setName(score + "");
	    s.add(pa);
	}
	Collections.sort(s, this);
	for(final Component c : s)
	    scores.add(c);
	quitter = new Bouton("Fermer").large();
	quitter.addActionListener(this);
	add(Outil.getTexte(resultat + " - " + partie.getTypeJeu().getNom(), true), BorderLayout.NORTH);
	add(quitter, BorderLayout.SOUTH);
	add(new ScrollPaneTransparent(scores), BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	changer(new EcranChoixPartie(partie.getClient().getCompte()));
    }

    @Override
    public int compare(Component o1, Component o2) {
	return o2.getName().compareTo(o1.getName());
    }


}
