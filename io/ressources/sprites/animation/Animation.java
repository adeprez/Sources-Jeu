package ressources.sprites.animation;

import interfaces.Localise;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import ressources.sprites.animation.sequence.EtapeSequence;
import ressources.sprites.animation.sequence.Sequence;
import divers.Couple;
import divers.Outil;
import exceptions.AnnulationException;

public abstract class Animation extends AbstractMembre {
    public static final int PAS_ETAPE = 1000;
    private int dX, dY, vitesse, cumulVitesse, etapePrecendente;
    private Sequence sequence;
    private boolean inverse;


    public Animation() {
	super(0, 0, 0, 0, 0);
	vitesse = PAS_ETAPE;
    }

    public abstract Membre[] getMembres();
    public abstract Animation dupliquer();
    public abstract Couple<Membre, Integer> getMembre(Localise source, Localise ancrable);

    public Sequence getSequence() {
	return sequence;
    }

    public int getNumEtape() {
	return cumulVitesse/PAS_ETAPE;
    }

    public EtapeSequence getEtape() {
	return sequence.getModele().getEtape(getNumEtape());
    }

    public int getVitesse() {
	return vitesse;
    }

    public void terminer() {
	if(sequence != null)
	    setSequence(sequence.getSuivante());
    }

    public void setSequence(Sequence sequence) {
	etapePrecendente = -1;
	cumulVitesse = 0;
	this.sequence = sequence;
	if(sequence == null)
	    toOrigine();
    }

    public void setVitesse(int vitesse) {
	this.vitesse = Math.max(1, vitesse);
    }

    public int getRang(Membre membre) throws AnnulationException {
	Membre[] membres = getMembres();
	for(int i=0 ; i<membres.length ; i++)
	    if(membres[i] == membre)
		return i;
	throw new AnnulationException("Cette animation ne contient pas le membre " + membre);
    }

    public void bouge() {
	if(sequence != null)
	    synchronized(sequence) {
		EtapeSequence e1 = getEtape();
		EtapeSequence e2 = getEtapeSuivante();
		if(e2 != null) {
		    if(etapePrecendente != getNumEtape())
			e1.effetPuisFutur(sequence.getDetermineurAngle(), this, PAS_ETAPE/Math.max(1, vitesse), e2);
		    else versAngle();
		    int prct = cumulVitesse % PAS_ETAPE;
		    dX = (e1.getdX() * (PAS_ETAPE - prct) + e2.getdX() * prct)/(PAS_ETAPE << 1);
		    dY = (e1.getdY() * (PAS_ETAPE - prct) + e2.getdY() * prct)/(PAS_ETAPE << 1);
		    etapePrecendente = getNumEtape();
		    cumulVitesse = (cumulVitesse + vitesse) % (sequence.getModele().getNombreEtapes() * PAS_ETAPE);
		} else {
		    e1.effet(sequence.getDetermineurAngle(), this);
		    terminer();
		}
	    }
	else {
	    if(dX != 0)
		dX /= 2;
	    if(dY != 0)
		dY /= 2;
	    versAngle();
	}
    }

    public EtapeSequence getEtapeSuivante() {
	int suivant = getSuivant();
	return suivant == -1 ? null : sequence.getModele().getEtape(suivant);
    }

    public int getSuivant() {
	int n = getNumEtape() + 1;
	if(sequence == null || n >= sequence.getModele().getNombreEtapes() && !sequence.boucle())
	    return -1;
	return n % sequence.getModele().getNombreEtapes();
    }

    public void versAngle() {
	for(final Membre m : getMembres())
	    m.versAngle();
    }

    public void toOrigine() {
	for(final Membre m : getMembres())
	    m.setAngleFutur(0);
    }

    public int getDecalageY() {
	return dY;
    }

    public void setDecalageY(int dY) {
	this.dY = dY;
    }

    public int getDecalageX() {
	return dX;
    }

    public void setDecalageX(int dX) {
	this.dX = dX;
    }

    public void setInverse(boolean inverse) {
	this.inverse = inverse;
    }

    @Override
    public float getAngle(boolean droite) {
	return 0;
    }

    @Override
    public int getDecalage(AbstractMembre enfant, Rectangle zone, double angle) {
	return 0;
    }

    @Override
    public int getDecalageY(AbstractMembre enfant, Rectangle zone) {
	return 0;
    }

    @Override
    public int getX(Rectangle zone, boolean droite) {
	return zone.x + zone.width/2 + Outil.getValeur(droite ? dX : -dX, zone.width);
    }

    @Override
    public int getY(Rectangle zone) {
	return zone.y + zone.height/2 - Outil.getValeur(dY, zone.width);
    }

    @Override
    public void dessiner(Graphics2D g, Rectangle zone, boolean droite) {
	if(inverse)
	    droite = !droite;
	for(final Membre m : getMembres())
	    m.dessiner(g, zone, droite);
    }

}
