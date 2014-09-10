package divers;

import interfaces.Actualisable;

import java.util.EventListener;

import javax.swing.event.EventListenerList;

public class Listenable {
	private EventListenerList listenerList;


	public void addActualiseListener(Actualisable l) {
		addListener(Actualisable.class, l);
	}

	public void removeActualiseListener(Actualisable l) {
		removeListener(Actualisable.class, l);
	}

	public void notifyActualiseListener() {
		for(final Actualisable l : getListeners(Actualisable.class)) try {
			l.actualise();
		} catch(Exception err) {
			err.printStackTrace();
		}
	}

	public EventListenerList getListenerList() {
		if(listenerList == null)
			listenerList = new EventListenerList();
		return listenerList;
	}

	@SuppressWarnings("unchecked")
	public <T extends EventListener> void addListeners(Class<T> t, T... ll) {
		for(final T l : ll)
			getListenerList().add(t, l);
	}

	public <T extends EventListener> void addListener(Class<T> t, T l) {
		getListenerList().add(t, l);
	}

	public <T extends EventListener> void removeListener(Class<T> t, T l) {
		getListenerList().remove(t, l);
	}

	public <T extends EventListener> T[] getListeners(Class<T> t) {
		return getListenerList().getListeners(t);
	}

	public void removeAllListeners() {
		listenerList = new EventListenerList();
	}


}
