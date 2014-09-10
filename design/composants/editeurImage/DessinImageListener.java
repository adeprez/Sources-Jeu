package composants.editeurImage;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class DessinImageListener extends MouseAdapter {
	private final PanelDessin panel;
	private boolean enfonce;
	

	public DessinImageListener(PanelDessin panel) {
		this.panel = panel;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if(enfonce)
			panel.dessiner(e.getPoint());
	}

	@Override
	public void mousePressed(MouseEvent e) {
		enfonce = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		enfonce = false;
	}
	
	
	
}
