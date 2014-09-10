package forme;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import listeners.ChangeDimensionListener;
import listeners.ChangeFormeListener;
import physique.forme.Forme;
import physique.forme.FormeVide;
import physique.forme.Rect;
import physique.forme.Triangle;
import physique.forme.TypeForme;
import vision.Orientation;
import base.Ecran;
import divers.Outil;

public class DefinisseurForme extends Ecran implements ActionListener, ChangeDimensionListener {
	private static final long serialVersionUID = 1L;
	private final DefinisseurDimension d;
	private final JComboBox<String> type, orientation;
	private boolean decoupe;

	
	public DefinisseurForme() {
		setName("Definition forme");
		setLayout(new BorderLayout());
		type = new JComboBox<String>(Outil.toStringArray(TypeForme.values()));
		orientation = new JComboBox<String>(Orientation.noms());
		d = new DefinisseurDimension();
		d.addChangeDimensionListener(this);
		orientation.addActionListener(this);
		add(type, BorderLayout.NORTH);
		add(orientation, BorderLayout.SOUTH);
		add(d, BorderLayout.CENTER);
		type.addActionListener(this);
	}
	
	public void addChangeChangeFormeListener(ChangeFormeListener l) {
		listenerList.add(ChangeFormeListener.class, l);
	}
	
	public void removeChangeChangeFormeListener(ChangeFormeListener l) {
		listenerList.remove(ChangeFormeListener.class, l);
	}
	
	public DefinisseurDimension getDefDim() {
		return d;
	}
	
	public void change() {
		Forme forme = getForme();
		forme.setOrientation(Orientation.get(orientation.getSelectedIndex()));
		for(final ChangeFormeListener l : listenerList.getListeners(ChangeFormeListener.class))
			l.change(forme);
	}
	
	public Forme getForme() {
		switch(TypeForme.values()[type.getSelectedIndex()]) {
		case RECTANGLE: return d.getRect().dupliquer();
		case VIDE: return new FormeVide();
		case TRIANGLE: return new Triangle(new Rectangle(d.getRect().getDimension()), decoupe);
		default: throw new IllegalAccessError();
		}
	}

	public void setForme(Forme forme) {
		type.setSelectedIndex(forme.getType().ordinal());
	}

	public void setDecoupe(boolean decoupe) {
		this.decoupe = decoupe;
		change();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		change();
	}

	@Override
	public void changeDimension(Rect taille) {
		change();
	}
	
}
