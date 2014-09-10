package carte.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


import layouts.LayoutLignes;
import listeners.ControleMapListener;
import statique.Style;
import divers.Taille;

public class PanelControleCarte extends JPanel implements ActionListener, ChangeListener {
	private static final long serialVersionUID = 1L;
	private final AbstractButton plus, moins;
	private final PrismeControleDirection dir;
	private final JSlider slide, angle;
	private final Taille taille;

	
	public PanelControleCarte(ControleMapListener l, Taille taille) {
		this.taille = taille;
		setBounds(5, 5, 45, 210);
		setOpaque(false);
		setLayout(new LayoutLignes(false));
		plus = new JButton("+");
		moins = new JButton("-");
		slide = new JSlider(SwingConstants.VERTICAL, taille.getlargeurMin(), taille.getLargeurMax(), taille.getLargeur());
		angle = new JSlider(SwingConstants.HORIZONTAL, 25, 100, (int) (taille.getRapport() * 100));
		angle.setToolTipText("angle");
		dir = new PrismeControleDirection(l);
		plus.setCursor(Style.curseur.main());
		moins.setCursor(Style.curseur.main());
		plus.setFont(getFont().deriveFont(10f));
		moins.setFont(getFont().deriveFont(10f));
		plus.setPreferredSize(new Dimension());
		moins.setPreferredSize(new Dimension());
		slide.setPreferredSize(new Dimension(15, 80));
		angle.setPreferredSize(new Dimension(40, 20));
		add(dir);
		add(Box.createRigidArea(new Dimension(1, 10)));
		add(angle);
		add(plus);
		add(slide);
		add(moins);
		moins.addActionListener(this);
		plus.addActionListener(this);
		slide.addChangeListener(this);
		angle.addChangeListener(this);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if(e.getSource() == slide)
			taille.setLargeur(slide.getValue());
		else if(e.getSource() == angle) {
			taille.setRapport(angle.getValue()/100.0);
			taille.forceHauteur(taille.getHauteur());
			taille.setLargeur(taille.getLargeur());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == moins)
			slide.setValue(slide.getValue() / 2);
		else slide.setValue(slide.getValue() * 2);
	}
	
	
}
