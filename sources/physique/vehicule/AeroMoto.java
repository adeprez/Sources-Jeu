package physique.vehicule;

import io.IO;

import java.awt.Graphics2D;

import physique.forme.Rect;
import ressources.sprites.vehicule.AnimationAeroMoto;
import vision.Camera;
import vision.Orientation;


public class AeroMoto extends VehiculeVolant {


    public AeroMoto(boolean droite) {
	super(0, new Rect(UNITE.width, UNITE.height, droite ? Orientation.DROITE : Orientation.GAUCHE), new AnimationAeroMoto());
    }

    public AeroMoto(IO io) {
	super(0, io, new AnimationAeroMoto());
    }

    @Override
    public void dessiner(Camera c, Graphics2D gPredessin, Graphics2D gDessin, Graphics2D gSurdessin) {
	super.dessiner(c, gPredessin, gDessin, gSurdessin);
	AnimationAeroMoto anim = (AnimationAeroMoto) getAnimation();
	anim.incrAngleRoue(1);
    }

    @Override
    public TypeVehicule getType() {
	return TypeVehicule.AEROMOTO;
    }

    @Override
    public int getVitalite() {
	return 50;
    }

    @Override
    public int getVitesse() {
	return 300;
    }

}
