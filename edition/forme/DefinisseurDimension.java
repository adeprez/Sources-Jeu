package forme;

import interfaces.Localise;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import listeners.ChangeDimensionListener;
import physique.forme.Rect;
import statique.Style;
import vision.Orientation;

import composants.panel.PanelDessinable;

public class DefinisseurDimension extends JPanel implements ChangeListener {
	private static final long serialVersionUID = 1L;
	private final PanelDessinable panelDessin; 
	private final JSlider largeur, hauteur;
	private final JLabel txt;
	private final Rect rect;
	
	
	public DefinisseurDimension() {
		this(new Rect(Localise.UNITE.width, Localise.UNITE.height, Orientation.DROITE));
	}

	public DefinisseurDimension(Rect rect) {
		setLayout(new BorderLayout());
		this.rect = rect;
		setOpaque(false);
		largeur = new JSlider(SwingConstants.HORIZONTAL, 0, Localise.UNITE.width, 0);
		hauteur = new JSlider(SwingConstants.VERTICAL, 0, Localise.UNITE.width, 0);
		txt = new JLabel("", SwingConstants.CENTER);
		txt.setFont(Style.POLICE);
		largeur.setPreferredSize(new Dimension(75, 15));
		hauteur.setPreferredSize(new Dimension(15, 90));
		largeur.addChangeListener(this);
		hauteur.addChangeListener(this);
		panelDessin = new PanelDessinable(new DessineurForme(rect));
		actualise();
		add(largeur, BorderLayout.SOUTH);
		add(hauteur, BorderLayout.WEST);
		add(txt, BorderLayout.NORTH);
		add(panelDessin, BorderLayout.CENTER);
	}
	
	public void actualise() {
		largeur.setValue(rect.getLargeur());
		hauteur.setValue(rect.getHauteur());
		changeDim();
	}
	
	public void change() {
		changeDim();
		for(final ChangeDimensionListener l : listenerList.getListeners(ChangeDimensionListener.class))
			l.changeDimension(rect);
	}
	
	public void changeDim() {
		txt.setText(rect.getLargeur() + "x" + rect.getHauteur());
		panelDessin.repaint();
	}
	
	public Rect getRect() {
		return rect;
	}
	
	public void addChangeDimensionListener(ChangeDimensionListener l) {
		listenerList.add(ChangeDimensionListener.class, l);
	}
	
	public void removeChangeDimensionListener(ChangeDimensionListener l) {
		listenerList.remove(ChangeDimensionListener.class, l);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if(e.getSource() == largeur)
			rect.setLargeur(largeur.getValue());
		else rect.setHauteur(hauteur.getValue());
		change();
	}
	
}
