package animation;

import interfaces.Cliquable;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import listeners.CliqueListener;
import ressources.sprites.animation.Animation;
import base.Ecran;
import divers.Outil;

public class ApercuAnim extends Ecran implements Cliquable, MouseWheelListener, ChangeListener {
	private static final long serialVersionUID = 1L;
	private final Animation anim;
	private JSpinner vitesse;
	private boolean droite;
	private int taille;

	
	public ApercuAnim(Animation anim, boolean changeVitesse) {
		anim.setVitesse(100);
		setLayout(new FlowLayout());
		this.anim = anim;
		if(changeVitesse) {
			vitesse = new JSpinner(new SpinnerNumberModel(anim.getVitesse(), 1, Animation.PAS_ETAPE * 10, 100));
			vitesse.addChangeListener(this);
			add(Outil.getTexte("% Vitesse :", false));
			add(vitesse);
		}
		setTaille(50);
		addMouseListener(new CliqueListener(this));
		addMouseWheelListener(this);
	}
	
	public Animation getAnim() {
		return anim;
	}
	
	public void setTaille(int taille) {
		this.taille = Math.max(taille, 15);
		setPreferredSize(new Dimension(taille * 4, taille * 3));
		if(getParent() != null)
			getParent().doLayout();
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		anim.dessiner((Graphics2D) g, 
				new Rectangle((getWidth() - taille)/2, (getHeight() - taille)/2, taille, taille), droite);
	}

	@Override
	public void clique(MouseEvent e) {
		droite = !droite;
		repaint();
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		setTaille(taille + e.getUnitsToScroll());
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		anim.setVitesse((int) vitesse.getValue());
	}
	
}
