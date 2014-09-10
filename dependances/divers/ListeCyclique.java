package divers;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;


public class ListeCyclique<E> extends ArrayList<E> {
	private static final long serialVersionUID = 1L;
	
	
	public int checkIndex(int index) {
		if(index < 0)
			return checkIndex(index + size());
		return isEmpty() ? 0 : (index % size());
	}

	@Override
	public void add(int index, E element) {
		super.add(checkIndex(index), element);
	}

	@Override
	public E get(int index) {
		return super.get(checkIndex(index));
	}

	@Override
	public E remove(int index) {
		return super.remove(checkIndex(index));
	}

	@Override
	public E set(int index, E element) {
		return super.set(checkIndex(index), element);
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return super.listIterator(checkIndex(index));
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		return super.subList(checkIndex(fromIndex), checkIndex(toIndex));
	}

	@Override
	protected void removeRange(int fromIndex, int toIndex) {
		super.removeRange(checkIndex(fromIndex), checkIndex(toIndex));
	}
	
	

}
