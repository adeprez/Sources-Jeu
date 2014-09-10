package composants.editeurImage;

import interfaces.Modifiable;

import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class EditeurImage extends JFrame {
	private static final long serialVersionUID = 1L;
	private final Modifiable<BufferedImage> modifiable;
	private final BufferedImage image;
	private final PanelDessin dessin;

	
	public EditeurImage(Modifiable<BufferedImage> modifiable, BufferedImage image) {
		super("Editer une image");
		setUndecorated(true);
		this.modifiable = modifiable;
		this.image = image;
		setLocationRelativeTo(null);
		dessin = new PanelDessin(image);
		setContentPane(dessin);
		setJMenuBar(new MenuEditeurImage(this, dessin));
		pack();
	}

	public void modifier() {
		modifiable.modifier(image);
	}
	
	
}
