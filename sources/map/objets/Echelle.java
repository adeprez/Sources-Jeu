package map.objets;

import interfaces.ContaineurImageOp;
import interfaces.ContaineurImagesOp;
import io.IO;

import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import map.Map;
import physique.forme.Forme;
import vision.Camera;
import vision.Extrusion3D;

public class Echelle extends Destructible {
    private final boolean decoupe;


    public Echelle(Map map, ContaineurImagesOp images, int fond, Forme forme, int id, int resistance, int degats, boolean decoupe) {
	super(map, images, fond, forme, id, resistance, degats);
	this.decoupe = decoupe;
    }

    public Echelle(Map map, ContaineurImagesOp img, IO io) {
	this(map, img, io.nextPositif(), Forme.get(io), io.nextPositif(), io.nextPositif(), io.nextPositif(), io.nextBoolean());
    }

    @Override
    public void dessine3D(Graphics2D g1, Graphics2D g2, Graphics2D g3, Camera c) {
	if(estVisible() && aFond()) {
	    int equipe = c.getSource().getEquipe();
	    Forme f = getForme();
	    Rectangle arr, av, m1, m2;
	    ContaineurImageOp img = getContaineurImageFond();
	    m1 = c.getZone(this, PLAN_ARR_AV);
	    m2 = c.getZone(this, PLAN_AV_ARR);
	    if(decoupe) {
		arr = c.getZone(this, PLAN_ARR_ARR);
		av = c.getZone(this, PLAN_AV_AV);
		Extrusion3D.dessine(g1, img, null, f, m1, arr);
		predessiner(g1, m1, equipe);
	    } else {
		arr = c.getZoneFond(this, PLAN_ARR_ARR);
		av = c.getZoneFond(this, PLAN_AV_AV);
		Rectangle r = c.getZoneFond(this, PLAN_ARR_AV);
		Extrusion3D.dessine(g1, img, getImageDegats(), f, r, arr);
		predessiner(g1, r, equipe);
	    }
	    Extrusion3D.dessine(g2, getContaineurImage(), getImageDegats(), getForme(), m2, m1);
	    dessiner(g2, m2, equipe);
	    Composite tmp1 = g2.getComposite(), tmp2 = g3.getComposite();
	    if(!decoupe) {
		opaciteSurdessin(g2);
		opaciteSurdessin(g3);
	    }
	    Extrusion3D.dessine(g2, img, getImageDegats(), f, av, m2);
	    surdessiner(g3, av);
	    g2.setComposite(tmp1);
	    g3.setComposite(tmp2);
	}
    }

    @Override
    public IO sauvegarder(IO io) {
	return super.sauvegarder(io).add(decoupe);
    }

    @Override
    public String getNom() {
	return "Echelle";
    }

    @Override
    public TypeObjet getType() {
	return TypeObjet.ECHELLE;
    }

    @Override
    public Objet dupliquer() {
	return new Echelle(getMap(), getContaineurImages(), getFond(), getForme().dupliquer(), getID(), getVitalite(), getVie(), decoupe);
    }

}
