package jeu;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import layouts.LayoutLignes;
import listeners.AjoutPersoListener;
import listeners.ChangeScoreListener;
import listeners.RemovePersoListener;
import partie.Partie;
import partie.modeJeu.scorable.Scorable;
import perso.Perso;
import base.Ecran;
import controles.ControleListener;

public class EcranScores extends Ecran implements ChangeScoreListener, AjoutPersoListener, RemovePersoListener, ControleListener {
    private static final long serialVersionUID = 1L;
    private final Map<Integer, PanelScore> scores;
    private final Partie partie;


    public EcranScores(Partie partie) {
	setLayout(new LayoutLignes());
	setName("Scores");
	this.partie = partie;
	scores = new HashMap<Integer, PanelScore>();
	partie.addAjoutPersoListener(this);
	partie.addRemovePersoListener(this);
	partie.addChangeScoreListener(this);
    }

    @Override
    public void changeScore(int id, int score, Scorable nouveau) {
	scores.get(id).setScore(score);
    }

    @Override
    public void remove(int id, Perso perso) {
	remove(scores.remove(id));
    }

    @Override
    public void ajout(Perso perso) {
	PanelScore p = new PanelScore(perso);
	scores.put(partie.getRessources().getIDPerso(perso), p);
	add(p);
    }

    @Override
    public void appuie(KeyEvent e) {
	if(e.getKeyCode() == KeyEvent.VK_CONTROL)
	    setVisible(!isVisible());
    }

    @Override
    public void relache(KeyEvent e) {
    }

}
