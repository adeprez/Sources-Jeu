package objets;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;

import javax.swing.JDialog;

import listeners.PositionListener;
import listeners.PositionTailleAdapter;
import ressources.SpriteObjets;
import ressources.sprites.FeuilleSprite;
import base.Fenetre;

import composants.panel.PanelImage;

public class SelecteurObjetSprite extends PanelImage implements PositionListener {
	private static final long serialVersionUID = 1L;
	private final FeuilleSprite sprite;
	private final Dimension taille;
	private final JDialog d;
	private Rectangle survol;
	private int id;

	
	private SelecteurObjetSprite(JDialog d) {
		super("sprites/objets.png");
		this.d = d;
		tailleImage();
		sprite = SpriteObjets.getInstance().getSprite();
		taille = sprite.getDimension();
		MouseAdapter l = new PositionTailleAdapter(this, taille);
		addMouseListener(l);
		addMouseMotionListener(l);
	}
	
	public void setID(int id) {
		this.id = id;
	}

	public int getID() {
		return id;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(survol != null)
			g.drawRect(survol.x, survol.y, survol.width, survol.height);
	}

	@Override
	public void clique(int x, int y) {
		id = (byte) (x + y*(getWidth()/taille.width));
		d.dispose();
	}

	@Override
	public void survol(int x, int y) {
		survol = new Rectangle(x*taille.width, y*taille.height, taille.width, taille.height);
		repaint();
	}
	
	public static int get() {
		Fenetre.getInstance().getGlassPane().setSombre(true);
		JDialog d = new JDialog(Fenetre.getInstance(), true);
		d.setUndecorated(true);
		SelecteurObjetSprite s = new SelecteurObjetSprite(d);
		d.setContentPane(s);
		d.pack();
		d.setLocationRelativeTo(null);
		d.setVisible(true);
		int id = s.getID();
		Fenetre.getInstance().getGlassPane().setSombre(false);
		return id;
	}
	
	public static BufferedImage getImage(int id) {
		return SpriteObjets.getInstance().getImage(id);
	}
	
	public static BufferedImage selectionneImage() {
		return getImage(get());
	}
	
	public static int selectionneID() {
		return get();
	}
	
}
