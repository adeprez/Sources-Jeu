package reseau.ressources;

import io.IO;

import java.awt.image.BufferedImage;

import map.objets.Objet;

public class RessourceImageObjet extends RessourceImage {


	public RessourceImageObjet(int id, BufferedImage image) {
		super(id, image);
	}

	public RessourceImageObjet(IO io) {
		super(io);
	}

	public RessourceImageObjet(Objet o) {
		this(o.getID(), o.getImage());
	}

	@Override
	public TypeRessource getType() {
		return TypeRessource.IMAGE_OBJET;
	}


}
