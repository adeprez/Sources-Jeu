package ressources.sprites.animation.sequence;


public class Sequence implements DetermineurAngle {
    private final ModeleSequence modele;
    private DetermineurAngle determineurAngle;
    private boolean boucle, retourOrigine;
    private Sequence suivante;


    public Sequence(ModeleSequence modele) {
	this.modele = modele;
	determineurAngle = this;
	boucle = true;
	retourOrigine = true;
    }

    public Sequence setSequenceur(DetermineurAngle sequenceur) {
	this.determineurAngle = sequenceur;
	return this;
    }

    public DetermineurAngle getDetermineurAngle() {
	return determineurAngle;
    }

    public ModeleSequence getModele() {
	return modele;
    }

    public Sequence setSuivante(Sequence suivante) {
	this.suivante = suivante;
	return this;
    }

    public Sequence getSuivante() {
	return suivante;
    }

    public boolean boucle() {
	return boucle;
    }

    public Sequence setBoucle(boolean boucle) {
	this.boucle = boucle;
	return this;
    }

    public boolean retourOrigine() {
	return retourOrigine;
    }

    public Sequence setRetourOrigine(boolean retourOrigine) {
	this.retourOrigine = retourOrigine;
	return this;
    }

    @Override
    public float getAngle(int id, float[] angles) {
	return angles[id];
    }

}
