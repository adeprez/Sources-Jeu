package ressources.sprites;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import ressources.Fichiers;
import ressources.RessourcesLoader;
import ressources.compte.Compte;
import divers.Outil;

public class SpritePerso {
	private static final Rectangle[] ZONES = new Rectangle[] {
		//bras avant
		new Rectangle(0, 0, 30, 70),
		new Rectangle(0, 70, 30, 60),
		new Rectangle(0, 130, 30, 30),
		//bras arriere
		new Rectangle(30, 0, 30, 70),
		new Rectangle(30, 70, 30, 60),
		new Rectangle(30, 130, 30, 30),
		//jambe avant
		new Rectangle(60, 0, 50, 85),
		new Rectangle(60, 85, 50, 70),
		new Rectangle(0, 160, 60, 30),
		//jambe arriere
		new Rectangle(110, 0, 50, 85),
		new Rectangle(110, 85, 50, 70),
		new Rectangle(60, 160, 60, 30),
		//corps
		new Rectangle(160, 65, 65, 150),
		//tete
		new Rectangle(160, 0, 65, 65)
	};
	private static final int 
	B = 0, A = 1, M = 2, B2 = 3, A2 = 4, M2 = 5, 
	C = 6, J = 7, P = 8, C2 = 9, J2 = 10, P2 = 11,
	CORPS = 12, TETE = 13;
	private final BufferedImage[] images;


	public SpritePerso(String compte, String perso) {
		this(getImage(compte, perso));
	}

	public SpritePerso(BufferedImage image) {
		images = decoupe(image);
	}

	public BufferedImage getAvantBras(boolean avant) {
		return get(avant ? A : A2);
	}

	public BufferedImage getBras(boolean avant) {
		return get(avant ? B : B2);
	}

	public BufferedImage getMain(boolean avant) {
		return get(avant ? M : M2);
	}

	public BufferedImage getCuisse(boolean avant) {
		return get(avant ? C : C2);
	}

	public BufferedImage getJambe(boolean avant) {
		return get(avant ? J : J2);
	}

	public BufferedImage getPied(boolean avant) {
		return get(avant ? P : P2);
	}

	public BufferedImage getCorps() {
		return get(CORPS);
	}

	public BufferedImage getTete() {
		return get(TETE);
	}

	public BufferedImage get(int id) {
		return images[id];
	}

	private static BufferedImage[] decoupe(BufferedImage image) {
		BufferedImage[] images = new BufferedImage[ZONES.length];
		for(int i=0 ; i<images.length ; i++) {
			Rectangle r = ZONES[i];
			images[i] = image.getSubimage(r.x, r.y, r.width, r.height);
		}
		return images;
	}
	
	public static BufferedImage getImage(String compte, String perso) {
		return RessourcesLoader.getImage(Fichiers.PATH + Compte.pathPerso(compte, perso) + "sprite.png");
	}
	
	public static void saveZones() {
		try {
			ImageIO.write(creerZones(), "png", Outil.getFichier("Enregistrer"));
		} catch(Exception e) {
			Outil.erreur(e.getMessage());
		}
	}
	
	public static BufferedImage creerZones() {
		int l = 0, h = 0;
		for(final Rectangle r : ZONES) {
			if(r.x + r.width > l)
				l = r.x + r.width;
			if(r.y + r.height > h)
				h = r.y + r.height;
		}
		BufferedImage img = new BufferedImage(l, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();
		for(final Rectangle r : ZONES) {
			g.setColor(new Color(Outil.r().nextInt(255), Outil.r().nextInt(255), Outil.r().nextInt(255), 150));
			g.fill(r);
		}
		return img;
	}

}
