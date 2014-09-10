package territoire;

import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import carte.element.CaseTerritoire;

import divers.Outil;

public class InformationCaseTerritoire extends JPanel {
	private static final long serialVersionUID = 1L;
	private final JLabel coord, contenu;

	
	public InformationCaseTerritoire() {
		super(new GridLayout(2, 1));
		setOpaque(false);
		coord = Outil.encadrer(Outil.getTexte("(Aucune selection)", false), true);
		contenu = Outil.encadrer(Outil.getTexte("", false), true);
		add(coord);
		add(contenu);
	}
	
	public void setSelection(CaseTerritoire objet) {
		coord.setText("Position : {" + objet.getPosX() + ";" + objet.getPosY() + "}  |||  Altitude : " + objet.getAltitude() + "m");
		contenu.setText(objet.getNomContenu());
		contenu.setIcon(new ImageIcon(objet.getImage()));
	}

	public void deselection() {
		coord.setText("(Aucune selection)");
		contenu.setText("");
		contenu.setIcon(null);
	}
	
	
}
