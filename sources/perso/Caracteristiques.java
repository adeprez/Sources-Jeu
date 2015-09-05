package perso;

import interfaces.Sauvegardable;
import io.IO;

public class Caracteristiques implements Sauvegardable {
    public static final String PATH = "caract";
    private final int[] caract;


    public Caracteristiques(int vitalite, int force, int vitesse) {
	caract = new int[] {vitalite, vitalite, force, vitesse};
    }

    public Caracteristiques(int vie, int vitalite, int force, int vitesse) {
	caract = new int[] {vie, vitalite, force, vitesse};
    }

    public Caracteristiques(IO io) {
	this(io.nextShortInt(), io.nextShortInt(), io.nextShortInt(), io.nextShortInt());
    }

    public int getVie() {
	return get(0);
    }

    public int getVitalite() {
	return get(1);
    }

    public int getForce() {
	return get(2);
    }

    public int getVitesse() {
	return get(3);
    }

    public int get(int id) {
	return caract[id];
    }

    @Override
    public IO sauvegarder(IO io) {
	return io.addIntsPositifs(caract);
    }

    @Override
    public String toString() {
	String s = "";
	for(final int i : caract)
	    s += i + ", ";
	return s.substring(0, s.length() - 2);
    }

}
