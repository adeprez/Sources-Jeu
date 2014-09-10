package carte;

import interfaces.LocaliseEquipe;

import java.awt.Graphics;
import java.util.List;

import javax.swing.JComponent;

import listeners.ControleMapListener;
import map.Map;
import physique.forme.FormeVide;
import vision.Camera;
import vision.Redessinement;
import carte.element.Lieu;
import carte.ui.PanelControleCarte;
import controles.TypeAction;
import divers.Outil;
import divers.Taille;
import exceptions.ExceptionJeu;
import exceptions.HorsLimiteException;

public class Carte extends JComponent implements ControleMapListener, Runnable {
	private static final long serialVersionUID = 1L;
	private static final int VITESSE = 50;
	private final PanelControleCarte panel;
	private final LocaliseEquipe point;
	private final Redessinement dessin;
	private final List<Lieu> lieux;
	private final Terrain terrain;
	private final Camera camera;
	private TypeAction direction;
	private boolean run;


	public Carte(Terrain terrain, List<Lieu> lieux) {
		setLayout(null);
		this.lieux = lieux;		
		this.terrain = terrain;
		point = new FormeVide();
		camera = new Camera(new Taille(16, .5).setLargeurMin(4).setLargeurMax(50), point, this);
		try {
			int i = Map.convertY(terrain.getAltitude(0)/2);
			point.setPos(i, i);
		} catch(Exception e1) {
			e1.printStackTrace();
		}
		dessin = new Redessinement(this);
		panel = new PanelControleCarte(this, camera.getTaille());
		for(final Lieu l : lieux) try {
			terrain.getObjet(l.getX(), l.getY()).setLieu(l);
		} catch (HorsLimiteException e) {
			e.printStackTrace();
		}
		add(panel);
	}

	public Camera getCamera() {
		return camera;
	}

	public Terrain getTerrain() {
		return terrain;
	}

	public List<Lieu> getLieux() {
		return lieux;
	}

	public boolean ouvrir() {
		return dessin.lancer();
	}

	public boolean fermer() {
		return dessin.fermer();
	}

	public void setDirection(TypeAction a) {
		direction = a; 
		if(!run) {
			run = true;
			new Thread(this).start();
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		terrain.dessiner(g, camera);
	}

	@Override
	public void appuie(TypeAction action) {
		setDirection(action);
	}

	@Override
	public void relache(TypeAction action) {
		run = false;
	}

	@Override
	public void setZoom(int zoom) {
		camera.getTaille().setLargeur(zoom);
	}

	@Override
	public void run() {
		try {
			while(run) {
				if(direction == null) 
					run = false;
				else switch(direction) {
				case DROITE:
					point.setX(point.getX() + VITESSE);
					break;
				case GAUCHE:
					point.setX(point.getX() - VITESSE);
					break;
				case ACCROUPI:
					point.setY(point.getY() - VITESSE);
					break;
				case ESCALADER:
				case SAUT:
					point.setY(point.getY() + VITESSE);
					break;
				default:
					break;
				}
				Outil.wait(50);
			}
		} catch(ExceptionJeu e) {}
	}

}
