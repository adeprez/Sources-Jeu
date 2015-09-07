package specialite.arme.projectile;

import map.elements.AnimationDessinable;
import map.elements.DessinableTemporaire;
import map.elements.Localisation;
import perso.Vivant;
import physique.Physique;
import physique.PhysiqueDestructible;
import ressources.Images;
import ressources.sprites.Sprites;
import specialite.effet.Effet;


public class Boulet extends Projectile {
    private final Effet<Vivant> effet;


    public Boulet(PhysiqueDestructible source, Effet<Vivant> effet) {
	super(Images.get("armes/bouclier.png"), 25, UNITE.width/3, UNITE.height/3, source.getEquipe());
	this.effet = effet;
	setServeur(source.getServeur());
    }

    public void explose() {
	getMap().ajoutDessinable(new AnimationDessinable(new Localisation(this, UNITE.width * 3, UNITE.height * 3, 0, UNITE.height),
		Sprites.getSprite("explosion", true), 1000, (DessinableTemporaire a) -> getMap().removeDessinable(a)));
    }

    @Override
    public void impact(Physique p) {
	if(p instanceof PhysiqueDestructible)
	    ((PhysiqueDestructible) p).degats(25, null);
	else if(p instanceof Vivant)
	    effet.faireEffet((Vivant) p);
	explose();
	getMap().removeDessinable(this);
    }

    @Override
    public boolean doitTesterCollisionPersos() {
	return true;
    }

    @Override
    public int getMasse() {
	return 50;
    }

    @Override
    public double getCoefReductionForceX() {
	return 1.005;
    }

    @Override
    public double getCoefReductionForceY() {
	return 1.001;
    }


}
