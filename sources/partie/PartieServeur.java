package partie;

import io.IO;

import java.util.List;
import java.util.Map;

import map.elements.Spawn;
import map.objets.Objet;
import partie.modeJeu.Jeu;
import partie.modeJeu.scorable.Scorable;
import perso.Perso;
import reseau.objets.InfoServeur;
import reseau.paquets.Paquet;
import reseau.paquets.TypePaquet;
import reseau.paquets.jeu.PaquetSpawn;
import reseau.ressources.RessourcePerso;
import reseau.serveur.Serveur;
import divers.Outil;

public class PartieServeur extends Partie {
    private final Serveur serveur;
    private final Jeu jeu;
    private Map<Integer, List<Spawn>> spawns;


    public PartieServeur(Serveur serveur) {
	super(serveur.getRessources());
	jeu = serveur.getRessources().getJeu();
	this.serveur = serveur;
    }

    public void spawn(int id) {
	spawn(serveur.getRessources().getPerso(id));
    }

    public void spawn(RessourcePerso rp) {
	if(rp != null) {
	    Perso p = rp.getPerso();
	    p.setVie(p.getVitalite());
	    int tentatives = 0;
	    while(tentatives < 10) try {
		p.setPos(getSpawn(p.getEquipe()));
		serveur.envoyerTous(new PaquetSpawn(rp));
		PaquetSpawn.effet(p);
		tentatives = 10;
	    } catch(Exception e) {
		tentatives++;
		if(tentatives > 10)
		    System.err.println("Impossible de placer " + p);
	    }
	}
    }

    public Spawn getSpawn(int equipe) {
	if(spawns == null || !spawns.containsKey(equipe))
	    spawns = Spawn.creerSpawns(getMap(), 3, getRessources().getIDEquipes());
	List<Spawn> l = spawns.get(equipe);
	return l.get(Outil.r().nextInt(l.size()));
    }

    public void finPartie() {
	finPartie(jeu.enEquipe(), jeu.getIDGagnant());
    }

    @Override
    public void finPartie(boolean equipe, int gagnant) {
	super.finPartie(equipe, gagnant);
	serveur.getInfosServeur().setEtat(InfoServeur.ETAT_FINI);
	serveur.envoyerTous(new Paquet(TypePaquet.ETAT_PARTIE, serveur.getInfosServeur().getEtat()).add(equipe)
		.addBytePositif(gagnant == -1 ? EGALITE : gagnant));
    }

    @Override
    public void addScorable(int id, Scorable scorable) {
	super.addScorable(id, scorable);
	serveur.envoyerTous(new Paquet(TypePaquet.SCORABLE, scorable.sauvegarder(new IO().addBytePositif(id))));
	int gagnant = jeu.getIDGagnant();
	if(gagnant != -1)
	    finPartie(jeu.enEquipe(), gagnant);
    }

    @Override
    public void add(RessourcePerso r) {
	Perso p = r.getPerso();
	p.setEquipe(jeu.nextIDEquipe(r));
	p.setServeur(serveur, r.getID());
	p.setVivantListener(jeu);
	super.add(r);
    }

    @Override
    public boolean lancer() {
	if(super.lancer()) {
	    serveur.getInfosServeur().setEtat(InfoServeur.ETAT_JEU);
	    serveur.envoyerTous(new Paquet(TypePaquet.ETAT_PARTIE, serveur.getInfosServeur().getEtat(), jeu.getType().getID()));
	    serveur.envoyerTous(new Paquet(TypePaquet.TEMPS, new IO().addShort(serveur.getInfosServeur().getTemps())));
	    for(final List<Objet> lo : getMap().getObjets())
		for(final Objet o : lo)
		    o.setServeur(serveur);
	    return true;
	}
	return false;
    }

    @Override
    public boolean estServeur() {
	return true;
    }

}
