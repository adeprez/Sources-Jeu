package editeurMap;

import interfaces.Cliquable;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import listeners.ChangeEtatListener;
import listeners.CliqueListener;
import map.objets.Objet;
import objets.SelecteurObjetSprite;
import statique.Style;

import composants.panel.PanelImage;

public class ChoixImageObjet extends JPanel implements Cliquable, ActionListener {
    private static final long serialVersionUID = 1L;
    private final JCheckBox aFond, decoupeForme;
    private final PanelImage image, fond;
    private int idFond, idImage;


    public ChoixImageObjet() {
	setLayout(new GridLayout(2, 1, 5, 5));
	setOpaque(false);
	aFond = new JCheckBox("Fond");
	decoupeForme = new JCheckBox("Decoupe");
	aFond.setFont(Style.POLICE);
	decoupeForme.setFont(Style.POLICE);
	BufferedImage img = SelecteurObjetSprite.getImage(0);
	image = new PanelImage(img);
	fond = new PanelImage(img);
	image.addMouseListener(new CliqueListener(this));
	fond.addMouseListener(new CliqueListener(this));
	aFond.addActionListener(this);
	decoupeForme.addActionListener(this);
	image.add(decoupeForme);
	fond.add(aFond);
	add(image);
	add(fond);
	setPreferredSize(new Dimension(120, 333));
    }

    public int getIdFond() {
	return aFond.isSelected() ?  idFond : Objet.SANS_FOND;
    }

    public int getIdImage() {
	return idImage;
    }

    public boolean decoupeForme() {
	return decoupeForme.isSelected();
    }

    public void addChangeEtatListener(ChangeEtatListener l) {
	listenerList.add(ChangeEtatListener.class, l);
    }

    public void removeChangeEtatListener(ChangeEtatListener l) {
	listenerList.remove(ChangeEtatListener.class, l);
    }

    public void change() {
	for(final ChangeEtatListener l : getListeners(ChangeEtatListener.class))
	    l.changeEtat(decoupeForme.isSelected());
    }

    @Override
    public void clique(MouseEvent e) {
	int id = SelecteurObjetSprite.get();
	if(e.getSource() == image) {
	    image.setImage(SelecteurObjetSprite.getImage(id));
	    idImage = id;
	} else {
	    fond.setImage(SelecteurObjetSprite.getImage(id));
	    idFond = id;
	}
	change();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	change();
    }

}
