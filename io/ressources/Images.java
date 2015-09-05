package ressources;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public final class Images {
    public static final String PATH = "images/";
    private static final Map<String, BufferedImage> images = new HashMap<String, BufferedImage>();


    private Images() {}

    public static BufferedImage get(String nom, boolean stocker) {
	if(stocker) {
	    if(!images.containsKey(nom))
		images.put(nom, RessourcesLoader.getImage(PATH + nom));
	    return images.get(nom);
	}
	return RessourcesLoader.getImage(PATH + nom);
    }

    public static BufferedImage get(String nom) {
	return get(nom, true);
    }

    public static void liberer() {
	synchronized(images) {
	    for(BufferedImage img : images.values())
		img.flush();
	    images.clear();
	}
    }

}
