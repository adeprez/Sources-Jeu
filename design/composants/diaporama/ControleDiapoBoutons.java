package composants.diaporama;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JLabel;

import composants.styles.Bouton;

import divers.Outil;

public abstract class ControleDiapoBoutons<E extends Component> extends ControleurDiaporama<E> implements ActionListener {
	private static final long serialVersionUID = 1L;
	private final AbstractButton plus, moins;
	private final JLabel texte;


	public ControleDiapoBoutons(Diaporama<E> diaporama) {
		super(diaporama);
		plus = new Bouton(">", true);
		moins = new Bouton("<", true);
		plus.setToolTipText("Suivant");
		moins.setToolTipText("Precedent");
		plus.addActionListener(this);
		moins.addActionListener(this);
		texte = Outil.getTexte(diaporama.getName(), true);
		placer(plus, moins, texte);
		actualise();
	}

	protected abstract void placer(AbstractButton plus, AbstractButton moins, JLabel texte);

	public AbstractButton getPlus() {
		return plus;
	}

	public AbstractButton getMoins() {
		return moins;
	}

	public JLabel getTexte() {
		return texte;
	}

	@Override
	public void actualise() {
		plus.setVisible(getDiaporama().getModele().getNombre() > 1);
		moins.setVisible(getDiaporama().getModele().getNombre() > 1);
		texte.setText(getDiaporama().getName() == null && getDiaporama().getModele().aElement() ? 
				"(" + (getDiaporama().getModele().getIndex() + 1) + 
				"/" + getDiaporama().getModele().getNombre() + ")" 
				: getDiaporama().getName());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == plus)
			suivant();
		else if(e.getSource() == moins)
			precedent();
		actualise();
	}


}
