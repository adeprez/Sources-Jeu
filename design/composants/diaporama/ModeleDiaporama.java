package composants.diaporama;

import java.util.List;

import listeners.AjoutListener;
import listeners.ChangeDiapoListener;
import divers.ListeCyclique;
import divers.Listenable;

public class ModeleDiaporama<E> extends Listenable {
	private final ListeCyclique<E> elements;
	private int index;
	
	
	public ModeleDiaporama() {
		elements = new ListeCyclique<E>();
	}
	
	public void setIndex(int index) {
		this.index = index;
		notifyChangeDiapoListener();
	}
	
	public List<E> getElements() {
		return elements;
	}

	public int getIndex() {
		return elements.checkIndex(index);
	}
	
	public void suivant() {
		index ++;
		setIndex(getIndex());
	}
	
	public void precedent() {
		index --;
		setIndex(getIndex());
	}
	
	public void ajouter(E e) {
		elements.add(e);
		notifyAjoutListener(e);
		setIndex(elements.size() - 1);
	}
	
	public void vider() {
		elements.clear();
		index = 0;
	}
	
	public boolean aElement() {
		return !elements.isEmpty();
	}

	public E getElement() {
		return elements.get(index);
	}

	public int getNombre() {
		return elements.size();
	}
	
	public void addChangeDiapoListener(ChangeDiapoListener l) {
		addListener(ChangeDiapoListener.class, l);
	}
	
	public void removeChangeDiapoListener(ChangeDiapoListener l) {
		removeListener(ChangeDiapoListener.class, l);
	}
	
	public void addAjoutListener(AjoutListener<E> l) {
		addListener(AjoutListener.class, l);
	}
	
	public void removeAjoutListener(AjoutListener<E> l) {
		removeListener(AjoutListener.class, l);
	}

	private void notifyChangeDiapoListener() {
		E e = aElement() ? getElement() : null;
		for(final ChangeDiapoListener l : getListeners(ChangeDiapoListener.class))
			l.changeDiapo(e);
	}

	@SuppressWarnings("unchecked")
	private void notifyAjoutListener(E o) {
		for(final AjoutListener<E> l : getListeners(AjoutListener.class))
			l.ajout(o);
	}

}
