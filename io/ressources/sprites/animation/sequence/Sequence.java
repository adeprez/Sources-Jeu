package ressources.sprites.animation.sequence;


public class Sequence implements DetermineurAngle {
	private final ModeleSequence modele;
	private DetermineurAngle determineurAngle;
	private Sequence suivante;
	private boolean boucle;
	
	
	public Sequence(ModeleSequence modele) {
		super();
		this.modele = modele;
		determineurAngle = this;
		boucle = true;
	}
	
	public void setSequenceur(DetermineurAngle sequenceur) {
		this.determineurAngle = sequenceur;
	}
	
	public DetermineurAngle getDetermineurAngle() {
		return determineurAngle;
	}
	
	public ModeleSequence getModele() {
		return modele;
	}

	public void setSuivante(Sequence suivante) {
		this.suivante = suivante;
	}
	
	public Sequence getSuivante() {
		return suivante;
	}
	
	public boolean boucle() {
		return boucle;
	}
	
	public void setBoucle(boolean boucle) {
		this.boucle = boucle;
	}

	@Override
	public float getAngle(int id, float[] angles) {
		return angles[id];
	}
	
}
