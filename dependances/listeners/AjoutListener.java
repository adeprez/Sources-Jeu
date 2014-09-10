package listeners;

import java.util.EventListener;

public interface AjoutListener<E> extends EventListener {
	public void ajout(E e);
}
