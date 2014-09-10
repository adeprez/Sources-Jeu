package composants.styles.fenetres;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JDialog;
import javax.swing.JPanel;

import base.Fenetre;

public class FenetrePopup extends JDialog {
	private static final long serialVersionUID = 1L;


	public <E extends Component> FenetrePopup(String titre, E c) {
		this(titre);
		setContenu(c);
	}
	
	public FenetrePopup(String titre, boolean decore) {
		super(Fenetre.getInstance(), titre, true);
		setUndecorated(!decore);
	}
	
	public FenetrePopup(String titre) {
		this(titre, true);
	}
	
	public FenetrePopup setContenu(Component c) {
		JPanel p = new JPanel(new GridLayout());
		p.add(c);
		setContentPane(p);
		return this;
	}
	
	public FenetrePopup setContenu(Container c) {
		setContentPane(c);
		return this;
	}
	
	public FenetrePopup centrer(Component c) {
		setLocationRelativeTo(c);
		return this;
	}
	
	public FenetrePopup centrer() {
		return centrer(Fenetre.getInstance());
	}
	
	public FenetrePopup afficher() {
		setVisible(true);
		return this;
	}
	
	public FenetrePopup afficher(Dimension taille) {
		setSize(taille);
		setLocationRelativeTo(null);
		setVisible(true);
		return this;
	}

}
