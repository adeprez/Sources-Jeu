package territoire;

import interfaces.Actualisable;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import layouts.LayoutLignes;
import ressources.compte.Compte;
import statique.Style;
import base.Ecran;
import carte.element.Lieu;

import composants.panel.PanelImage;
import composants.styles.Bouton;

import divers.Outil;
import editeurMap.EcranEditeurLieu;

public class GestionLieu extends Ecran implements Actualisable, ActionListener, KeyListener {
	private static final long serialVersionUID = 1L;
	private final AbstractButton map, detruire, save;
	private final JLabel taille, blocs, type;
	private final JTextComponent description;
	private final PanelImage img;
	private final Compte compte;
	private Lieu lieu;

	
	public GestionLieu(Compte compte) {
		this.compte = compte;
		setName("Gestion lieu");
		setLayout(new BorderLayout(15, 0));
		JPanel p = new JPanel(new LayoutLignes());
		map = new Bouton().large();
		detruire = new Bouton("Detruire").large();
		save = new Bouton("Sauvegarder la description").large();
		save.setFont(Style.POLICE.deriveFont(14f));
		save.addActionListener(this);
		map.addActionListener(this);
		detruire.addActionListener(this);
		taille = Outil.getTexte("", false);
		blocs = Outil.getTexte("", false);
		type = Outil.getTexte("", false);
		img = new PanelImage();
		img.setPreferredSize(new Dimension(100, 128));
		description = new JEditorPane();
		description.setFont(Style.POLICE);
		description.setBorder(BorderFactory.createLoweredBevelBorder());
		description.addKeyListener(this);
		p.setOpaque(false);
		p.add(Outil.creerPanel("Architecture", type));
		p.add(Outil.creerPanel("Surface", taille));
		p.add(Outil.creerPanel("Blocs disponibles", blocs));
		p.add(description);
		p.add(save);
		JPanel p2 = new JPanel(new GridLayout());
		p2.setOpaque(false);
		p2.add(map);
		p2.add(detruire);
		add(p, BorderLayout.CENTER);
		add(img, BorderLayout.WEST);
		add(p2, BorderLayout.SOUTH);
	}

	public GestionLieu setLieu(Lieu lieu) {
		this.lieu = lieu;
		actualise();
		return this;
	}

	@Override
	public void actualise() {
		type.setText(lieu.getType().getNom());
		taille.setText(lieu.getTaille() + "m");
		blocs.setText(lieu.getType().getNombreBlocs() + "");
		img.setImage(lieu.getType().getImage());
		map.setText(lieu.aMap(compte.path()) ? "~ Editer carte ~" : "~ Creer carte ~");
		description.setText(lieu.getDescription());
		save.setVisible(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == save) {
			lieu.setDescription(description.getText());
			lieu.sauvegardeInfos(compte);
			save.setVisible(false);
		}
		else if(e.getSource() == detruire) 
			lieu.detruire(compte);
		else if(e.getSource() == map)
			changer(new EcranEditeurLieu(compte, lieu));
	}

	@Override
	public void keyPressed(KeyEvent e) {
		save.setVisible(true);
	}

	@Override public void keyReleased(KeyEvent e) {}
	@Override public void keyTyped(KeyEvent e) {}

}
