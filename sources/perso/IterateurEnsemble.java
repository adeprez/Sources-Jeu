package perso;

import java.util.Iterator;
import java.util.List;

public abstract class IterateurEnsemble<E> implements Iterator<E>, Iterable<E> {
	private final List<E> liste;
	private int index;

	
	public IterateurEnsemble(List<E> liste) {
		this.liste = liste;
		index = -1;
	}
	
	public abstract boolean iterable(E e);
	
	public int getIndexSuivant() {
		int i = index;
		do {
			i++;
		} while(i < liste.size() && !iterable(liste.get(i)));
		return i;
	}
	
	@Override
	public boolean hasNext() {
		return getIndexSuivant() < liste.size();
	}

	@Override
	public E next() {
		return liste.get(index = getIndexSuivant());
	}

	@Override
	public void remove() {
		System.err.println("IterateurEquipe : remove");
	}

	@Override
	public Iterator<E> iterator() {
		return this;
	}
	
	

}
