package physique.vehicule;

import io.IO;
import perso.Vivant;
import physique.forme.Forme;
import ressources.sprites.animation.Animation;
import exceptions.HorsLimiteException;

public abstract class VehiculeVolant extends Vehicule {


    public VehiculeVolant(int extraPlaces, Forme forme, Animation anim) {
	super(extraPlaces, forme, anim);
    }

    public VehiculeVolant(int extraPlaces, IO io, Animation anim) {
	super(extraPlaces, io, anim);
    }

    @Override
    public boolean faireAction() {
	if(super.faireAction()) {
	    Vivant v = get(CONDUCTEUR);
	    if(v != null) try {
		if(v.estServeur() && v.setPos(this) != null)
		    retire(v);
	    } catch (HorsLimiteException e) {
		if(estServeur())
		    retire(v);
	    }
	    return true;
	}
	return false;
    }

    @Override
    public int getGraviteMax() {
	return -getVitesseInstantanee()/50 + 3;
    }

}
