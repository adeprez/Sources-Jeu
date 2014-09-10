package sprites;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ressources.Images;
import ressources.sprites.Sprite;
import base.Ecran;

import composants.DimensionGraphique;
import composants.styles.Bouton;
import composants.styles.ChampTexte;

public class EcranSprite extends Ecran implements ActionListener {
	private static final long serialVersionUID = 1L;
	private final DimensionGraphique dimension;
	private final SelecteurFichiers fichiers;
	private final JTextField nom;
	private final JButton ok;


	public EcranSprite() {
		super(Images.get("fond/parchemin.jpg", true));
		setName("Sprites");
		setLayout(new BorderLayout());
		setMax(true);
		dimension = new DimensionGraphique();
		nom = new ChampTexte("Nouveau sprite", true, false);
		fichiers = new SelecteurFichiers();
		ok = new Bouton("Generer");
		ok.setPreferredSize(new Dimension(100, 30));
		ok.addActionListener(this);
		nom.addActionListener(this);
		JPanel p = new JPanel(new BorderLayout());
		p.setMaximumSize(new Dimension(5000, 30));
		p.setOpaque(false);
		p.add(ok, BorderLayout.WEST);
		p.add(nom, BorderLayout.CENTER);
		add(dimension, BorderLayout.NORTH);
		add(fichiers, BorderLayout.CENTER);
		add(p, BorderLayout.SOUTH);
	}

	@Override
	public boolean fermer() {
		return true;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Sprite.creerSprite(nom.getText(), dimension.getDimension(), fichiers.getImages());
	}
	
}
