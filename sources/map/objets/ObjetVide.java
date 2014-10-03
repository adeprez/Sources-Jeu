package map.objets;

import interfaces.ContaineurImagesOp;
import io.IO;

import java.awt.Color;
import java.awt.Container;

import map.Map;
import physique.Collision;
import physique.Physique;
import physique.forme.FormeVide;

public class ObjetVide extends Objet {


    public ObjetVide(Map map, ContaineurImagesOp images, int fond) {
	super(map, images, -1, fond, new FormeVide());
	setVisible(aFond());
    }

    public ObjetVide(Map map, ContaineurImagesOp images, IO io) {
	this(map, images, io.nextPositif());
    }

    @Override
    public boolean doitTesterCollisionPersos() {
	return false;
    }

    @Override
    public Collision getCollision(Physique p) {
	return null;
    }

    @Override
    public boolean intersection(Physique p) {
	return false;
    }

    @Override
    public Color getCouleur() {
	return new Color(0, 0, 0, 0);
    }

    @Override
    public TypeObjet getType() {
	return TypeObjet.VIDE;
    }

    @Override
    public void construireInterface(Container c, boolean editable) {}

    @Override
    public Objet dupliquer() {
	return new ObjetVide(getMap(), getContaineurImages(), getFond());
    }

    @Override
    public boolean estVide() {
	return true;
    }

    @Override
    public String getNom() {
	return "Vide";
    }

    @Override
    public IO sauvegarder(IO io) {
	return io.addBytePositif(getType().getID()).addBytePositif(getFond());
    }

    @Override
    public int getVitalite() {
	return 1;
    }

}
