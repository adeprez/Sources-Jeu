package perso;

import javax.swing.border.TitledBorder;

import base.Ecran;

public class EcranPerso extends Ecran {
	private static final long serialVersionUID = 1L;
	private final Perso perso;
	
	
	public EcranPerso(Perso perso) {
		setName(perso.getNom());
		setBorder(new TitledBorder(" "));
		this.perso = perso;
		add(new InterfacePerso(true, perso));
	}

	public Perso getPerso() {
		return perso;
	}
	
}
