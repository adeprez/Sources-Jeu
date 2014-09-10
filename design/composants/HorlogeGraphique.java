package composants;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import temps.Horloge;
import temps.HorlogeListener;
import divers.Outil;

public class HorlogeGraphique extends JPanel implements HorlogeListener {
	private static final long serialVersionUID = 1L;
	private final JLabel txt;
	
	
	public HorlogeGraphique(Horloge horloge) {
		super(new GridLayout());
		horloge.addHorlogeListener(this);
		setOpaque(false);
		add(txt = Outil.encadrer(Outil.getTexte("", true), true));
		action(horloge);
	}
	
	public HorlogeGraphique setTexte(String texte) {
		txt.setText(texte);
		return this;
	}

	@Override
	public void action(Horloge horloge) {
		if(txt != null)
			txt.setText(horloge.toString());
	}
	
}
