package util;



public class ElementListeChainee<E extends Comparable<E>> implements Comparable<E> {
    private final E element;
    private ElementListeChainee<E> suivant;


    public ElementListeChainee(E element) {
	this.element = element;
    }

    public ElementListeChainee<E> getSuivant() {
	return suivant;
    }

    public E getElement() {
	return element;
    }

    public void add(E e) {
	if(suivant == null)
	    suivant = new ElementListeChainee<E>(e);
	else if(suivant.compareTo(e) <= 0) {
	    ElementListeChainee<E> ne = new ElementListeChainee<E>(e);
	    ne.suivant = suivant;
	    suivant = ne;
	}
	else suivant.add(e);
    }

    @Override
    public int compareTo(E e) {
	return element.compareTo(e);
    }

}
