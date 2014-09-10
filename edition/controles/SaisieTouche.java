package controles;

import interfaces.Fermable;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

import listeners.ChangeControleListener;
import base.Fenetre;
import bordures.BordureImage;

import composants.styles.fenetres.FenetrePopup;

import divers.Outil;

public class SaisieTouche extends FenetrePopup implements Fermable, KeyListener {
	private static final long serialVersionUID = 1L;
	private static final int FERMETURE = KeyEvent.VK_ESCAPE;
	private final ChangeControleListener l;
	private final byte id;
	

	public SaisieTouche(ChangeControleListener l, byte id, String nom) {
		super("");
		this.id = id;
		this.l = l;
		setUndecorated(true);
		init(nom);
		setSize(250, 75);
		centrer(Fenetre.getInstance());
		Fenetre.getInstance().getGlassPane().setSombre(true);
		addKeyListener(this);
	}
	
	private void init(String nom) {
		JPanel p = new JPanel(new GridLayout(3, 1));
		p.setBorder(new BordureImage("trait enfonce.png"));
		p.setBackground(Color.DARK_GRAY);
		p.add(Outil.getTexte(nom, true, Color.WHITE));
		p.add(Outil.getTexte("Appuyez sur une touche", false, Color.YELLOW));
		p.add(Outil.getTexte("(" + KeyEvent.getKeyText(FERMETURE) + " pour quitter)", false, Color.GRAY));
		setContenu(p);
	}

	@Override
	public boolean fermer() {
		removeKeyListener(this);
		Fenetre.getInstance().getGlassPane().setSombre(false);
		dispose();
		return true;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == FERMETURE || l.change(id, e.getKeyCode()))
			fermer();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

}
