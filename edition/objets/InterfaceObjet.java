package objets;

import interfaces.ImageListener;
import interfaces.Localise;
import interfaces.Sauvegardable;
import io.IO;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import layouts.LayoutLignes;
import listeners.ChangeObjetListener;
import listeners.RemoveListener;
import map.objets.Bloc;
import map.objets.Destructible;
import map.objets.Objet;
import map.objets.ObjetVide;
import map.objets.TypeObjet;
import physique.forme.Forme;
import ressources.SpriteObjets;
import vision.Orientation;

import composants.ChangeurImage;
import composants.SelecteurImage;
import composants.panel.PanelImage;
import composants.styles.Bouton;
import composants.styles.ScrollPaneTransparent;

import divers.Outil;
import exceptions.ExceptionJeu;


public class InterfaceObjet extends PanelImage implements Sauvegardable, ActionListener, ImageListener, ChangeObjetListener, ChangeListener {
	private static final long serialVersionUID = 1L;
	private final boolean editable;
	private final JComponent autres;
	private final PanelImage image;
	private final RemoveListener<Objet> l;
	private final JLabel nom;
	private JSpinner largeur, hauteur, x, y;
	private SelecteurImage select;
	private JComboBox<String> type, orientation;
	private AbstractButton suppr;
	private Objet objet;


	public InterfaceObjet(boolean editable, Objet objet, RemoveListener<Objet> l) {
		super("fond/parchemin.jpg");
		this.l = l;
		this.objet = objet;
		this.editable = editable;
		setLayout(new BorderLayout());
		add(nom = Outil.getTexte(objet.getNom(), false), BorderLayout.NORTH);
		JSplitPane jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		jsp.setDividerSize(4);
		autres = new JPanel(new LayoutLignes());
		if(editable) {
			select = new ChangeurImage(objet.getID());
			select.setOpaque(false);
			jsp.setLeftComponent(select);
			type = new JComboBox<String>(TypeObjet.noms());
			add(type, BorderLayout.SOUTH);
			orientation = new JComboBox<String>(Orientation.noms());
			suppr = new Bouton("Supprimer").large();
			largeur = new JSpinner(new SpinnerNumberModel(objet.getLargeur(), 0, Localise.UNITE.width * 2, 5));
			hauteur = new JSpinner(new SpinnerNumberModel(objet.getHauteur(), 0, Localise.UNITE.width * 2, 5));
			x = new JSpinner(new SpinnerNumberModel(objet.getX(), -Localise.UNITE.width, Integer.MAX_VALUE, 10));
			y = new JSpinner(new SpinnerNumberModel(objet.getY(), -Localise.UNITE.height, Integer.MAX_VALUE, 10));
			largeur.addChangeListener(this);
			hauteur.addChangeListener(this);
			x.addChangeListener(this);
			y.addChangeListener(this);
			type.addActionListener(this);
			select.addImageListener(this);
			suppr.addActionListener(this);
			orientation.addActionListener(this);
			image = null;
		} else {
			image = new PanelImage();
			jsp.setLeftComponent(image);
			add(Outil.getTexte("(" + objet.getType().getNom() + ")", false), BorderLayout.SOUTH);
		}
		Component c = new ScrollPaneTransparent(autres);
		c.setMinimumSize(new Dimension(200, 200));
		jsp.setRightComponent(c);
		add(jsp, BorderLayout.CENTER);
		actualise();
	}

	private void actualise() {
		objet.addChangeObjetListener(this);
		nom.setText(objet.getNom());
		autres.removeAll();
		if(editable) {
			try {
				select.setImage(objet.getImage());
			} catch(IndexOutOfBoundsException err) {}
			autres.add(Outil.creerPanel("Position X", x));
			autres.add(Outil.creerPanel("Position Y", y));
			autres.add(Outil.creerPanel("Largeur", largeur));
			autres.add(Outil.creerPanel("Hauteur", hauteur));
			orientation.setSelectedItem(objet.getForme().getOrientation().getNom());
			orientation.setSelectedIndex(objet.getForme().getOrientation().getID());
			largeur.setValue(objet.getLargeur());
			hauteur.setValue(objet.getHauteur());
		} else {
			autres.add(Outil.creerPanel("Coordonnees", "{" + objet.getX() + ", " + objet.getY() + "}"));
			autres.add(Outil.creerPanel("Largeur", objet.getLargeur() + "cm"));
			autres.add(Outil.creerPanel("Hauteur", objet.getHauteur() + "cm"));
			image.setImage(objet.getImage());
			image.tailleImage();
			add(Outil.getTexte("(" + objet.getType().getNom() + ")", false), BorderLayout.SOUTH);
		}
		objet.construireInterface(autres, editable);
		if(editable) {
			autres.add(Outil.creerPanel("Orientation", orientation));
			autres.add(Box.createRigidArea(new Dimension(20, 20)));
			autres.add(suppr);
			if(objet.getType().getID() != type.getSelectedIndex()) {
				type.removeActionListener(this);
				type.setSelectedIndex(objet.getType().getID());
				type.addActionListener(this);
			}
		} else {
			autres.add(Outil.creerPanel("Orientation", objet.getForme().getOrientation().getNom()));
		}
	}

	@Override
	public IO sauvegarder(IO io) {
		return objet.sauvegarder(io);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == type)
			objet.notifyChangeObjetListener(convertir(TypeObjet.get(type.getSelectedIndex())));
		else if(e.getSource() == suppr)
			l.remove(objet);
		else if(e.getSource() == orientation)
			objet.getForme().setOrientation(Orientation.get(orientation.getSelectedItem().toString()));
	}

	@Override
	public void change(int id, BufferedImage image) {
		SpriteObjets.getInstance().setImage(id, image);
	}

	@Override
	public void change(Objet ancien, Objet nouveau) {
		if(ancien == objet) {
			ancien.removeChangeObjetListener(this);
			objet = nouveau;
			actualise();
		}
	}

	private Objet convertir(TypeObjet typeObjet) {
		Forme f = objet.getForme();
		Objet o = creer(typeObjet);
		o.setForme(f);
		return o;
	}

	private Objet creer(TypeObjet typeObjet) {
		switch(typeObjet) {
		case VIDE: 
			return new ObjetVide(objet.getMap(), objet.getContaineurImages(), objet.getFond());
		case BLOC:
			int resistance = 10, degats = 0;
			if(objet instanceof Destructible) {
				resistance = ((Destructible) objet).getVitalite();
				degats = ((Destructible) objet).getVie();
			}
			return new Bloc(objet.getMap(), objet.getContaineurImages(), objet.getFond(), 
					objet.getForme().dupliquer(), objet.getID(), resistance, degats);
		default: throw new IllegalArgumentException(typeObjet + " n'a pas ete defini");
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if(e.getSource() == largeur) try {
			objet.setLargeur((Integer) largeur.getValue());
		} catch(Exception e2) {
			largeur.setValue(objet.getLargeur());
		} else if(e.getSource() == hauteur) try {
			objet.setHauteur((Integer) hauteur.getValue());
		} catch(Exception e2) {
			hauteur.setValue(objet.getHauteur());
		} else if(e.getSource() == x) try {
			objet.setX((Integer) x.getValue());
		} catch(ExceptionJeu e1) {
			x.setValue(objet.getX());
		} else if(e.getSource() == y) try {
			objet.setY((Integer) y.getValue());
		} catch(ExceptionJeu e1) {
			y.setValue(objet.getY());
		}
	}

}
