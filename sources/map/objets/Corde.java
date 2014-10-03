package map.objets;

import interfaces.ContaineurImageOp;
import interfaces.ContaineurImagesOp;
import io.IO;

import java.awt.Composite;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import map.Map;
import physique.forme.Forme;
import vision.Camera;
import vision.Extrusion3D;

public class Corde extends Objet {


    public Corde(Map map, ContaineurImagesOp images, int fond, Forme forme, int id) {
	super(map, images, id, fond, forme);
    }

    public Corde(Map map, ContaineurImagesOp img, IO io) {
	this(map, img, io.nextPositif(), Forme.get(io), io.nextPositif());
    }

    @Override
    public void dessine3D(Graphics2D g1, Graphics2D g2, Graphics2D g3, Camera c) {
	if(estVisible()) {
	    int equipe = c.getSource().getEquipe();
	    Forme f = getForme();
	    if(aFond()) {
		Rectangle arr, av;
		ContaineurImageOp img;
		arr = c.getZoneFond(this, PLAN_ARR_ARR);
		av = c.getZoneFond(this, PLAN_AV_AV);
		img = getContaineurImageFond();
		Rectangle r = c.getZoneFond(this, PLAN_ARR_AV);
		Extrusion3D.dessine(g1, img, getImageDegats(), f, r, arr);
		predessiner(g1, r, equipe);
		Composite tmp1 = g2.getComposite(), tmp2 = g3.getComposite();
		opaciteSurdessin(g2);
		opaciteSurdessin(g3);
		Extrusion3D.dessine(g2, img, getImageDegats(), f, av, c.getZoneFond(this, PLAN_AV_ARR));
		surdessiner(g3, av);
		g2.setComposite(tmp1);
		g3.setComposite(tmp2);
	    }
	    dessiner(g2, c.getZone(this, PLAN_MILIEU), equipe);
	}
    }

    @Override
    public IO sauvegarder(IO io) {
	return super.sauvegarder(io).addBytePositif(getID());
    }

    @Override
    public String getNom() {
	return "Corde";
    }

    @Override
    public TypeObjet getType() {
	return TypeObjet.CORDE;
    }

    @Override
    public boolean estVide() {
	return false;
    }

    @Override
    public Objet dupliquer() {
	return new Corde(getMap(), getContaineurImages(), getFond(), getForme().dupliquer(), getID());
    }

    @Override
    public void construireInterface(Container c, boolean editable) {
    }

    @Override
    public int getVitalite() {
	return 0;
    }

}
