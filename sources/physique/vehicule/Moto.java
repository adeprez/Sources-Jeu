package physique.vehicule;

import io.IO;
import perso.Vivant;
import physique.forme.Rect;
import ressources.sprites.animation.Animation;
import ressources.sprites.animation.perso.AnimationPerso;
import ressources.sprites.vehicule.AnimationMoto;
import vision.Orientation;
import exceptions.HorsLimiteException;

public class Moto extends Vehicule {
    private int angle, deplacement;


    public Moto(boolean droite) {
	super(0, new Rect((int) (UNITE.width * 1.5), UNITE.height, droite ? Orientation.DROITE : Orientation.GAUCHE), new AnimationMoto());
    }

    public Moto(IO io) {
	super(0, io, new AnimationMoto());
    }

    @Override
    public int getGraviteMax() {
	return 15;
    }

    @Override
    public boolean faireAction() {
	if(super.faireAction()) {
	    Vivant v = get(CONDUCTEUR);
	    if(v != null) try {
		if(v.estServeur() && v.setPos(this) != null)
		    retire(v);
		Animation a = v.getAnimation();
		if(a instanceof AnimationPerso) {
		    ((AnimationPerso) a).getCorpsPerso().setAngle(-angle/5f);
		    a.setDecalageY(angle * -40);
		    a.setDecalageX(angle * -15);
		}
		getAnimation().setDecalageX(angle);
		getAnimation().setDecalageY(angle * -10);
		if(getVitesseInstantanee() > 0) {
		    AnimationMoto anim = (AnimationMoto) getAnimation();
		    anim.incrAngleRoues(getVitesseInstantanee()/3000f);
		    anim.getCorps().setAngle(angle/-5f);
		}
	    } catch (HorsLimiteException e) {
		if(estServeur())
		    retire(v);
	    }
	    return true;
	}
	return false;
    }

    @Override
    public int getVitalite() {
	return 50;
    }

    @Override
    public int getVitesse() {
	return 1000;
    }

    @Override
    public IO savePos(IO io) {
	return io.addShort(getX() + UNITE.width/2).addShort(getY());
    }

    @Override
    public TypeVehicule getType() {
	return TypeVehicule.MOTO;
    }

    @Override
    public void deplacementFluide() {
	super.deplacementFluide();
	deplacement++;
	if(angle > 0 && deplacement > 10)
	    angle--;
    }

    @Override
    public void escalade(int hauteur) {
	super.escalade(hauteur);
	deplacement = 0;
	if(angle < hauteur)
	    angle = Math.min(3, angle + 1);
    }

}
