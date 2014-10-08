package composants.styles.fenetres;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JComponent;

import ressources.Images;

import composants.FenetreInterne;

public class FenetreInterneTransparente extends FenetreInterne {
    private static final long serialVersionUID = 1L;
    private final Image header;


    public FenetreInterneTransparente(String titre, JComponent content) {
	this(Images.get("fond/parchemin.jpg", true), titre, content);
    }

    public FenetreInterneTransparente(Image header, String titre, JComponent content) {
	super(titre, content);
	this.header = header;
	getRootPane().setOpaque(false);
	setFocusable(false);
    }

    @Override
    public void paintComponent(Graphics g) {
	g.drawImage(header, 0, 0, getWidth(), 50, null);
    }


}
