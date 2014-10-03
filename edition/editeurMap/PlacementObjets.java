package editeurMap;

import java.awt.Component;
import java.awt.event.KeyEvent;

import listeners.SelectionListener;
import listeners.SelectionObjetListener;
import map.AbstractMap;
import map.Map;
import map.objets.Objet;

import composants.panel.PanelImage;

import controles.ToucheListener;
import divers.Outil;
import exceptions.AnnulationException;


public class PlacementObjets extends PanelImage implements SelectionObjetListener, SelectionListener, ToucheListener {
    private static final long serialVersionUID = 1L;
    private final EcranEditeurMap editeur;
    private final Map map;
    private Objet objet, selection;


    public PlacementObjets(EcranEditeurMap editeur, Map map, Component... cpt) {
	this.editeur = editeur;
	this.map = map;
	for(final Component c : cpt)
	    add(c);
    }

    public Objet placer(int x, int y, boolean cliqueDroit) throws AnnulationException {
	if(!cliqueDroit && objet != null) try {
	    Objet o = objet.dupliquer();
	    if(map.aObjet(x, y))
		map.supprimeObjet(x, y);
	    o.setCoordMap(x, y);
	    return o;
	} catch(Exception e) {
	    if(e.getMessage() != null)
		Outil.erreur(e.getMessage());
	}
	throw new AnnulationException();
    }

    public void setSelection(Objet objet, boolean afficheInterface) {
	objet.setSelectionne(true);
	if(selection != null)
	    selection.setSelectionne(false);
	selection = objet;
	editeur.setInterface(selection, afficheInterface);
    }

    @Override
    public void selection(Objet o, boolean cliqueDroit) {
	if(cliqueDroit)
	    setSelection(o, cliqueDroit);
	else try {
	    setSelection(placer(AbstractMap.checkX(o.getX()), AbstractMap.checkY(o.getY()), cliqueDroit), cliqueDroit);
	} catch(AnnulationException e) {}
    }

    @Override
    public boolean selection(int x, int y, boolean cliqueDroit) {
	try {
	    setSelection(placer(x, y, cliqueDroit), cliqueDroit);
	} catch(Exception e) {
	    if(e.getMessage() != null)
		Outil.erreur(e.getMessage());
	    return false;
	}
	return true;
    }

    @Override
    public void appuie(int key) {
	if(selection != null) switch(key) {
	case KeyEvent.VK_DELETE:
	    try {
		map.supprimeObjet(selection);
		selection.setSelectionne(false);
	    } catch(Exception e) {
		e.printStackTrace();
	    }
	    break;
	case KeyEvent.VK_Q:
	case KeyEvent.VK_LEFT:
	    selection(selection.getXMap() - 1, selection.getYMap(), false);
	    break;
	case KeyEvent.VK_D:
	case KeyEvent.VK_RIGHT:
	    selection(selection.getXMap() + 1, selection.getYMap(), false);
	    break;
	case KeyEvent.VK_Z:
	case KeyEvent.VK_UP:
	    selection(selection.getXMap(), selection.getYMap() + 1, false);
	    break;
	case KeyEvent.VK_S:
	case KeyEvent.VK_DOWN:
	    selection(selection.getXMap(), selection.getYMap() - 1, false);
	    break;
	}
    }

    public void setObjet(Objet objet) {
	this.objet = objet;
    }

}
