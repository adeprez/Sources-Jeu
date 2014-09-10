package ressources;

import interfaces.ContaineurImageOp;
import interfaces.ContaineurImagesOp;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import ressources.sprites.FeuilleSprite;
import ressources.sprites.Sprite;
import ressources.sprites.Sprites;


public class SpriteObjets implements ContaineurImagesOp {
	private static final String PATH = "objets";
	private static SpriteObjets instance;
	private FeuilleSprite sprite;


	private SpriteObjets() {
		if(Fichiers.existe(Sprite.PATH + PATH))
			sprite = Sprites.getSprite(PATH, true);
		else {
			sprite = new FeuilleSprite(PATH, new Dimension(64, 64));
			Sprite.creer(sprite);
		}
	}

	public static SpriteObjets getInstance() {
		synchronized(SpriteObjets.class) {
			if(instance == null)
				instance = new SpriteObjets();
			return instance;
		}
	}

	public FeuilleSprite getSprite() {
		return sprite;
	}
	
	public void addImage(BufferedImage image) {
		sprite.addImage(image);
	}
	
	public void removeImage(int id) {
		sprite.removeImage(id);
	}
	
	public void enregistrer() {
		Sprite.creer(sprite);
	}

	public void setImage(int id, BufferedImage image) {
		sprite.setImage(id, image);
	}

	@Override
	public BufferedImage getImage(int id) {
		return sprite.getImage(id);
	}

	@Override
	public ContaineurImageOp getImageOp(int id) {
		return sprite.getImageOp(id);
	}
	
}
