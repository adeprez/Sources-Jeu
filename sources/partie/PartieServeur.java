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
import reseau.ressources.RessourceReseau;
import reseau.ressources.TypeRessource;
import reseau.serveur.Serveur;
import divers.Outil;

public class PartieServeur extends Partie {
    private static final int TENTATIVES_SPAWN = 25;
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
	if(estLancee() && rp != null) {
	    Perso p = rp.getPerso();
	    int tentatives = 0;
	    while(tentatives < TENTATIVES_SPAWN && !trySpawn(rp))
		tentatives++;
	    if(tentatives == TENTATIVES_SPAWN)
		System.err.println("Impossible de placer " + p);
	    p.setVie(p.getVitalite());
	}
    }

    public boolean trySpawn(RessourcePerso rp) {
	try {
	    Perso p = rp.getPerso();
	    if(p.setPos(getSpawn(p.getEquipe())) == null) {
		serveur.envoyerTous(new PaquetSpawn(rp));
		PaquetSpawn.effet(p);
		return true;
	    }
	} catch(Exception e) {}
	return false;
    }

    public Spawn getSpawn(int equipe) {
	if(spawns == null || !spawns.containsKey(equipe))
	    spawns = Spawn.creerSpawns(getMap(), 3, getRessources().getIDEquipes());
	List<Spawn> l = spawns.get(equipe);
	return l.get(Outil.r().nextInt(l.size()));
    }

    public void finPartie(boolean finTemps) {
	finPartie(jeu.enEquipe(), jeu.getIDGagnant(finTemps));
    }

    public Map<Integer, List<Spawn>> getSpawns() {
	return spawns;
    }

    @Override
    public void finPartie(boolean equipe, int gagnant) {
	super.finPartie(equipe, gagnant);
	serveur.envoyerTous(new Paquet(TypePaquet.ETAT_PARTIE).addBytePositif(InfoServeur.ETAT_FINI).add(equipe)
		.addBytePositif(gagnant == -1 ? EGALITE : gagnant));
	serveur.deconnecterClients();
	serveur.fermer();
	serveur.getRessources().removeAll();
    }

    @Override
    public void addScorable(int id, Scorable scorable) {
	super.addScorable(id, scorable);
	serveur.envoyerTous(new Paquet(TypePaquet.SCORABLE, scorable.sauvegarder(new IO().addBytePositif(id))));
	int gagnant = jeu.getIDGagnant(false);
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
	    for(final RessourceReseau<?> rp : jeu.getRessources().get(TypeRessource.PERSO).values())
		spawn((RessourcePerso) rp);
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
