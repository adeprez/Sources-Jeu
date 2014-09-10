package composants;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

import statique.Style;
import temps.CompteARebours;
import temps.Horloge;
import temps.HorlogeListener;

public class HorlogeProgression extends JPanel implements HorlogeListener {
	private static final long serialVersionUID = 1L;
	private final JProgressBar avancement;
	private final CompteARebours horloge;


	public HorlogeProgression(CompteARebours horloge) {
		super(new GridLayout());
		this.horloge = horloge;
		setOpaque(false);
		add(avancement = new JProgressBar());
		avancement.setStringPainted(true);
		avancement.setFont(Style.POLICE);
		avancement.setValue(100);
		action(horloge);
		horloge.addHorlogeListener(this);
	}

	public HorlogeProgression setTexte(String texte) {
		avancement.setString(texte);
		avancement.setToolTipText(texte + " secondes restantes");
		return this;
	}

	@Override
	public void action(Horloge horloge) {
		if(this.horloge.getMax() != 0) {
			setTexte(horloge.toString());
			avancement.setMaximum(this.horloge.getMax());
			avancement.setValue(horloge.getTemps());
		}
	}

}
