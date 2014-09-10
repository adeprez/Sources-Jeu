package listeners;

public interface ModificationListener<E> extends AjoutListener<E> {
	public void remove(E e);
}
