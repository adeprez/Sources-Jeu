package composants.styles.fenetres;

import interfaces.Fermable;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

import base.Fenetre;
import bordures.BordureImage;
import divers.Outil;
import exceptions.AnnulationException;

public class FenetreSaisie<E extends Component> extends FenetrePopup implements Fermable, KeyListener {
	private static final long serialVersionUID = 1L;
	private static final int FERMETURE = KeyEvent.VK_ESCAPE;
	private E c;


	public FenetreSaisie(String nom, E c) {
		super("");
		this.c = c;
		setUndecorated(true);
		init(nom);
		setSize(200 + nom.length() * 8, 75);
		centrer(Fenetre.getInstance());
		Fenetre.getInstance().getGlassPane().setSombre(true);
		c.addKeyListener(this);
		addKeyListener(this);
	}

	private void init(String nom) {
		JPanel p = new JPanel(new GridLayout(3, 1));
		p.setBorder(new BordureImage("trait enfonce.png"));
		p.setBackground(Color.DARK_GRAY);
		p.add(Outil.getTexte(nom, true, Color.WHITE));
		p.add(c);
		p.add(Outil.getTexte("(" + KeyEvent.getKeyText(FERMETURE) + " pour quitter)", false, Color.GRAY));
		setContenu(p);
	}

	public E get() {
		return c;
	}

	public E demander() throws AnnulationException {
		afficher();
		if(c == null)
			throw new AnnulationException();
		return c;
	}

	@Override
	public boolean fermer() {
		removeKeyListener(this);
		Fenetre.getInstance().getGlassPane().setSombre(false);
		dispose();
		return true;
	}

	@Override
	public void setVisible(boolean isVisible) {
		if(isVisible)
			super.setVisible(true);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == FERMETURE) {
			c = null;
			fermer();
		}
	}

	@Override public void keyReleased(KeyEvent e) {}
	@Override public void keyTyped(KeyEvent e) {}

}
