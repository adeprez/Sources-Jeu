package specialite.arme.attaque;

import perso.AbstractPerso;
import perso.Vivant;
import physique.actions.Action;
import ressources.sprites.animation.AnimationPerso;
import ressources.sprites.animation.sequence.DetermineurAngle;
import specialite.arme.projectile.Projectile;
import specialite.effet.Effet;
import controles.TypeAction;

public abstract class ActionTir extends Action<AbstractPerso> implements DetermineurAngle, Effet<Vivant> {
    private final int tourTir, finAction, degats;
    private boolean aTire, finie;
    private float angle, angleTete;


    public ActionTir(AbstractPerso source, int tourTir, int tempsRepos) {
	super(source);
	this.tourTir = tourTir;
	finAction = tourTir + tempsRepos;
	degats = source.getSpecialite().getArme().getDegats();
    }

    public abstract Projectile getProjectile();

    public void tirer() {
	aTire = true;
	Projectile p = getProjectile();
	Vivant v = getSource();
	p.getForme().setPos(v.getXCentre(), v.getY() + 5 * v.getHauteur()/6);
	p.setDroite(v.estDroite());
	p.setAngle(angleTete);
	v.getMap().ajout(p);
    }

    @Override
    public float getAngle(int id, float[] angles) {
	return id == AnimationPerso.TETE ? angleTete :
	    id == AnimationPerso.BRAS_AV && getSource().getAnimation().getNumEtape() > 0 ? angle : angles[id];
    }

    @Override
    public TypeAction getType() {
	return TypeAction.ATTAQUER;
    }

    @Override
    public void tourAction() {
	if(!aTire && getTour() >= tourTir)
	    tirer();
	if(getTour() >= finAction) {
	    finie = true;
	    stopAction();
	}
    }

    @Override
    public void commence() {
	aTire = false;
	finie = false;
	angleTete = getSource().getAngleRad();
	angle = (float) (angleTete - Math.PI/2);
    }

    @Override
    public void seTermine() {}

    @Override
    public boolean peutArret() {
	return finie;
    }

    @Override
    public void faireEffet(Vivant e) {
	e.degats(degats, getSource());
    }

    public static int getX(float angle, int d) {
	return (int) (Math.sin(angle) * d);
    }

    public static int getY(float angle, int d) {
	return (int) (Math.cos(angle) * d);
    }

}
