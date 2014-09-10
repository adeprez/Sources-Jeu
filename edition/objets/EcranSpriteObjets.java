package objets;

import interfaces.Cliquable;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import listeners.CliqueListener;
import ressources.SpriteObjets;

import composants.SelecteurImage;
import composants.diaporama.DiaporamaModifiable;
import composants.panel.PanelImage;

import exceptions.AnnulationException;

public class EcranSpriteObjets extends DiaporamaModifiable<PanelImage> implements Cliquable {
	private static final long serialVersionUID = 1L;

	
	public EcranSpriteObjets() {
		setName("Objets");
		addMouseListener(new CliqueListener(this));
		actualise();
	}


	public void actualise() {
		diapo.getModele().vider();
		for(final BufferedImage img : SpriteObjets.getInstance().getSprite().getImages())
			diapo.getModele().ajouter(new PanelImage(img));
	}
	
	@Override
	public void creer() {
		try {
			SpriteObjets.getInstance().addImage(SelecteurImage.chargeImage());
			actualise();
			diapo.getModele().setIndex(diapo.getModele().getNombre() - 1);
		} catch(AnnulationException e) {}
	}

	@Override
	public void enregistrer() {
		SpriteObjets.getInstance().enregistrer();
	}

	@Override
	public void clique(MouseEvent e) {
		try {
			int index = diapo.getModele().getIndex();
			SpriteObjets.getInstance().setImage(diapo.getModele().getIndex(), SelecteurImage.chargeImage());
			actualise();
			diapo.getModele().setIndex(index);
		} catch(AnnulationException e1) {}
	}

}
