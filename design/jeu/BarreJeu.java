package jeu;

import interfaces.Actualisable;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import layouts.LayoutPerso;
import layouts.PlaceurComposants;
import listeners.ChangeSpecialiteListener;
import listeners.DestructibleListener;
import perso.AbstractPerso;
import perso.Perso;
import physique.PhysiqueDestructible;
import specialite.Specialite;
import statique.Style;
import xp.PanelSpecialite;

public class BarreJeu extends JPanel implements Actualisable, ChangeSpecialiteListener, PlaceurComposants, DestructibleListener {
    private static final long serialVersionUID = 1L;
    private final PanelSpecialitesJeu type;
    private final JProgressBar vie;
    private final Perso perso;
    private PanelSpecialiteJeu spe;


    public BarreJeu(Perso perso, ChangeSpecialiteListener l1) {
	this.perso = perso;
	perso.setVivantListener(this);
	setLayout(new LayoutPerso(this));
	setPreferredSize(new Dimension(70, 70));
	setOpaque(false);

	type = new PanelSpecialitesJeu(l1, perso.getSpecialites());
	vie = new JProgressBar(SwingConstants.VERTICAL) {
	    private static final long serialVersionUID = 1L;
	    @Override
	    public void paintComponent(Graphics g) {
		PanelSpecialite.dessineFondCuivre(this, g);
		super.paintComponent(g);
	    }
	};
	vie.setFont(Style.POLICE.deriveFont(15f));
	vie.setPreferredSize(new Dimension(20, 80));
	vie.setStringPainted(true);
	add(type);
	add(vie);

	actualise();
	vieChange(perso);
	perso.addChangeSpecialiteListener(this);
    }

    public void setSpecialite(Specialite specialite) {
	if(spe != null)
	    remove(spe);
	spe = new PanelSpecialiteJeu(specialite);
	add(spe);
	type.changeSpecialite(specialite);
	validate();
	doLayout();
	repaint();
    }

    @Override
    public void actualise() {
	setSpecialite(perso.getSpecialite());
    }

    @Override
    public void changeSpecialite(AbstractPerso perso, Specialite ancienne, Specialite nouvelle) {
	actualise();
    }

    @Override
    public void layout(Container parent) {
	Rectangle r = new Rectangle((getWidth() - vie.getPreferredSize().width)/2, 0, vie.getPreferredSize().width, getHeight());
	vie.setBounds(r);
	Dimension d1 = spe.getPreferredSize(), d2 = type.getPreferredSize();
	spe.setBounds(r.x + r.width, r.height - d1.height, d1.width, d1.height);
	type.setBounds(r.x - d2.width, r.height - d2.height, d2.width, d2.height);
    }

    @Override
    public void meurt(PhysiqueDestructible vivant) {
	vie.setString("Mort");
    }

    @Override
    public void vieChange(PhysiqueDestructible vivant) {
	vie.setString(vivant.getVie() + "");
	vie.setValue(vivant.getPourcentVie());
	vie.setToolTipText("Vie : " + vivant.getPourcentVie() + "%");
    }

}
