package specialite.arme.attaque;

import map.objets.Objet;
import perso.AbstractPerso;
import perso.Perso;
import perso.Vivant;
import physique.actions.Action;
import physique.forme.Rect;
import specialite.effet.Effet;
import vision.Orientation;
import controles.TypeAction;

public abstract class ActionAttaqueCac extends Action<AbstractPerso> implements Effet<Vivant> {
    private final int debutDegats, finDegats;
    private boolean finie, aTouche;
    private Effet<Vivant> effet;


    public ActionAttaqueCac(AbstractPerso source, int debutDegats, int finDegats) {
	super(source);
	this.debutDegats = debutDegats;
	this.finDegats = finDegats;
	effet = this;
    }

    public abstract int getLargeur();
    public abstract int getHauteur();

    public void setEffet(Effet<Vivant> effet) {
	this.effet = effet;
    }

    public Rect getFormeArme() {
	Vivant v = getSource();
	int w = getLargeur(), h = getHauteur();
	return new Rect(v.getXCentre() + (v.estDroite() ? 0 : -w), v.getYCentre() - h/2, w, h, Orientation.DROITE);
    }

    public void testeCollision() {
	Rect r = getFormeArme();
	Objet o = getSource().getMap().getCollisionMap(r);
	if(o == null) {
	    for(final Perso p : getSource().getMap().getEnnemis(getSource().getEquipe(), false))
		if(p.getForme().intersection(r)) {
		    effet.faireEffet(p);
		    aTouche = true;
		    break;
		}
	} else {
	    aTouche = true;
	    o.degats(1, getSource());
	}
    }

    @Override
    public void faireEffet(Vivant e) {
	e.degats(getSource().getSpecialite().getArme().getDegats(), getSource());
    }

    @Override
    public TypeAction getType() {
	return TypeAction.ATTAQUER;
    }

    @Override
    public void tourAction() {
	if(!aTouche && getTour() >= debutDegats && getTour() <= finDegats)
	    testeCollision();
	if(getTour() > finDegats) {
	    finie = true;
	    stopAction();
	}
    }

    @Override
    public void commence() {
	getSource().getAnimation().setVitesse(100);
	aTouche = false;
	finie = false;
    }

    @Override
    public void seTermine() {}

    @Override
    public boolean peutArret() {
	return finie;
    }

}
