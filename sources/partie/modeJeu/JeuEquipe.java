package partie.modeJeu;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import perso.Perso;
import reseau.ressources.RessourcePerso;
import reseau.ressources.RessourcesReseau;
import reseau.ressources.RessourcesServeur;
import reseau.serveur.Serveur;
import divers.Outil;

public abstract class JeuEquipe extends Jeu {


    public JeuEquipe(Serveur serveur, int scoreVictoire) {
	super(serveur, scoreVictoire);
    }

    public abstract int getNombreEquipes();

    public int getNombre(int equipe) {
	List<Perso> e = getRessources().getEquipes().get(equipe);
	return e == null ? 0 : e.size();
    }

    @Override
    public int getIDGagnant(boolean max) {
	int id = -1, score = Integer.MIN_VALUE;
	for(final Entry<Integer, Integer> i : getServeur().getPartie().getScoreEquipes().entrySet()) {
	    if(max)
		if(i.getValue() == score)
		    id = -1;
		else if(i.getValue() > score) {
		    score = i.getValue();
		    id = i.getKey();
		}
	    if(i.getValue() >= getScoreVictoire())
		return i.getKey();
	}
	return id;
    }

    @Override
    public int nextIDEquipe(RessourcePerso r) {
	Map<Integer, List<Perso>> equipes = getRessources().getEquipes();
	int equipe = 1, n = getNombreEquipes();
	if(equipes.size() <= n)
	    while(equipes.containsKey(equipe))
		equipe++;
	else {
	    int nn = Integer.MAX_VALUE;
	    for(final Entry<Integer, List<Perso>> e : equipes.entrySet())
		if(e.getKey() > 0 && e.getValue().size() < nn) {
		    equipe = e.getKey();
		    nn = e.getValue().size();
		}
	}
	return equipe;
    }

    @Override
    public List<Component> getComposants(RessourcesServeur r) {
	return creerComposants(r);
    }

    public static List<Component> creerComposants(RessourcesReseau r) {
	Map<Integer, List<Perso>> persos = r.getEquipes();
	List<Component> l = new ArrayList<Component>();
	for(final Entry<Integer, List<Perso>> equipe : persos.entrySet()) {
	    JPanel pl = new JPanel();
	    pl.setLayout(new BoxLayout(pl, BoxLayout.Y_AXIS));
	    pl.setOpaque(false);
	    pl.setBorder(Outil.getBordure("Equipe " + equipe.getKey()));
	    for(final Perso p : equipe.getValue())
		pl.add(getInterface(p));
	    l.add(pl);
	}
	return l;
    }

}
