package listeners;

import interfaces.Cliquable;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CliqueListener extends MouseAdapter {
	private final Cliquable cliquable;
	
	
	public CliqueListener(Cliquable cliquable) {
		this.cliquable = cliquable;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		cliquable.clique(e);
	}

	
}
