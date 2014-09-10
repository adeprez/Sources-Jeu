package listeners;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JScrollPane;

public class MouseScrollListener extends MouseAdapter {
	private final JScrollPane scroll;
	private Point precedent;

	
	public MouseScrollListener(JScrollPane scroll) {
		this.scroll = scroll;
	}
	
	public void setListener() {
		scroll.addMouseListener(this);
		scroll.addMouseMotionListener(this);
	}
	
	public void removeListener() {
		scroll.removeMouseListener(this);
		scroll.removeMouseMotionListener(this);
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		precedent = e.getPoint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if(precedent != null) {
			scroll.getHorizontalScrollBar().setValue(scroll.getHorizontalScrollBar().getValue() + precedent.x - e.getX());
			scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getValue() + precedent.y - e.getY());
		}
		precedent = e.getPoint();
	}

}
