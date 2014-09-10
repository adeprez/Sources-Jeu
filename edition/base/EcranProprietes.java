package base;

import interfaces.Actualisable;

import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import layouts.LayoutLignes;
import ressources.Proprietes;
import statique.Style;

import composants.DimensionGraphique;

import divers.Outil;

public class EcranProprietes extends Ecran implements Actualisable, ActionListener {
	private static final long serialVersionUID = 1L;
	private final JComboBox<String> police;
	private final JCheckBox admin, debug, d3d;
	private final DimensionGraphique taille;

	
	public EcranProprietes() {
		super("fond/parchemin.jpg");
		setName("Proprietes");
		setLayout(new LayoutLignes());
		Proprietes.getInstance().addActualiseListener(this);
		police = new JComboBox<String>(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
		admin = new JCheckBox();
		debug = new JCheckBox();
		d3d = new JCheckBox();
		taille = new DimensionGraphique(new Actualisable() {
			@Override public void actualise() {
				Proprietes.getInstance().setTaille(taille.getTaille());
			}});
		actualise();
		init();
	}
	
	private void init() {
		police.setFont(Style.POLICE);
		police.addActionListener(this);
		admin.addActionListener(this);
		debug.addActionListener(this);
		d3d.addActionListener(this);
		add(Outil.creerPanel("Administrateur", admin));
		add(Outil.creerPanel("Mode debug", debug));
		add(Outil.creerPanel("3D", d3d));
		add(Outil.creerPanel("Police", police));
		add(Outil.creerPanel("Taille initiale", taille));
	}

	@Override
	public void actualise() {
		Proprietes.getInstance().setIgnoreEnregistrement(true);
		police.setSelectedItem(Proprietes.getInstance().getPolice());
		admin.setSelected(Proprietes.getInstance().estAdmin());
		debug.setSelected(Proprietes.getInstance().estDebug());
		d3d.setSelected(Proprietes.getInstance().est3D());
		taille.setTaille(Proprietes.getInstance().getTaille());
		Proprietes.getInstance().setIgnoreEnregistrement(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == admin)
			Proprietes.getInstance().setAdmin(admin.isSelected());
		else if(e.getSource() == debug)
			Proprietes.getInstance().setDebug(debug.isSelected());
		else if(e.getSource() == d3d)
			Proprietes.getInstance().set3D(d3d.isSelected());
		else if(e.getSource() == police)
			Proprietes.getInstance().setPolice((String) police.getSelectedItem());
	}

}
