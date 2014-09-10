package editeurMap;

import interfaces.Actualisable;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import listeners.AjoutListener;
import listeners.RemoveListener;
import map.Map;
import map.objets.Objet;
import ressources.compte.Compte;
import statique.Style;
import territoire.EcranGestionMonde;
import base.Ecran;
import base.Fenetre;
import carte.element.Lieu;

import composants.styles.BoutonImage;

public class EcranEditeurLieu extends Ecran implements AjoutListener<Objet>, RemoveListener<Objet>, Actualisable, ActionListener {
    private static final long serialVersionUID = 1L;
    private final EcranEditeurMap ecranMap;
    private final JProgressBar blocs;
    private final Compte compte;
    private final JButton ok;
    private final Lieu lieu;
    private final Map map;


    public EcranEditeurLieu(Compte compte, Lieu lieu) {
	super(new BorderLayout());
	setName(lieu.getNom());
	this.lieu = lieu;
	this.compte = compte;
	if(lieu.aMap(compte.path()))
	    map = lieu.chargeMap(compte.path());
	else map = new Map(lieu.getTaille(), null);
	map.setMaxObjets(lieu.getType().getNombreBlocs());
	map.addAjoutListener(this);
	map.addRemoveListener(this);
	ecranMap = new EcranEditeurMap(map, ok = new BoutonImage("icones/map.png"));
	ok.setPreferredSize(new Dimension(80, 80));
	ok.addActionListener(this);
	ok.setToolTipText("Gestion du monde");
	add(ecranMap);
	add(blocs = new JProgressBar(SwingConstants.VERTICAL, 0, map.getMaxObjets()), BorderLayout.WEST);
	blocs.setFont(Style.POLICE);
	blocs.setStringPainted(true);
	actualise();
    }

    @Override
    public boolean fermer() {
	ecranMap.fermer();
	return super.fermer();
    }

    @Override
    public void afficher(Fenetre fenetre) {
	super.afficher(fenetre);
	ecranMap.afficher(fenetre);
    }

    @Override
    public void remove(Objet e) {
	actualise();
    }

    @Override
    public void ajout(Objet e) {
	actualise();
    }

    @Override
    public void actualise() {
	int n = map.getNombreObjetRestants();
	blocs.setValue(n);
	String txt = n + "m� restants";
	blocs.setString(txt);
	blocs.setToolTipText(txt + ". Chaque bloc a une surface de 1m�");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	lieu.setMap(map, compte.path());
	changer(new EcranGestionMonde(compte));
    }

}
