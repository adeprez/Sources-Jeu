package reseau.objets;

import interfaces.Sauvegardable;
import interfaces.StyleListe;
import io.IO;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JComponent;
import javax.swing.JPanel;

import partie.modeJeu.TypeJeu;
import temps.Horloge;
import temps.HorlogeListener;
import divers.Outil;

public class InfoServeur implements Sauvegardable, StyleListe, HorlogeListener {
    public static final int ETAT_OFF = 0, ETAT_ATTENTE = 1, ETAT_JEU = 2, ETAT_FINI = 3;
    private final int delaiIni, tempsIni;
    private final String adresse;
    private String nomPartie, createur;
    private int maxJoueurs, joueurs, etat, delai, temps, scoreVictoire;
    private TypeJeu typeJeu;


    public InfoServeur(String adresse, String nomPartie, String createur, int maxJoueurs,
	    int joueurs, int etat, int delai, int temps, TypeJeu typeJeu, int scoreVictoire) {
	this.adresse = adresse;
	this.nomPartie = nomPartie;
	this.createur = createur;
	this.maxJoueurs = maxJoueurs;
	this.joueurs = joueurs;
	this.etat = etat;
	this.delai = delai;
	this.temps = temps;
	this.typeJeu = typeJeu;
	this.scoreVictoire = scoreVictoire;
	delaiIni = delai;
	tempsIni = temps;
    }

    public InfoServeur(String createur) throws UnknownHostException {
	this(InetAddress.getLocalHost().getHostAddress(), "Nouvelle partie", createur, 8, 0, ETAT_OFF, 0, 120, TypeJeu.DEATHMATCH_EN_EQUIPE, 5);
    }

    public InfoServeur(IO io) {
	this(io.nextShortString(),
		io.nextShortString(),
		io.nextShortString(),
		io.nextPositif(),
		io.nextPositif(),
		io.nextPositif(),
		io.nextPositif(),
		io.nextShortInt(),
		TypeJeu.get(io.nextPositif()),
		io.nextPositif()
		);
    }

    public InetAddress getAdresse() throws UnknownHostException {
	return InetAddress.getByName(adresse);
    }

    public TypeJeu getTypeJeu() {
	return typeJeu;
    }

    public void setTypeJeu(TypeJeu typeJeu) {
	this.typeJeu = typeJeu;
    }

    public void setTemps(int temps) {
	this.temps = temps;
    }

    public int getTemps() {
	return temps;
    }

    public void setDelai(int delai) {
	this.delai = delai;
    }

    public int getDelai() {
	return delai;
    }

    public int getEtat() {
	return etat;
    }

    public void setEtat(int etat) {
	this.etat = etat;
    }

    public void setNomPartie(String nomPartie) {
	this.nomPartie = nomPartie;
    }

    public void setCreateur(String createur) {
	this.createur = createur;
    }

    public void setMaxJoueurs(int maxJoueurs) {
	this.maxJoueurs = maxJoueurs;
    }

    public void setJoueurs(int joueurs) {
	this.joueurs = joueurs;
    }

    public void setScoreVictoire(int scoreVictoire) {
	this.scoreVictoire = scoreVictoire;
    }

    public String getNomAdresse() {
	return adresse;
    }

    public String getNomPartie() {
	return nomPartie;
    }

    public String getCreateur() {
	return createur;
    }

    public int getMaxJoueurs() {
	return maxJoueurs;
    }

    public int getJoueurs() {
	return joueurs;
    }

    public int getScoreVictoire() {
	return scoreVictoire;
    }

    public boolean peutRejoindre() {
	return joueurs < maxJoueurs;
    }

    public int getDelaiInitial() {
	return delaiIni;
    }

    public int getTempsInitial() {
	return tempsIni;
    }

    @Override
    public IO sauvegarder(IO io) {
	return io.addShort(adresse)
		.addShort(nomPartie)
		.addShort(createur)
		.addBytePositif(maxJoueurs)
		.addBytePositif(joueurs)
		.addBytePositif(etat)
		.addBytePositif(delai)
		.addShort(temps)
		.addBytePositif(typeJeu.getID())
		.addBytePositif(scoreVictoire);
    }

    @Override
    public JComponent creerVue() {
	JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 0));
	p.setMinimumSize(new Dimension(0, 40));
	p.add(Outil.getTexte(nomPartie, true));
	p.add(Outil.getTexte(createur, true, new Color(0, 100, 0)));
	p.add(Outil.getTexte(joueurs + "/" + maxJoueurs + " joueurs", false, new Color(0, 100, 0)));
	p.add(Outil.getTexte(temps/60 + "min", false));
	switch(etat) {
	case ETAT_OFF:
	    p.add(Outil.getTexte("(Hors ligne)", false));
	    break;
	case ETAT_ATTENTE:
	    p.add(Outil.getTexte("(Commence dans " + delai + "s)", false));
	    break;
	case ETAT_JEU:
	    p.add(Outil.getTexte("En jeu", false));
	    break;
	default: throw new IllegalAccessError("Etat " + etat + " inconnu");
	}
	return p;
    }

    @Override
    public void action(Horloge horloge) {
	if(etat == ETAT_ATTENTE)
	    delai = horloge.getTemps();
	else temps = horloge.getTemps();
    }

}
