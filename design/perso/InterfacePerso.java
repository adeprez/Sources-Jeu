package perso;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import layouts.LayoutLignes;
import vision.Orientation;

import composants.PointCouleur;
import composants.panel.PanelImage;
import composants.styles.ScrollPaneTransparent;

import divers.Outil;


public class InterfacePerso extends PanelImage implements ActionListener {
	private static final long serialVersionUID = 1L;
	private final JComboBox<String> orientation;
	private final boolean editable;
	private final JComponent autres;
	private final Perso perso;


	public InterfacePerso(boolean editable, Perso perso) {
		super("fond/parchemin.jpg");
		this.perso = perso;
		this.editable = editable;
		setLayout(new BorderLayout());
		autres = new JPanel(new LayoutLignes());
		if(editable) {
			orientation = new JComboBox<String>(Orientation.noms());
			orientation.addActionListener(this);
		} else orientation = null;
		Component c = new ScrollPaneTransparent(autres);
		c.setMinimumSize(new Dimension(200, 200));
		JSplitPane jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		jsp.setDividerSize(4);
		jsp.setLeftComponent(new PanelImage(perso.getImage()).tailleImage());
		jsp.setRightComponent(c);
		add(Outil.getTexte(perso.getNom(), true), BorderLayout.NORTH);
		add(jsp, BorderLayout.CENTER);
		actualise();
	}

	private void actualise() {
		autres.removeAll();
		autres.add(Outil.creerPanel("Largeur", perso.getLargeur() + "cm"));
		autres.add(Outil.creerPanel("Hauteur", perso.getLargeur() + "cm"));
		autres.add(Outil.creerPanel("Coordonnees", "{" + perso.getX() + ", " + perso.getY() + "}"));
		autres.add(Outil.creerPanel("Couleur", new PointCouleur(perso.getCouleur(), 18, true)));
		if(editable) {
			orientation.setSelectedItem(perso.getForme().getOrientation().getNom());
			orientation.setSelectedIndex(perso.getForme().getOrientation().getID());
		} else autres.add(Outil.creerPanel("Orientation", perso.getForme().getOrientation().getNom()));
		if(editable) {
			autres.add(Outil.creerPanel("Orientation", orientation));
			autres.add(Box.createRigidArea(new Dimension(20, 20)));
		} else autres.add(Outil.creerPanel("Orientation", perso.getForme().getOrientation().getNom()));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == orientation)
			perso.getForme().setOrientation(Orientation.get(orientation.getSelectedItem().toString()));
	}

}
