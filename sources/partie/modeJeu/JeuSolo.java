package partie.modeJeu;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import layouts.LayoutLignes;
import map.Map;
import perso.Perso;
import reseau.ressources.RessourcePerso;
import reseau.ressources.RessourceReseau;
import reseau.ressources.RessourcesReseau;
import reseau.ressources.RessourcesServeur;
import reseau.ressources.TypeRessource;
import reseau.serveur.Serveur;

public abstract class JeuSolo extends Jeu {


    public JeuSolo(Serveur serveur, int scoreVictoire) {
	super(serveur, scoreVictoire);
    }

    @Override
    public void prepareMap(Map map) {
    }

    @Override
    public int nextIDEquipe(RessourcePerso r) {
	return r.getID() + 1;
    }

    @Override
    public List<Component> getComposants(RessourcesServeur r) {
	return creerComposants(r);
    }

    @Override
    public int getIDGagnant(boolean max) {
	int id = -1, score = Integer.MIN_VALUE;
	for(final Integer i : getRessources().get(TypeRessource.PERSO).keySet()) {
	    int s = getServeur().getPartie().getScore(i);
	    if(max)
		if(s == score)
		    id = -1;
		else if(s > score) {
		    score = s;
		    id = i;
		}
	    if(s >= getScoreVictoire())
		return i;
	}
	return id;
    }

    public static List<Component> creerComposants(RessourcesReseau r) {
	List<Component> l = new ArrayList<Component>();
	JPanel p = new JPanel(new LayoutLignes());
	p.setOpaque(false);
	for(final RessourceReseau<?> rr : r.get(TypeRessource.PERSO).values())
	    p.add(getInterface((Perso) rr.getRessource()));
	l.add(p);
	return l;
    }

}
