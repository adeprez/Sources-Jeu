package divers;

import java.util.ArrayList;

import javax.swing.ComboBoxModel;
import javax.swing.ListModel;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class Liste<E> extends ArrayList<E> implements ListModel<E>, ComboBoxModel<E> {
	private static final long serialVersionUID = 1L;
	private final EventListenerList listenerList;
	private int index;


	public Liste() {
		listenerList = new EventListenerList();
	}

	public void notifyChangement() {
		ListDataEvent e = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, getSize());
		for(final ListDataListener l : listenerList.getListeners(ListDataListener.class))
			l.contentsChanged(e);
	}
	
	@Override
	public boolean add(E e) {
		if(super.add(e)) {
			notifyChangement();
			return true;
		}
		return false;
	}

	@Override
	public void add(int index, E element) {
		super.add(index, element);
		notifyChangement();
	}

	@Override
	public E remove(int index) {
		E e = super.remove(index);
		notifyChangement();
		return e;
	}

	@Override
	public boolean remove(Object o) {
		if(super.remove(o)) {
			notifyChangement();
			return true;
		}
		return false;
	}
	
	@Override
	public void clear() {
		super.clear();
		notifyChangement();
	}

	@Override
	public E set(int index, E element) {
		E e = super.set(index, element);
		notifyChangement();
		return e;
	}

	@Override
	public E getElementAt(int index) {
		return get(index);
	}

	@Override
	public int getSize() {
		return size();
	}
	
	@Override
	public Object getSelectedItem() {
		return get(index);
	}

	@Override
	public void setSelectedItem(Object anItem) {
		index = indexOf(anItem);
	}

	@Override
	public void addListDataListener(ListDataListener l) {
		listenerList.add(ListDataListener.class, l);
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		listenerList.remove(ListDataListener.class, l);
	}


}
