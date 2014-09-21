package reseau.client;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

import jeu.EcranJeu;
import listeners.ChangeSpecialiteListener;
import perso.AbstractPerso;
import perso.Perso;
import reseau.paquets.jeu.PaquetAction;
import specialite.Specialite;
import vision.Camera;
import controles.ControleActionListener;
import controles.TypeAction;

public class ControleOutReseau extends MouseAdapter implements ControleActionListener, ChangeSpecialiteListener {
	private final Set<TypeAction> actifs;
	private final Client client;
	private final Camera cam;


	public ControleOutReseau(Camera cam, Client client) {
		this.client = client;
		this.cam = cam;
		actifs = new HashSet<TypeAction>();
	}

	public void action(TypeAction action, boolean appuie) {
		client.write(PaquetAction.getPaquet(action, client.getPerso(), appuie));
	}

	public void setSens(MouseEvent e) {
		Component c = (Component) e.getSource();
		Perso p = client.getPerso();
		if(!p.enAction()) {
			boolean droite = c.getWidth()/2 < e.getX();
			if(droite != p.estDroite()) {
				p.setDroite(droite);
			}
		}
		p.setAngle(EcranJeu.getAngle(cam, p, e.getX(), e.getY(), p.estDroite()));
	}

	@Override
	public void appuie(TypeAction action) {
		actifs.add(action);
		action(action, true);
	}

	@Override
	public void relache(TypeAction action) {
		actifs.remove(action);
		if(action.aFin())
			action(action, false);
		if(!actifs.isEmpty())
			appuie(actifs.iterator().next());
	}

	@Override
	public void changeSpecialite(AbstractPerso perso, Specialite ancienne, Specialite nouvelle) {
		if(nouvelle != ancienne)
			client.write(PaquetAction.getPaquet(TypeAction.CHANGER_ARME, client.getPerso(), false)
					.addBytePositif(nouvelle.getType().ordinal()));
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		setSens(e);
		action(TypeAction.ATTAQUER, false);
		if(!actifs.isEmpty())
			appuie(actifs.iterator().next());
	}

}
