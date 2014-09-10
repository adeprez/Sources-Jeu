package composants.editeurImage;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JColorChooser;
import javax.swing.JMenuBar;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import composants.styles.Bouton;

public class MenuEditeurImage extends JMenuBar implements ActionListener, ChangeListener {
	private static final long serialVersionUID = 1L;
	private final AbstractButton valider, quitter,  couleur;
	private final EditeurImage editeur;
	private final PanelDessin dessin;
	private final JSpinner taille;

	
	public MenuEditeurImage(EditeurImage editeur, PanelDessin dessin) {
		this.editeur = editeur;
		this.dessin = dessin;
		setBackground(Color.DARK_GRAY);
		couleur = new Bouton("Couleur");
		valider = new Bouton("Valider");
		quitter = new Bouton("Fermer");
		taille = new JSpinner(new SpinnerNumberModel(dessin.getTaille(), 1, 50, 1));
		taille.addChangeListener(this);
		couleur.addActionListener(this);
		valider.addActionListener(this);
		quitter.addActionListener(this);
		add(taille);
		add(couleur);
		add(valider);
		add(quitter);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == valider)
			editeur.modifier();
		else if(e.getSource() == quitter)
			editeur.dispose();
		else if(e.getSource() == couleur)
			dessin.setCouleur(JColorChooser.showDialog(this, "Choisissez une couleur", Color.BLACK));
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		dessin.setTaille((Integer) taille.getValue());
	}
	
}
