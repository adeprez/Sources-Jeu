package editeurMap;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import listeners.ChangeEtatListener;
import listeners.ChangeFormeListener;
import listeners.SelectionObjetListener;
import map.Map;
import map.objets.Bloc;
import map.objets.Objet;
import map.objets.ObjetVide;
import map.objets.TypeObjet;
import physique.forme.Forme;
import ressources.SpriteObjets;
import base.Ecran;
import divers.Outil;
import forme.DefinisseurForme;

public class CreateurObjets extends Ecran implements ActionListener, ChangeFormeListener, ChangeEtatListener {
	private static final long serialVersionUID = 1L;
	private final DefinisseurForme dforme;
	private final ChoixImageObjet image;
	private final JComboBox<String> type;
	private final Map map;
	private int resistance, degats;
	private Objet objet;
	private Forme forme;


	public CreateurObjets(Map map, SelectionObjetListener... l) {
		super("fond/parchemin.jpg");
		setName("Selection objets");
		this.map = map;
		setLayout(new BorderLayout(15, 15));
		type = new JComboBox<String>(TypeObjet.noms());
		type.setSelectedIndex(1);
		type.addActionListener(this);
		setLayout(new BorderLayout());
		add(image = new ChoixImageObjet(), BorderLayout.SOUTH);
		add(type, BorderLayout.NORTH);
		image.setBorder(Outil.getBordure("Image"));
		image.addChangeEtatListener(this);
		dforme = new DefinisseurForme();
		add(dforme, BorderLayout.CENTER);
		dforme.addChangeChangeFormeListener(this);
		for(final SelectionObjetListener ll : l)
			addSelectionObjetListener(ll);
		dforme.setForme(dforme.getDefDim().getRect().dupliquer());
	}
	
	public void addSelectionObjetListener(SelectionObjetListener l) {
		listenerList.add(SelectionObjetListener.class, l);
	}
	
	public void removeSelectionObjetListener(SelectionObjetListener l) {
		listenerList.remove(SelectionObjetListener.class, l);
	}
	
	public void notifySelectionObjetListener() {
		for(final SelectionObjetListener l : listenerList.getListeners(SelectionObjetListener.class))
			l.selection(objet, true);
	}
	
	public void change() {
		switch(TypeObjet.values()[type.getSelectedIndex()]) {
		case VIDE:
			objet = new ObjetVide(map, SpriteObjets.getInstance(), image.getIdFond());
			break;
		case BLOC:
			objet = new Bloc(map, SpriteObjets.getInstance(), image.getIdFond(), forme, image.getIdImage(), resistance, degats);
			break;
		default:
			throw new IllegalAccessError("Erreur : objet non trouve");
		}
		notifySelectionObjetListener();
	}
	
	public void changeType() {
		change();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		changeType();
	}

	@Override
	public void change(Forme forme) {
		this.forme = forme;
		change();
	}

	@Override
	public void changeEtat(boolean etat) {
		dforme.setDecoupe(etat);
		change();
	}


}
