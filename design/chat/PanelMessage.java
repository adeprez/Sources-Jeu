package chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import perso.Perso;
import statique.Style;

public class PanelMessage extends JPanel implements MouseListener {
    private static final long serialVersionUID = 1L;
    private final Color fond, texte;
    private final JTextArea txt;
    private final boolean self;
    private final Perso cible;
    private Color couleur;


    public PanelMessage(String message, Perso expediteur, Color texte, Color fond, boolean self, Perso cible) {
	this.fond = fond;
	this.texte = texte;
	this.self = self;
	this.cible = cible;
	couleur = fond;
	setOpaque(false);
	setName(expediteur.getNom());
	setLayout(new BorderLayout(5, 5));
	txt = new JTextArea(" " + message);
	txt.setFont(Style.POLICE.deriveFont(16f));
	txt.setForeground(texte);
	txt.setEditable(false);
	txt.setBackground(new Color(0, 0, 0, 0));
	txt.setFocusable(false);
	txt.addMouseListener(this);
	txt.setBorder(null);
	txt.setDragEnabled(true);
	if(expediteur != null) {
	    JLabel l = new JLabel(expediteur.getSmallIcone());
	    l.setToolTipText(getName());
	    add(l, self ? BorderLayout.EAST : BorderLayout.WEST);
	}
	add(txt, BorderLayout.CENTER);
	add(Box.createRigidArea(new Dimension()), self ? BorderLayout.WEST : BorderLayout.EAST);
	add(Box.createRigidArea(new Dimension()), BorderLayout.NORTH);
    }

    public Perso getCible() {
	return cible;
    }

    public boolean estOrigine() {
	return self;
    }

    @Override
    protected void paintComponent(Graphics g) {
	super.paintComponent(g);
	g.setColor(couleur);
	((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	g.fillRoundRect(txt.getX(), txt.getY(), txt.getWidth(), txt.getHeight(), 15, 15);
	int x = self ? txt.getX() + txt.getWidth() : txt.getX();
	g.fillPolygon(new int[] {x, x, x + (self ? 10 : -10)}, new int[] {txt.getY() + txt.getHeight()/2 + 2, txt.getY() + txt.getHeight()/2 + 10,
		txt.getY() + txt.getHeight()/2 + 7}, 3);
	g.drawRoundRect(txt.getX(), txt.getY(), txt.getWidth(), txt.getHeight(), 15, 15);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }


    @Override
    public void mousePressed(MouseEvent e) {
    }


    @Override
    public void mouseReleased(MouseEvent e) {
    }


    @Override
    public void mouseEntered(MouseEvent e) {
	txt.setFocusable(true);
	couleur = Color.WHITE;
	txt.setForeground(Color.BLACK);
    }


    @Override
    public void mouseExited(MouseEvent e) {
	txt.setFocusable(false);
	txt.setForeground(texte);
	couleur = fond;
    }

}
