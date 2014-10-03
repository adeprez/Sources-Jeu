package objets;

import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import map.objets.Destructible;
import physique.forme.Forme;
import physique.forme.FormeVide;
import physique.forme.Rect;
import physique.forme.Triangle;
import physique.forme.TypeForme;
import divers.Outil;

public class InterfaceDestructible implements ChangeListener, ActionListener {
    private final JSpinner resistance, degats;
    private final Destructible objet;
    private final JComboBox<String> forme;


    public InterfaceDestructible(Destructible objet, Container c) {
	this.objet = objet;
	resistance = new JSpinner(new SpinnerNumberModel(objet.getVitalite(), 0, 100, 1));
	degats = new JSpinner(new SpinnerNumberModel(objet.getVie(), 0, objet.getVitalite(), 1));
	forme = new JComboBox<String>(Outil.toStringArray(TypeForme.values()));
	forme.setSelectedIndex(objet.getForme().getType().ordinal());
	forme.addActionListener(this);
	resistance.addChangeListener(this);
	degats.addChangeListener(this);
	c.add(Outil.creerPanel("Resistance", resistance));
	c.add(Outil.creerPanel("Degats", degats));
	c.add(Outil.creerPanel("Forme", forme));
    }

    @Override
    public void stateChanged(ChangeEvent e) {
	if(e.getSource() == resistance)
	    objet.setResistance(((Integer) resistance.getValue()).byteValue());
	else objet.setVie(objet.getVitalite() - (Integer) degats.getValue());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	Forme f = null;
	switch(TypeForme.values()[forme.getSelectedIndex()]) {
	case RECTANGLE:
	    f = new Rect(new Rectangle(objet.getX(), objet.getY(), objet.getLargeur(), objet.getHauteur()), objet.getForme().getOrientation());
	    break;
	case TRIANGLE:
	    f = new Triangle(objet.getForme().getOrientation(), true, new Rectangle(objet.getX(), objet.getY(), objet.getLargeur(), objet.getHauteur()));
	    break;
	default:
	    f = new FormeVide(objet.getX(), objet.getY());
	    break;
	}
	objet.setForme(f);
    }

}
