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


    public HorlogeProgression() {
	super(new GridLayout());
	setOpaque(false);
	add(avancement = new JProgressBar());
	avancement.setStringPainted(true);
	avancement.setFont(Style.POLICE);
	avancement.setValue(100);
    }

    public HorlogeProgression setTexte(String texte) {
	avancement.setString(texte);
	avancement.setToolTipText(texte + " secondes restantes");
	return this;
    }

    @Override
    public void action(Horloge horloge) {
	int max = ((CompteARebours) horloge).getMax();
	if(max != 0) {
	    setTexte(horloge.toString());
	    avancement.setMaximum(max);
	    avancement.setValue(horloge.getTemps());
	}
    }

}
