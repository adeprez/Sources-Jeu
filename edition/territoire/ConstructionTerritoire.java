package territoire;

import interfaces.Actualisable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import layouts.LayoutLignes;
import ressources.compte.ManqueRessourceException;
import ressources.compte.Or;
import statique.Style;
import carte.element.CaseTerritoire;
import carte.element.Lieu;
import carte.element.TypeLieu;

import composants.panel.PanelImage;
import composants.styles.Bouton;

import divers.Outil;

public class ConstructionTerritoire extends JPanel implements ActionListener, Actualisable, ChangeListener {
	private static final long serialVersionUID = 1L;
	private final JSlider taille;
	private final JComboBox<String> type;
	private final PanelImage img;
	private final OrGraphique or;
	private final Bouton acheter;
	private final JLabel tTaille, blocs;
	private final Or r;
	private CaseTerritoire objet;

	
	public ConstructionTerritoire(Or r, ActionListener l) {
		super(new BorderLayout(15, 5));
		this.r = r;
		r.addActualiseListener(this);
		setOpaque(false);
		type = new JComboBox<String>(TypeLieu.noms());
		type.setFont(Style.POLICE);
		taille = new JSlider(TypeLieu.TAILLE_MIN, TypeLieu.TAILLE_MIN);
		or = new OrGraphique();
		JPanel p = new JPanel(new LayoutLignes());
		acheter = new Bouton("~ Construire ~").large();
		acheter.addActionListener(l);
		tTaille = Outil.getTexte("  " + taille.getValue() + "m", false);
		blocs = Outil.getTexte("", false);
		p.setOpaque(false);
		p.add(Outil.creerPanel("Architecture", type));
		p.add(Outil.creerPanel(Outil.getTexte("  Surface  ", false), taille, tTaille));
		p.add(Outil.creerPanel("Blocs disponibles", blocs));
		img = new PanelImage();
		img.setLayout(new BorderLayout());
		img.setPreferredSize(new Dimension(100, 128));
		img.add(or, BorderLayout.SOUTH);
		add(img, BorderLayout.WEST);
		add(p);
		add(acheter, BorderLayout.SOUTH);
		setType(TypeLieu.CAMPEMENT);
		type.addActionListener(this);
		taille.addChangeListener(this);
		selection(null);
	}

	public void setType(TypeLieu type) {
		this.type.setSelectedIndex(type.getID());
		taille.setMaximum(type.getTailleMax());
		img.setImage(type.getImage());
		blocs.setText(type.getNombreBlocs() + "");
		actualise();
	}

	public void selection(CaseTerritoire objet) {
		this.objet = objet;
		actualiseAchetable();
	}
	
	public void actualiseAchetable() {
		acheter.setVisible(objet != null && objet.getLieu() == null && r.peutUtiliser(or.getOr()));
	}

	public Lieu getLieu() {
		return new Lieu(objet.getPosX(), objet.getPosY(), taille.getValue(), TypeLieu.get(type.getSelectedIndex()), or.getOr());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		setType(TypeLieu.get(type.getSelectedIndex()));
	}

	@Override
	public void actualise() {
		try {
			or.getOr().set(taille.getValue() * 5 + TypeLieu.get(type.getSelectedIndex()).getNombreBlocs());
		} catch (ManqueRessourceException e) {
			e.printStackTrace();
		}
		or.setCouleur(r.peutUtiliser(or.getOr()) ? Color.BLACK : Color.RED);
		actualiseAchetable();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		tTaille.setText("  " + taille.getValue() + "m");
		actualise();
	}
	
}
