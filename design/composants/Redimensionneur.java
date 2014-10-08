package composants;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.Box;
import javax.swing.JPanel;

public class Redimensionneur extends JPanel implements MouseMotionListener, MouseListener {
    private static final int BORDURE = 6;
    private static final long serialVersionUID = 1L;
    private final Component haut, droite;
    private Dimension d;
    private int cursor;
    private Point p;


    public Redimensionneur(Component enfant) {
	super(new BorderLayout());
	setOpaque(false);
	add(enfant);
	droite = Box.createRigidArea(new Dimension(BORDURE, BORDURE));
	haut = Box.createRigidArea(new Dimension(BORDURE, BORDURE));
	addMouseMotionListener(this);
	addMouseListener(this);
	add(enfant, BorderLayout.CENTER);
	add(droite, BorderLayout.EAST);
	add(haut, BorderLayout.NORTH);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
	if(p != null) {
	    Dimension dd = new Dimension(d.width, d.height);
	    if(cursor == Cursor.E_RESIZE_CURSOR || cursor == Cursor.NE_RESIZE_CURSOR)
		dd.width += e.getX() - p.x;
	    if(cursor == Cursor.N_RESIZE_CURSOR || cursor == Cursor.NE_RESIZE_CURSOR)
		dd.height += p.y - e.getY();
	    setPreferredSize(dd);
	    getParent().doLayout();
	    getParent().validate();
	    getParent().repaint();
	}
    }

    @Override
    protected void paintComponent(Graphics g) {
	g.setColor(new Color(50, 100, 125, 200));
	g.fillRoundRect(0, 0, getWidth() - 1, getHeight(), BORDURE * 3, BORDURE * 3);
	((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	g.setColor(getBackground());
	g.fillRoundRect(-BORDURE, 0, getWidth() + BORDURE/2, BORDURE, BORDURE * 3, BORDURE * 3);
	g.fillRoundRect(getWidth() - BORDURE, BORDURE/2, BORDURE - 1, getHeight(), BORDURE * 3, BORDURE * 3);
	super.paintComponent(g);
	g.setColor(Color.BLACK);
	g.drawRoundRect(-BORDURE, BORDURE, getWidth() - 1, getHeight() + BORDURE, BORDURE, BORDURE);
	g.drawRoundRect(-BORDURE, 0, getWidth() + BORDURE - 1, getHeight() + BORDURE, BORDURE * 3, BORDURE * 3);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
	if(e.getY() < BORDURE) {
	    if(e.getX() > getWidth() - BORDURE)
		cursor = Cursor.NE_RESIZE_CURSOR;
	    else cursor = Cursor.N_RESIZE_CURSOR;
	}
	else if(e.getX() > getWidth() - BORDURE)
	    cursor = Cursor.E_RESIZE_CURSOR;
	else cursor = Cursor.DEFAULT_CURSOR;
	setCursor(Cursor.getPredefinedCursor(cursor));
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
	p = e.getPoint();
	d = getPreferredSize();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
	p = null;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
	setCursor(Cursor.getDefaultCursor());
    }

}
