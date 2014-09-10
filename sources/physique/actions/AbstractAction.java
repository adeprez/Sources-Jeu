package physique.actions;

import interfaces.Fermable;
import interfaces.Lancable;
import perso.Vivant;
import ressources.sprites.animation.Membre;
import ressources.sprites.animation.sequence.Sequence;

public abstract class AbstractAction<E extends Vivant> implements Lancable, Fermable, Actionable {
	private final E source;
	private AbstractAction<?> suivante;
	private int tour;


	public AbstractAction(E source) {
		this.source = source;
	}
	
	public abstract boolean estCompetence();
	public abstract Sequence getSequence();
	public abstract void tourAction();
	public abstract void commence();
	public abstract void seTermine();
	public abstract boolean peutFaire(AbstractAction<?> courante);
	public abstract boolean peutArret();
	
	public AbstractAction<?> getSuivante() {
		return suivante;
	}

	public void setSuivante(AbstractAction<?> suivante) {
		this.suivante = suivante;
	}

	public E getSource() {
		return source;
	}

	public void demarre() {
		tour = 0;
		commence();
		source.getAnimation().setSequence(getSequence());
	}

	public boolean stopAction() {
		return source.stopAction(null);
	}

	public void changeAnim() {
		source.getAnimation().setSequence(getSequence());
	}

	public void setVitesseAnim(int vitesse) {
		source.getAnimation().setVitesse(vitesse);
	}

	public void toOrigine() {
		for(final Membre m : source.getAnimation().getMembres())
			m.setAngleFutur(0);
	}

	public void forceLancement() {
		tour = 0;
		source.forceAction(this);
	}
	public int getTour() {
		return tour;
	}
	
	@Override
	public boolean faireAction() {
		tourAction();
		tour++;
		return true;
	}

	@Override
	public boolean lancer() {
		return peutFaire(source.getAction()) && source.setAction(this);
	}

	@Override
	public boolean fermer() {
		if(peutArret()) {
			seTermine();
			return true;
		} 
		return false;
	}
	
}
