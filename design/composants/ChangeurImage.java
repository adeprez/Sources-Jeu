package composants;

import interfaces.Modifiable;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractButton;

import composants.editeurImage.EditeurImage;
import composants.styles.Bouton;

import exceptions.AnnulationException;

public class ChangeurImage extends SelecteurImage implements Modifiable<BufferedImage> {
	private static final long serialVersionUID = 1L;
	private final AbstractButton modif;
	
	
	public ChangeurImage(int id) {
		super(id);
		modif = new Bouton("Modifier");
		modif.addActionListener(this);
		add(modif);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		if(e.getSource() == modif)
			ouvrirModificateur();
	}

	@Override
	public void modifier(BufferedImage e) {
		setImage(e);
	}

	private void ouvrirModificateur() {
		BufferedImage img;
		try {
			img = getImage();
		} catch (AnnulationException e) {
			img = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
		}
		EditeurImage e = new EditeurImage(this, img);
		e.setAlwaysOnTop(true);
		e.setVisible(true);
	}
	
}
