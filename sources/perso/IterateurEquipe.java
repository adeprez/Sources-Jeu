package perso;

import java.util.List;

public class IterateurEquipe<E extends Vivant> extends IterateurEnsemble<E> {
	private final boolean memeEquipe;
	private final int equipe;

	
	public IterateurEquipe(int equipe, boolean memeEquipe, List<E> liste) {
		super(liste);
		this.equipe = equipe;
		this.memeEquipe = memeEquipe;
	}
	
	@Override
	public boolean iterable(E e) {
		return memeEquipe ? e.getEquipe() == equipe : e.getEquipe() != equipe;
	}

}
