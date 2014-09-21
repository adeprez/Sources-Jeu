package listeners;

import java.util.EventListener;

import partie.modeJeu.scorable.Scorable;


public interface ChangeScoreListener extends EventListener {
    public void changeScore(int id, int score, Scorable nouveau);
}
