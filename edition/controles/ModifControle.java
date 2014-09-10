package controles;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import layouts.LayoutTriplet;
import listeners.ChangeControleListener;

import composants.styles.Bouton;
import composants.styles.LignePoints;

import divers.Outil;

public class ModifControle extends JPanel {
	private static final long serialVersionUID = 1L;
	private final ChangeControleListener l;
	private final byte id;
	private final JLabel texte;
	private final JButton bouton;
	private int touche;

	
	public ModifControle(ChangeControleListener l, final byte id, int touche) {
		super(new LayoutTriplet());
		this.l = l;
		this.id = id;
		this.touche = touche;
		setOpaque(false);
		texte = Outil.getTexte("", false);
		bouton = new Bouton();
		bouton.setToolTipText("Cliquez pour modifier");
		bouton.setMinimumSize(new Dimension(75, 30));
		bouton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeTouche(id);
			}
		});
		add(texte);
		add(new LignePoints());
		add(bouton);
		actualiser();
	}
	
	public void setTouche(int touche) {
		this.touche = touche;
	}

	public void actualiser() {
		texte.setText("  " + TypeAction.get(id).getNom());
		bouton.setText(KeyEvent.getKeyText(touche));
	}

	private void changeTouche(byte id) {
		new SaisieTouche(l, id, texte.getText().trim()).afficher();
	}

}
