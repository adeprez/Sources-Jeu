package map.objets;

import interfaces.ContaineurImagesOp;
import io.IO;

import java.awt.Container;

import map.Map;
import objets.InterfaceDestructible;
import physique.forme.Forme;
import reseau.paquets.Paquet;
import reseau.paquets.TypePaquet;
import reseau.serveur.Serveur;
import divers.Outil;

public abstract class Destructible extends Objet {
    public static final int VIE_DEFAUT = 10;
    private int resistance;


    public Destructible(Map map, ContaineurImagesOp images, int fond, Forme forme, int id, int resistance, int vie) {
	super(map, images, id, fond, forme);
	this.resistance = resistance == 0 ? VIE_DEFAUT : resistance;
	setVie(vie == 0 ? resistance : vie);
    }

    @Override
    public void setServeur(Serveur serveur) {
	super.setServeur(serveur);
    }

    public void setResistance(int resistance) {
	this.resistance = resistance;
    }

    @Override
    public IO getPaquetVie() {
	return new Paquet(TypePaquet.VIE_OBJET).addBytePositif(getXMap()).addBytePositif(getYMap()).addBytePositif(getVie());
    }

    @Override
    public int getVitalite() {
	return resistance;
    }

    @Override
    public void construireInterface(Container c, boolean editable) {
	if(editable)
	    new InterfaceDestructible(this, c);
	else c.add(Outil.creerPanel("Resistance", getVie() + "/" + resistance));
    }

    @Override
    public boolean estVide() {
	return false;
    }

    @Override
    public String toString() {
	return super.toString() + "[DESTRUCTIBLE " + getVie() + "/" + resistance + "]";
    }

    @Override
    public IO sauvegarder(IO io) {
	return super.sauvegarder(io).addBytePositif(getID()).addBytePositif(resistance).addBytePositif(resistance);
    }

}
