package composants.styles;

import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.Border;

import statique.Style;
import bordures.BordureImage;

public class Bouton extends JButton implements MouseListener {
	private static final long serialVersionUID = 1L;
	public static final int NORMAL = 0, SURVOL = 1, ENFONCE = 2, RELACHE = 3, LARGE = 4;
	private final Border[] bordures;


	public Bouton(boolean titre) {
		setCursor(Style.curseur.main());
		setFont(titre ? Style.TITRE : Style.POLICE);
		setContentAreaFilled(false);
		bordures = new Border[] {
				new BordureImage("trait.png"),
				new BordureImage("trait survol.png"),
				new BordureImage("trait enfonce.png"),
				new BordureImage("trait relache.png"),
				new BordureImage("trait large.png")
		};
		setBordure(NORMAL);
		addMouseListener(this);
	}

	public Bouton(String texte) {
		this(texte, false);
	}

	public Bouton() {
		this(false);
	}

	public Bouton(String texte, boolean titre) {
		this(titre);
		setText("  " + texte + "  ");
	}

	public Bouton(Image image, String texte, boolean titre) {
		this(texte, titre);
		setIcon(new ImageIcon(image));
	}

	public Bouton large() {
		setBordure(LARGE);
		return this;
	}

	public void setBordure(int id) {
		if(id == NORMAL && getWidth() > 100)
			setBorder(bordures[LARGE]);
		else setBorder(bordures[id]);
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {
		if(isEnabled())
			setBordure(SURVOL);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		setBordure(NORMAL);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(isEnabled())
			setBordure(ENFONCE);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(isEnabled())
			setBordure(RELACHE);
	}

}
