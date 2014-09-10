package vision;

import java.awt.event.MouseEvent;

import listeners.PositionSourisListener;
import divers.Listenable;
import exceptions.ExceptionJeu;


public class DeplacementSouris extends Listenable implements PositionSourisListener {
	protected Camera camera;


	public DeplacementSouris() {}

	public DeplacementSouris(Camera camera) {
		this.camera = camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	@Override 
	public void deplace(int dx, int dy) {
		try {
			camera.getSource().setX(camera.getSource().getX() + camera.getRapportX(dx));
			camera.getSource().setY(camera.getSource().getY() - camera.getRapportY(dy));
		} catch(ExceptionJeu err) {
			err.printStackTrace();
		}
	}

	@Override
	public void clique(MouseEvent e) {

	}

}
