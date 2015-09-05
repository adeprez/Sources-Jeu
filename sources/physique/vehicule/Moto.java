package physique.vehicule;

import io.IO;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import perso.Vivant;
import physique.forme.Rect;
import ressources.Images;
import vision.Orientation;
import exceptions.HorsLimiteException;

public class Moto extends Vehicule {
    private final BufferedImage roue, moto;
    private float angleRoues;


    public Moto(boolean droite) {
	super(0, new Rect((int) (UNITE.width * 1.5), UNITE.height, droite ? Orientation.DROITE : Orientation.GAUCHE));
	roue = Images.get("armes/bouclier.png");
	moto = Images.get("armes/carquois.png", true);
    }

    public Moto(IO io) {
	super(0, io);
	roue = Images.get("armes/bouclier.png", true);
	moto = Images.get("armes/epee.png", true);
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
		if(v.setPos(this) != null && v.estServeur())
		    retire(v);
		angleRoues += getVitesseInstantanee()/1000f;
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
	return 10;
    }

    @Override
    public int getVitesse() {
	return 1000;
    }

    @Override
    public void setPosition(Vivant source) throws HorsLimiteException {
	source.setPos(source);
    }

    @Override
    public TypeVehicule getType() {
	return TypeVehicule.MOTO;
    }

    @Override
    public void dessiner(Graphics2D g, Rectangle zone, int equipe) {
	super.dessiner(g, zone, equipe);
	int taille = zone.width/3;
	AffineTransform t = AffineTransform.getTranslateInstance(zone.x, zone.y + zone.height - taille);
	double scale = (double) taille/roue.getWidth();
	t.scale(scale, scale);
	if(angleRoues != 0)
	    t.rotate(estDroite() ? angleRoues : -angleRoues, roue.getWidth()/2, roue.getHeight()/2);
	g.drawImage(roue, t, null);
	t = AffineTransform.getTranslateInstance(zone.x + zone.width - taille, zone.y + zone.height - taille);
	t.scale(scale, scale);
	if(angleRoues != 0)
	    t.rotate(estDroite() ? angleRoues : -angleRoues, roue.getWidth()/2, roue.getHeight()/2);
	g.drawImage(roue, t, null);
	g.drawImage(moto, zone.x + (estDroite() ? 0 : zone.width),
		zone.y + zone.height/10, estDroite() ? zone.width : -zone.width, (int) (zone.height/1.5), null);
    }

}
