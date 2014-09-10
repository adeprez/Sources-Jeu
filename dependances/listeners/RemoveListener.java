package listeners;

import java.util.EventListener;

public interface RemoveListener<E> extends EventListener {
	public void remove(E e);
}
