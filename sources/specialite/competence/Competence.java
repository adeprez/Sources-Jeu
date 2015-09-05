package specialite.competence;

import java.awt.image.BufferedImage;

import perso.AbstractPerso;
import physique.actions.vivant.AbstractActionVivant;
import ressources.Images;

public abstract class Competence extends AbstractActionVivant<AbstractPerso> {
    public static final String PATH = "competences/";


    public Competence(AbstractPerso source) {
	super(source);
    }

    public Competence(AbstractPerso source, boolean active) {
	this(source);
    }

    public abstract String getNomIcone();

    public BufferedImage getIcone() {
	return Images.get(PATH + getNomIcone() + ".png", true);
    }

    @Override
    public String toString() {
	return getNomIcone();
    }

}
