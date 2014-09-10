package listeners;

import interfaces.DoubleCliquable;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DoubleCliqueListener extends MouseAdapter {
	private final DoubleCliquable cliquable;
	
	
	public DoubleCliqueListener(DoubleCliquable cliquable) {
		this.cliquable = cliquable;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getClickCount() > 1)
			cliquable.doubleClique(e);
	}

	
}
