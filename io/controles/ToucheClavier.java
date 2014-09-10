package controles;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import divers.Listenable;

public class ToucheClavier extends Listenable implements KeyListener {
	
	
	public ToucheClavier(ToucheListener... l) {
		addListeners(ToucheListener.class, l);
	}
	
	public void addToucheListener(ToucheListener l) {
		addListener(ToucheListener.class, l);
	}
	
	public void removeToucheListener(ToucheListener l) {
		removeListener(ToucheListener.class, l);
	}
	
	public void notifyToucheListener(int key) {
		for(final ToucheListener l : getListeners(ToucheListener.class))
			l.appuie(key);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		notifyToucheListener(e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
}
