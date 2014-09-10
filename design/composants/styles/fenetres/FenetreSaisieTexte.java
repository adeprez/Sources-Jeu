package composants.styles.fenetres;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

import composants.styles.ChampTexte;

import divers.LimiteurTexte;
import exceptions.AnnulationException;

public class FenetreSaisieTexte extends FenetreSaisie<JTextField> implements ActionListener {
	private static final long serialVersionUID = 1L;


	public FenetreSaisieTexte(String nom) {
		super(nom, new ChampTexte());
		get().setForeground(Color.ORANGE);
		get().addActionListener(this);
	}
	
	public FenetreSaisieTexte(String nom, int max) {
		this(nom);
		get().setDocument(new LimiteurTexte(max));
	}

	public String getTexte() throws AnnulationException {
		String txt = demander().getText().trim();
		if(txt.isEmpty())
			throw new AnnulationException("Saisie vide");
		return txt;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		fermer();
	}

}
