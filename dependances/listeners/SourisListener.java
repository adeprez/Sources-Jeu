package listeners;

import java.awt.Point;
import java.awt.event.MouseEvent;


public class SourisListener extends SurvolListener {
    private Point precedent;
    private PositionSourisListener l;


    public SourisListener() {}

    public SourisListener(PositionSurvolListener l) {
	super(l);
    }

    public SourisListener(PositionSurvolListener l1, PositionSourisListener l2) {
	super(l1);
	l = l2;
    }

    public SourisListener setPositionSourisListener(PositionSourisListener l) {
	this.l = l;
	return this;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
	if(precedent != null) {
	    l.deplace(precedent.x - e.getX(), precedent.y - e.getY());
	    precedent = e.getPoint();
	}
    }

    @Override
    public void mouseReleased(MouseEvent e) {
	precedent = null;
    }

    @Override
    public void mousePressed(MouseEvent e) {
	precedent = e.getPoint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
	if(l != null)
	    l.clique(e);
    }



}
