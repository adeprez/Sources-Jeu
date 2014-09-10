package editeurMap;

import interfaces.Localise;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.beans.PropertyVetoException;
import java.util.List;

import javax.swing.JInternalFrame;

import listeners.SelectionObjetListener;
import map.DessineurElementsMap3D;
import map.Map;
import map.objets.Objet;
import objets.InterfaceObjet;
import vision.Camera;
import vision.ReticuleObjet;
import base.Ecran;
import base.Fenetre;

import composants.FenetreInterne;

import controles.ToucheClavier;
import ecrans.ContainerMap;

public class EcranEditeurMap extends Ecran implements SelectionObjetListener {
    private static final long serialVersionUID = 1L;
    private final JInternalFrame interfaceObjet;
    private final ContainerMap<Objet> ecranMap;
    private final PlacementObjets objets;
    private final ToucheClavier ctrl;
    private final ReticuleObjet r;
    private final Map map;


    public EcranEditeurMap(int taille, Component haut) {
	this(new Map(taille, null), haut);
    }

    public EcranEditeurMap(Map map, Component... haut) {
	this.map = map;
	map.setDessineur(new DessineurElementsMap3D<Objet>());
	for(final List<Objet> lo : map.getObjets())
	    for(Objet o : lo)
		o.setOpaque(false);
	setName("Editeur de map");
	setLayout(new BorderLayout());
	Camera cam = new Camera();
	ecranMap = new ContainerMap<Objet>(map, cam);
	ecranMap.setReticule(r = new ReticuleObjet(map, cam));
	add(ecranMap, BorderLayout.CENTER);
	ecranMap.add(objets = new PlacementObjets(this, map, haut));
	objets.setBounds(0, 0, 80, 80);
	r.addSelectionObjetListener(objets);
	r.addSelectionListener(objets);
	interfaceObjet = new FenetreInterne();
	ecranMap.add(interfaceObjet);
	add(new CreateurObjets(map, r, this), BorderLayout.EAST);
	cam.setX(Localise.UNITE.width * (map.getObjets().size()/2));
	interfaceObjet.setFocusable(false);
	ctrl = new ToucheClavier(objets);
	ecranMap.setFocusable(true);
    }

    public void setInterface(Objet objet, boolean affiche) {
	setInterface(new InterfaceObjet(true, objet, map), affiche).setName(objet.getNom());
    }

    public JInternalFrame setInterface(Container c, boolean affiche) {
	interfaceObjet.setContentPane(c);
	if(affiche && !interfaceObjet.isVisible()) {
	    interfaceObjet.setBounds(0, 0, 400, 260);
	    try {
		interfaceObjet.setIcon(true);
	    } catch(PropertyVetoException e) {}
	    interfaceObjet.setVisible(true);
	}
	return interfaceObjet;
    }

    @Override
    public boolean fermer() {
	getFenetre().removeKeyListener(ctrl);
	ecranMap.fermer();
	return super.fermer();
    }

    @Override
    public void afficher(Fenetre fenetre) {
	super.afficher(fenetre);
	fenetre.addKeyListener(ctrl);
    }

    @Override
    public void selection(Objet objet, boolean cliqueDroit) {
	objets.setObjet(objet);
	ecranMap.requestFocus();
    }

}
