package territoire;

import interfaces.Actualisable;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import listeners.PositionSourisListener;
import listeners.SourisListener;
import ressources.compte.Compte;
import vision.ReticuleSelection;
import base.Ecran;
import carte.Territoire;
import carte.element.CaseTerritoire;
import exceptions.ExceptionJeu;
import exceptions.HorsLimiteException;

public class EcranGestionMonde extends Ecran implements Actualisable, PositionSourisListener {
	private static final long serialVersionUID = 1L;
	private final ReticuleSelection reticule;
	private final EditionMonde edition;
	private final MouseAdapter l;
	private final Compte compte;
	private Territoire territoire;


	public EcranGestionMonde(Compte compte) {
		super(new BorderLayout());
		setName("Territoire de " + compte.getNom());
		this.compte = compte;
		edition = new EditionMonde(compte, this);
		edition.addActualisable(this);
		reticule = new ReticuleSelection();
		l = new SourisListener(reticule, this);
		add(edition, BorderLayout.SOUTH);
		actualise();
	}

	public void ferme() {
		remove(territoire);
		territoire.removeMouseMotionListener(l);
		territoire.removeMouseListener(l);
		territoire.getTerrain().removeDessinable(reticule);
		removeMouseMotionListener(l);
		territoire.fermer();
		edition.removeActualisable(this);
	}

	@Override
	public boolean fermer() {
		ferme();
		removeMouseMotionListener(l);
		removeMouseListener(l);
		return super.fermer();
	}

	@Override
	public void actualise() {
		if(territoire != null)
			ferme();
		territoire = compte.getTerritoire();
		reticule.setCamera(territoire.getCamera());
		territoire.getTerrain().ajoutDessinable(reticule);
		territoire.addMouseMotionListener(l);
		territoire.addMouseListener(l);
		add(territoire);
		territoire.ouvrir();
		validate();
		repaint();
	}

	@Override
	public void clique(MouseEvent e) {
		if(e.getClickCount() > 1) {
			territoire.getCamera().getTaille().setMax();
			CaseTerritoire c;
			try {
				c = territoire.getTerrain().getObjet(e, territoire.getCamera());
				if(c != null) {
					territoire.getCamera().setX(c.getPosX());
					territoire.getCamera().setY(c.getPosY());
				}
			} catch(ExceptionJeu err) {}
		}
		else try {
			edition.setSelection(territoire.getTerrain().getObjet(e, territoire.getCamera()));
		} catch(HorsLimiteException e1) {
			edition.deselection();
		}
	}

	@Override
	public void deplace(int dx, int dy) {
		reticule.deplace(dx, dy);
	}


}
