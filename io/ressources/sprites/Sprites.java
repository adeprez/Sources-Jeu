package ressources.sprites;

import java.util.HashMap;
import java.util.Map;

public final class Sprites {
	private static final Map<String, FeuilleSprite> sprites = new HashMap<String, FeuilleSprite>();
	private static final Map<String, EnsembleSprite> animations = new HashMap<String, EnsembleSprite>();
	
	
	private Sprites() {}
	
	public static FeuilleSprite getSprite(String nom, boolean memorise) {
		if(sprites.containsKey(nom))
			return sprites.get(nom);
		FeuilleSprite sprite = new FeuilleSprite(nom);
		if(memorise)
			sprites.put(nom, sprite);
		return sprite;
	}
	
	public static EnsembleSprite getAnimation(String nom, boolean memorise) {
		if(animations.containsKey(nom))
			return animations.get(nom);
		EnsembleSprite sprite = new EnsembleSprite(nom);
		if(memorise)
			animations.put(nom, sprite);
		return sprite;
	}
	
}
