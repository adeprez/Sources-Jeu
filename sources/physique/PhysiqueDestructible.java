package physique;

import interfaces.IOable;
import interfaces.LocaliseEquipe;
import listeners.DestructibleListener;
import partie.modeJeu.Jeu;
import perso.Vivant;
import physique.forme.Forme;
import physique.vehicule.Vehicule;
import divers.Outil;
import exceptions.HorsLimiteException;

public abstract class PhysiqueDestructible extends Physique implements LocaliseEquipe {
    private static final int DELAI_INSENSIBLE_RESPAWN = Jeu.DELAI_RESPAWN + 1000;
    private DestructibleListener vivant;
    private long derniereMort;
    private int equipe, vie;
    private Vivant tueur;


    public PhysiqueDestructible(Forme forme) {
	super(forme);
    }

    public abstract int getVitalite();
    public abstract IOable getPaquetVie();
    public abstract boolean doitTesterCollisionPersos();

    public void setVivantListener(DestructibleListener l) {
	vivant = l;
    }

    public int getVie() {
	return vie;
    }

    public void setVie(int nouvelle) {
	int tmp = vie;
	vie = Outil.entre(nouvelle, 0, getVitalite());
	if(tmp != vie) {
	    if(estServeur())
		envoyerClients(getPaquetVie());
	    if(vivant != null)
		vivant.vieChange(this);
	    if(vie == 0)
		meurt();
	}
    }

    public void meurt() {
	clear();
	derniereMort = System.currentTimeMillis();
	if(vivant != null)
	    vivant.meurt(this, tueur);
    }

    public int getTempsDerniereMort() {
	return derniereMort == 0 ? Integer.MAX_VALUE : (int) (System.currentTimeMillis() - derniereMort);
    }

    public boolean estVivant() {
	return vie > 0;
    }

    public void degats(int dmg, Vivant tueur) {
	if(estServeur() && dmg > 0 && getTempsDerniereMort() > DELAI_INSENSIBLE_RESPAWN) {
	    this.tueur = tueur;
	    setVie(vie - dmg);
	}
    }

    public void soin(int soin) {
	if(estServeur() && soin > 0)
	    setVie(vie + soin);
    }

    public void setEquipe(int equipe) {
	this.equipe = equipe;
    }

    public boolean memeEquipe(PhysiqueDestructible p) {
	return p.equipe == equipe;
    }

    public int getPourcentVie() {
	return Outil.getPourcentage(vie, getVitalite());
    }

    @Override
    public int getEquipe() {
	return equipe;
    }

    @Override
    public synchronized Collision setPos(int x, int y) throws HorsLimiteException {
	try {
	    return super.setPos(x, y);
	} catch(HorsLimiteException out) {
	    degats(getVie(), null);
	    throw out;
	}
    }

    @Override
    public Collision getCollision() {
	Collision c = super.getCollision();
	if(c != null)
	    return c;
	if(doitTesterCollisionPersos()) {
	    for(final Vivant p : getMap().getEnnemis(getEquipe(), false))
		if((c = getCollision(p)) != null)
		    return c;
	    for(final Vehicule v : getMap().getVehicules())
		if((c = getCollision(v)) != null)
		    return c;
	}
	return null;
    }

}
