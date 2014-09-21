package base;

import interfaces.Affichable;
import interfaces.Fermable;
import interfaces.Lancable;
import interfaces.Nomme;

import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;

import ressources.Images;
import statique.Style;

import composants.panel.PanelImage;

public abstract class Ecran extends PanelImage implements Lancable, Fermable, Nomme, Affichable {
    private static final long serialVersionUID = 1L;
    private Fenetre fenetre;


    public Ecran() {
	this(new GridLayout());
    }

    public Ecran(String image) {
	this(Images.get(image, true));
    }

    public Ecran(LayoutManager layout) {
	setLayout(layout);
    }

    public Ecran(BufferedImage image) {
	super(image, true);
    }

    public void setFenetre(Fenetre fenetre) {
	this.fenetre = fenetre;
	afficher(fenetre);
    }

    public Fenetre getFenetre() {
	return fenetre == null ? Fenetre.getInstance() : fenetre;
    }

    public void changer(Ecran nouveau) {
	getFenetre().changer(nouveau).setVisible(true);
    }

    public void setBordure(String nom) {
	setBorder(BorderFactory.createTitledBorder(null, nom,
		TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, Style.TITRE));
    }

    public void setBordureCentre(String nom) {
	setBorder(BorderFactory.createTitledBorder(null, nom,
		TitledBorder.CENTER, TitledBorder.CENTER, Style.TITRE));
    }

    @Override
    public void afficher(Fenetre fenetre) {}

    @Override
    public String getNom() {
	return getName();
    }

    @Override
    public boolean fermer() {
	return true;
    }

    @Override
    public boolean lancer() {
	return true;
    }



}
