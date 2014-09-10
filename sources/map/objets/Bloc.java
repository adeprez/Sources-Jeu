package map.objets;

import interfaces.ContaineurImagesOp;
import io.IO;
import map.Map;
import physique.forme.Forme;

public class Bloc extends Destructible {


    public Bloc(Map map, ContaineurImagesOp images, int fond, Forme forme, int id, int resistance, int degats) {
	super(map, images, fond, forme, id, resistance, degats);
    }

    public Bloc(Map map, ContaineurImagesOp img, IO io) {
	this(map, img, io.nextPositif(), Forme.get(io), io.nextPositif(), io.nextPositif(), io.nextPositif());
    }

    @Override
    public TypeObjet getType() {
	return TypeObjet.BLOC;
    }

    @Override
    public Objet dupliquer() {
	return new Bloc(getMap(), getContaineurImages(), getFond(), getForme().dupliquer(), getID(), getVitalite(), getVie());
    }

    @Override
    public String getNom() {
	return "Bloc";
    }

}
