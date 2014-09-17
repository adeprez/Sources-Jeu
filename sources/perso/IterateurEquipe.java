package perso;

import java.util.List;

public class IterateurEquipe<E extends Vivant> extends IterateurEnsemble<E> {
    private final boolean memeEquipe, avecMorts;
    private final int equipe;


    public IterateurEquipe(int equipe, boolean memeEquipe, boolean avecMorts, List<E> liste) {
	super(liste);
	this.equipe = equipe;
	this.memeEquipe = memeEquipe;
	this.avecMorts = avecMorts;
    }

    @Override
    public boolean iterable(E e) {
	return (avecMorts || e.estVivant()) && (memeEquipe ? e.getEquipe() == equipe : e.getEquipe() != equipe);
    }

}
