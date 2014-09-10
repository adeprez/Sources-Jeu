package ressources.sprites.animation;

import interfaces.Nomme;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Membre extends AbstractMembre implements Nomme {
	private final AbstractMembre parent;
	private final BufferedImage image;
	private float angle, angleFutur;
	private List<Membre> membres;
	private int etapesToAngle;
	private float diffMouv;
	private String nom;


	public Membre(AbstractMembre parent, BufferedImage image, int l, int h, int x, int y, int py) {
		super(l, h, x, y, py);
		this.image = image;
		this.parent = parent;
	}

	public void addMembre(Membre m) {
		if(membres == null)
			membres = new ArrayList<Membre>();
		membres.add(m);
	}

	public void removeMembre(Membre e) {
		membres.remove(e);
		if(membres.isEmpty())
			membres = null;
	}

	public boolean bouge() {
		return etapesToAngle > 0;
	}

	public float getAngleFutur() {
		return angleFutur;
	}

	public synchronized void versAngle() {
		if(bouge()) {
			angle = angle(angle - diffMouv);
			etapesToAngle--;
			if(etapesToAngle < 0)
				finAutoMouv();
		}
	}

	public float getDiffAutoMouv() {
		float d = angle - angleFutur;
		if(d > Math.PI)
			d -= Math.PI * 2;
		else if(d < -Math.PI)
			d += Math.PI * 2;
		return d;
	}

	public void finAutoMouv() {
		angle = angleFutur;
		etapesToAngle = 0;
	}

	public void setAngleFutur(float anglefutur) {
		setAngleFutur(anglefutur, 10);
	}

	public synchronized void setAngleFutur(float angleFutur, int etapesToAngle) {
		float f = angle(angleFutur);
		if(Math.abs(f - this.angleFutur) > .05) {
			this.angleFutur = f;
			this.etapesToAngle = etapesToAngle;
			diffMouv = getDiffAutoMouv()/etapesToAngle;
		}
	}

	public AbstractMembre getParent() {
		return parent;
	}

	public synchronized void setAngle(float angle) {
		etapesToAngle = 0;
		this.angle = angle(angle);
	}

	public float getAngleMembre() {
		return angle;
	}

	public void setAngleMembre(float angle) {
		setAngle(getAnglePourMembre(angle));
	}

	public void setAngleMembreFutur(float angle) {
		setAngleFutur(getAnglePourMembre(angle));
	}

	public float getAnglePourMembre(float angleCible) {
		return angle + angleCible - getAngle(false);
	}

	public Rectangle getRect(Rectangle zone, boolean droite) {
		int l = getLargeur(zone), h = getHauteur(zone);
		return new Rectangle(getX(zone, droite) - val(getPrctX(), droite ? l : -l), getY(zone) - val(getPrctY(), h), l, h);
	}

	public boolean contient(Rectangle zone, boolean droite, Point p) {
		return getRect(zone, droite).contains(p);
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public void dessineImage(Graphics2D g, AffineTransform t) {
		g.drawImage(image, t, null);
	}

	@Override
	public BufferedImage getImage() {
		return image;
	}

	@Override
	public String toString() {
		return nom == null ? super.toString() : nom;
	}

	@Override
	public String getNom() {
		return nom;
	}

	@Override
	public float getAngle(boolean droite) {
		return (droite ? angle : -angle) + parent.getAngle(droite);
	}

	@Override
	public int getDecalage(AbstractMembre enfant, Rectangle zone, double angle) {
		return (int) (val(getPrctY() - enfant.getPrctYParent(), getHauteur(zone)) * angle);
	}

	@Override
	public int getX(Rectangle zone, boolean droite) {
		return parent.getX(zone, droite) + parent.getDecalageX(this, zone, droite);
	}

	@Override
	public int getY(Rectangle zone) {
		return parent.getY(zone) - parent.getDecalageY(this, zone);
	}

	@Override
	public void dessiner(Graphics2D g, Rectangle zone, boolean droite) {
		if(membres != null) try {
			for(int i=0 ; i<membres.size() ; i++)
				membres.get(i).dessiner(g, zone, droite);
		} catch(Exception e) {
			e.printStackTrace();
		}
		int l = getLargeur(zone), h = getHauteur(zone), dx = val(getPrctX(), droite ? l : -l), dy = val(getPrctY(), h);
		AffineTransform t = AffineTransform.getTranslateInstance(getX(zone, droite) - dx, getY(zone) - dy);
		float angle = getAngle(droite);
		if(angle != 0)
			t.rotate(angle, dx, dy);
		t.scale(((double) (droite ? l : -l))/image.getWidth(), ((double) h)/image.getHeight());
		dessineImage(g, t);
	}

	public static float angle(float angle) {
		while(angle < 0)
			angle += Math.PI * 2;
		return (float) (angle % (Math.PI * 2));
	}

}
