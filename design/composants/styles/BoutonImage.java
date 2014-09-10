package composants.styles;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

import ressources.Images;
import statique.Style;

public class BoutonImage extends JButton implements MouseListener {
	private static final long serialVersionUID = 1L;
	public static final int NORMAL = 0, SURVOL = 1, ENFONCE = 2;
	private boolean selectionne;
	private Image image;
	private int id;


	public BoutonImage(Image image, boolean tailleImage) {
		this();
		this.image = image;
		if(tailleImage)
			setPreferredSize(new Dimension(image.getWidth(null), image.getHeight(null)));
	}

	public BoutonImage(String nom) {
		this(nom, false);
	}

	public BoutonImage(String nom, boolean tailleImage) {
		this(Images.get(nom, false), tailleImage);
	}

	public BoutonImage(String nom, Dimension taille) {
		this(Images.get(nom, false).getScaledInstance(taille.width, taille.height, Image.SCALE_AREA_AVERAGING), true);
	}
	
	public BoutonImage() {
		setCursor(Style.curseur.main());
		setFont(Style.POLICE);
		setContentAreaFilled(false);
		addMouseListener(this);
	}

	public void set(int id) {
		this.id = id;
		repaint();
	}

	public void setSelectionne(boolean selectionne) {
		this.selectionne = selectionne;
		repaint();
	}
	
	public boolean estSelectionne() {
		return selectionne;
	}
	
	public BoutonImage setImage(String image) {
		return setImage(Images.get(image, true));
	}
	
	public BoutonImage setImage(Image image) {
		this.image = image;
		return this;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawImage(image, 5, 5, getWidth() - 10, getHeight() - 10, null);
		if(id != NORMAL) {
			Composite tmp = g2d.getComposite();
			g2d.setComposite(Style.TRANSPARENCE);
			switch(id) {
			case SURVOL:
				g.setColor(Color.WHITE);
				g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, getWidth()/5, getHeight()/5);
				break;
			case ENFONCE:
				g.setColor(Color.BLACK);
				g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, getWidth()/5, getHeight()/5);
				break;
			default:
				break;
			}
			g2d.setComposite(tmp);
		} if(selectionne) {
			g.setColor(Color.BLACK);
			Stroke tmp2 = g2d.getStroke();
			g2d.setStroke(new BasicStroke(6f));
			g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, getWidth()/5, getHeight()/5);
			g2d.setStroke(tmp2);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {
		set(SURVOL);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		set(NORMAL);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		set(ENFONCE);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		set(NORMAL);
	}

}
