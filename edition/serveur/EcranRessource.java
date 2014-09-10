package serveur;

import interfaces.Actualisable;
import interfaces.ImageListener;
import interfaces.Localise;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import map.MapIO;
import map.objets.Objet;
import perso.InterfacePerso;
import perso.Perso;
import reseau.ressources.RessourceImage;
import reseau.ressources.RessourceImageObjet;
import reseau.ressources.RessourceMap;
import reseau.ressources.RessourcePerso;
import reseau.ressources.RessourceReseau;
import reseau.ressources.RessourcesReseau;
import reseau.ressources.TypeRessource;
import ressources.compte.Compte;
import vision.Camera;
import base.Ecran;

import composants.SelecteurImage;
import composants.panel.PanelImage;
import composants.styles.Bouton;
import composants.styles.ScrollPaneTransparent;
import composants.styles.fenetres.FenetrePopup;

import divers.Listenable;
import divers.Outil;
import divers.Taille;
import ecrans.ContainerMap;
import exceptions.AnnulationException;

public class EcranRessource<E> extends Ecran implements ListCellRenderer<RessourceReseau<E>>, Actualisable, ActionListener {
	private static final long serialVersionUID = 1L;
	private final JButton ajouter, supprimer, modifier;
	private final JList<RessourceReseau<E>> liste;
	private final RessourcesReseau ressources;
	private final TypeRessource type;


	public EcranRessource(RessourcesReseau ressources, TypeRessource type) {
		super("fond/parchemin.jpg");
		setName(type.getNom());
		setLayout(new BorderLayout(0, 15));
		this.ressources = ressources;
		this.type = type;
		liste = new JList<RessourceReseau<E>>(new Modele(type));
		liste.setCellRenderer(this);
		liste.setOpaque(false);
		add(new ScrollPaneTransparent(liste));
		JPanel haut = new JPanel(new GridLayout());
		haut.setOpaque(false);
		haut.add(ajouter = new Bouton("Ajouter...").large());
		haut.add(modifier = new Bouton("Modifier...").large());
		haut.add(supprimer = new Bouton("Supprimer").large());
		add(haut, BorderLayout.NORTH);
		ajouter.addActionListener(this);
		modifier.addActionListener(this);
		supprimer.addActionListener(this);
		ressources.addActualiseListener(this);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends RessourceReseau<E>> list, 
			RessourceReseau<E> value, int index, boolean isSelected, boolean cellHasFocus) {
		return rendu(value, isSelected);
	}

	@Override
	public void actualise() {
		ListDataEvent e = new ListDataEvent(liste, ListDataEvent.CONTENTS_CHANGED, 0, ressources.getListe(type).size());
		for(final ListDataListener l : ((Listenable) liste.getModel()).getListeners(ListDataListener.class))
			l.contentsChanged(e);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == ajouter)
			ajout();
		else if(e.getSource() == modifier)
			modif();
		else if(e.getSource() == supprimer)
			suppr();
		validate();
		repaint();
	}

	public void suppr() {
		if(liste.getSelectedValue() != null) {
			ressources.removeRessource(liste.getSelectedValue());
		}
	}

	public void modif() {
		if(liste.getSelectedValue() != null) {
			try {
				RessourceReseau<E> r = liste.getSelectedValue();
				switch(r.getType()) {
				case MAP:
					ressources.putRessource(new RessourceMap(r.getID(), MapIO.demanderMap()));
					break;
				case IMAGE:
					ressources.putRessource(new RessourceImage(r.getID(), getImage(((RessourceImage) r).getImage())));
					break;
				case IMAGE_OBJET:
					ressources.putRessource(new RessourceImageObjet(r.getID(), getImage(((RessourceImage) r).getImage())));
					break;
				case PERSO:
					ressources.putRessource(new RessourcePerso(r.getID(), getPerso()));
					break;
				default: throw new IllegalAccessError("Non implemente");
				}
			} catch(AnnulationException e) {}
		}
	}

	public void ajout() {
		try {
			switch(type) {
			case MAP:
				ressources.putRessource(new RessourceMap(ressources.getNextID(type), MapIO.demanderMap()));
				break;
			case IMAGE:
				ressources.putRessource(new RessourceImage(ressources.getNextID(type), getImage(null)));
				break;
			case IMAGE_OBJET:
				ressources.putRessource(new RessourceImageObjet(ressources.getNextID(type), getImage(null)));
				break;
			case PERSO:
				ressources.putRessource(new RessourcePerso(ressources.getNextID(type), getPerso()));
				break;
			default: throw new IllegalAccessError("Non implemente");
			}
		} catch(AnnulationException e) {}
	}

	public static Perso getPerso() throws AnnulationException {
		List<Perso> lp = new ArrayList<Perso>();
		for(final Compte c : Compte.getComptes())
			for(final Perso p : c.getPersos())
				lp.add(p);
		int i = JOptionPane.showOptionDialog(null, "Choisissez un personnage", "Choix", 
				JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, 
				lp.isEmpty() ? null : new ImageIcon(lp.get(0).getImage()), lp.toArray(), null);
		if(i == -1)
			throw new AnnulationException();
		return lp.get(i);
	}

	public static BufferedImage getImage(BufferedImage image) throws AnnulationException {
		SelecteurImage img = new SelecteurImage(0);
		img.setImage(image);
		final FenetrePopup p = new FenetrePopup("Choisir une image", img);
		img.addImageListener(new ImageListener() {
			@Override public void change(int id, BufferedImage image) {
				p.dispose();
			}});
		p.setResizable(false);
		p.afficher(new Dimension(300, 300));
		return img.getImage();
	}

	public static Component rendu(RessourceReseau<?> value, boolean isSelected) {
		JComponent p;
		if(value == null) {
			p = Outil.getTexte("(Vide)", false);
			return p;
		}
		switch(value.getType()) {
		case MAP:
			Camera cam = new Camera(new Taille(25));
			try {
				cam.setX(((((RessourceMap) value).getMap().getObjets().size() - 2) * Localise.UNITE.width)/2);
				cam.setY((int) (Localise.UNITE.height * 1.1));
			} catch(Exception err) {
				err.printStackTrace();
			}
			p = new ContainerMap<Objet>(((RessourceMap) value).getMap(), cam);
			p.setPreferredSize(new Dimension(111, 111));
			break;
		case IMAGE:
		case IMAGE_OBJET:
			p = new PanelImage(((RessourceImage) value).getImage());
			p.setPreferredSize(new Dimension(256, 256));
			break;
		case PERSO:
			p = new InterfacePerso(false, ((RessourcePerso) value).getPerso());
			break;
		default: 
			p = Outil.getTexte(value + "", false);
			break;
		}
		if(isSelected)
			p.setBorder(Outil.getBordure("id=" + value.getID()));
		return p;
	}

	private class Modele extends Listenable implements ListModel<RessourceReseau<E>> {
		private final TypeRessource type;


		public Modele(TypeRessource type) {
			this.type = type;
		}

		@Override
		public void addListDataListener(ListDataListener l) {
			addListener(ListDataListener.class, l);
		}

		@SuppressWarnings("unchecked")
		@Override
		public RessourceReseau<E> getElementAt(int index) {
			return (RessourceReseau<E>) ressources.getRessource(type, index);
		}

		@Override
		public int getSize() {
			return ressources.getMaxID(type) + 1;
		}

		@Override
		public void removeListDataListener(ListDataListener l) {
			removeListener(ListDataListener.class, l);
		}

	}

}
