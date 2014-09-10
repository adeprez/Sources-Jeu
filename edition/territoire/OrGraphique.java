package territoire;

import interfaces.Actualisable;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.JTextComponent;

import ressources.compte.ManqueRessourceException;
import ressources.compte.Or;
import statique.Style;

import composants.panel.PanelImage;

import divers.Outil;
import divers.Taille;

public class OrGraphique extends PanelImage implements Actualisable, ChangeListener, LayoutManager {
	private static final long serialVersionUID = 1L;
	private final JComponent val;
	private final Or or;
	
	
	public OrGraphique(Or ressources) {
		this(ressources, false);
	}
	
	public OrGraphique(Or or, boolean editable) {
		super("divers/or.png");
		setLayout(this);
		this.or = or;
		setToolTipText("Quantite d'Or");
		tailleImage();
		if(editable) {
			val = new JSpinner(new SpinnerNumberModel(or.get(), 0, Integer.MAX_VALUE, 5));
			((JSpinner) val).addChangeListener(this);
		} else {
			val = new JTextField();
			((JTextComponent) val).setEditable(false);
		}
		val.setToolTipText("Quantite d'Or");
		val.setFont(Style.POLICE);
		or.addActualiseListener(this);
		add(val);
		actualise();
	}
	
	public OrGraphique() {
		this(new Or());
	}
	
	public Or getOr() {
		return or;
	}
	
	public void setCouleur(Color c) {
		val.setForeground(c);
	}

	@Override
	public void actualise() {
		if(val instanceof JTextField)
			((JTextComponent) val).setText(or.get() + "");
		else ((JSpinner) val).setValue(or.get());
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		try {
			or.set((Integer) ((JSpinner) val).getValue());
		} catch(ManqueRessourceException err) {
			Outil.erreur(err.getMessage());
		}
	}

	@Override public void addLayoutComponent(String name, Component comp) {}
	@Override public void removeLayoutComponent(Component comp) {}

	@Override
	public void layoutContainer(Container parent) {
		val.setBounds((int) (getWidth()/3.2), getHeight()/9, (int) (getWidth() - getWidth()/3.3), getHeight() - getHeight()/6);
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return new Taille().getDimension();
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return new Taille(getImage().getWidth(), getImage().getHeight()).getDimension();
	}

	
	
}
