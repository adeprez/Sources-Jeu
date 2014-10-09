package map.elements;

import interfaces.Dessinable;
import interfaces.Localise;

import java.awt.Graphics2D;

import listeners.RemoveListener;
import vision.Camera;

public class DessinableTemporaire extends LocaliseDessinableDefaut {
    private final RemoveListener<DessinableTemporaire> listener;
    private final int duree;
    private int temps;
    private long t;


    public DessinableTemporaire(Localise element, Dessinable dessin, int duree, RemoveListener<DessinableTemporaire> listener) {
	super(element, dessin);
	this.listener = listener;
	this.duree = duree;
    }

    public int getTemps() {
	return temps;
    }

    public int getDuree() {
	return duree;
    }

    @Override
    public void dessiner(Camera c, Graphics2D gPredessin, Graphics2D gDessin, Graphics2D gSurdessin) {
	if(temps < duree) {
	    if(t > 0)
		temps += System.currentTimeMillis() - t;
	    super.dessiner(c, gPredessin, gDessin, gSurdessin);
	    t = System.currentTimeMillis();
	}
	else listener.remove(this);
    }

}
