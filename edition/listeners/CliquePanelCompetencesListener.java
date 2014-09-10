package listeners;

import java.util.EventListener;

import xp.PanelCompetence;
import xp.PanelCompetences;

public interface CliquePanelCompetencesListener extends EventListener {
	public void clique(PanelCompetences p1, PanelCompetence p2);
}
