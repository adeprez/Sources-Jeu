package specialite.arme.projectile;

import java.awt.image.BufferedImage;

import map.objets.Objet;
import perso.Vivant;
import physique.Physique;
import physique.PhysiqueDestructible;
import ressources.sprites.animation.Membre;
import specialite.arme.Arme;
import specialite.attaches.MembreAttachable;
import specialite.effet.Effet;
import divers.Couple;
import divers.Outil;


public class Fleche extends Projectile {
    private final Effet<Vivant> effet;


    public Fleche(PhysiqueDestructible source, Effet<Vivant> effet) {
	super(getFleche(Outil.r().nextBoolean()), 60, (int) (UNITE.width * .70), UNITE.height/14, source.getEquipe());
	this.effet = effet;
	setServeur(source.getServeur());
    }

    public MembreFleche creerMembre(Membre parent, int x, int y, float angle) {
	return new MembreFleche(getImage(), parent, x, y, angle);
    }

    public static BufferedImage getFleche(boolean allie) {
	return Arme.getImage("fleche " + (allie ? "allie" : "ennemie"));
    }

    public MembreFleche addTo(Membre parent, int x, int y, float angle) {
	MembreFleche m = creerMembre(parent, x, y, angle);
	m.lancer();
	return m;
    }

    public void impact(Vivant v) {
	getMap().removeDessinable(this);
	Couple<Membre,Integer> c = v.getAnimation().getMembre(v, this);
	addTo(c.get1(), 80, c.get2(), (float) (estDroite() == v.estDroite() ? getAngle() : -getAngle() + Math.PI));
	effet.faireEffet(v);
    }

    @Override
    public void impact(Physique p) {
	if(p instanceof Vivant)
	    impact((Vivant) p);
	else if(p instanceof Objet)
	    ((Objet) p).addRemoveListener(this);
    }

    @Override
    public boolean doitTesterCollisionPersos() {
	return true;
    }


    public class MembreFleche extends MembreAttachable {


	public MembreFleche(BufferedImage image, Membre parent, int x, int y, float angle) {
	    super(parent, image, 150, 20, x, y, y);
	    setAngle(angle);
	}


    }

}
