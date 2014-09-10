package fichiers;

import io.IO;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ressources.Fichiers;
import ressources.Images;
import ressources.RessourcesLoader;

public class EditeurFichier extends JPanel implements ActionListener, ChangeListener {
	private static final long serialVersionUID = 1L;
	private final JPanel panel;
	private final JTextField texte;
	private final JButton annuler;
	private final IO io;
	private String nom;
	private IO champs;


	public EditeurFichier(File fichier) {
		setLayout(new BorderLayout());
		try {
			nom = fichier.toString().substring(Fichiers.getChemin().length());
		} catch(Exception e) {
			nom = fichier.toString().substring(RessourcesLoader.PATH.length());
		}
		io = Fichiers.lire(nom);
		panel = new JPanel(new GridLayout(0, 4));
		panel.setBorder(new TitledBorder("Octets"));
		texte = new JTextField();
		texte.setEditable(false);
		texte.setBackground(Color.GRAY);
		texte.setForeground(Color.WHITE);
		annuler = new JButton("Retablir");
		annuler.addActionListener(this);
		actualise();
		Container ev = new EditeurValeurs(this);
		ev.add(texte, BorderLayout.SOUTH);
		add(ev, BorderLayout.NORTH);
		add(new JScrollPane(panel), BorderLayout.CENTER);
		add(annuler, BorderLayout.SOUTH);
	}

	public void actualise() {
		panel.removeAll();
		champs = Fichiers.lire(nom);
		for(int i=0 ; i<champs.getBytes().length ; i++) {
			final int id = i;
			JSpinner spin = new JSpinner(new SpinnerNumberModel(champs.getBytes()[i], -128, 127, 1));
			spin.addChangeListener(this);
			spin.setToolTipText(i+"");
			panel.add(spin);
			panel.add(new JLabel(""+(char) champs.getBytes()[i], SwingConstants.CENTER));
			final JPanel control = new JPanel();
			JButton suppr = new JButton("Supprimer", new ImageIcon(Images.get("icones/fermer.png", true)));
			suppr.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					champs.supprimer(id);
					enregistrer();
				}
			});
			if(id != 0) {
				JButton monter = new JButton("^");
				monter.setToolTipText("Decaler vers le haut");
				monter.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				monter.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						champs.inverser(id, id - 1);
						enregistrer();
					}
				});
				control.add(monter);
			}
			if(id != champs.size() - 1) {
				JButton descendre = new JButton("v");
				descendre.setToolTipText("Decaler vers le bas");
				descendre.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				descendre.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						champs.inverser(id, id + 1);
						enregistrer();
					}
				});
				control.add(descendre);
			}
			panel.add(control);
			panel.add(suppr);
		}
		texte.setText(champs.toString());
		validate();
		repaint();
	}

	public IO getChamps() {
		return champs;
	}

	public void enregistrer() {
		Fichiers.ecrire(champs, nom);
		actualise();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Fichiers.ecrire(io, nom);
		actualise();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		champs = new IO();
		for(final Component c : panel.getComponents())
			if(c instanceof JSpinner)
				champs.add(((Integer) ((JSpinner) c).getValue()).byteValue());
		enregistrer();
	}


}
