package composants.diaporama;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.AbstractButton;
import javax.swing.JLabel;

public class ControleurBarre<E extends Component> extends ControleDiapoBoutons<E> {
	private static final long serialVersionUID = 1L;

	
	public ControleurBarre(Diaporama<E> diaporama) {
		super(diaporama);
		diaporama.add(this, BorderLayout.NORTH);
	}
	
	@Override
	protected void placer(AbstractButton plus, AbstractButton moins, JLabel texte) {
		setLayout(new BorderLayout());
		add(moins, BorderLayout.WEST);
		add(plus, BorderLayout.EAST);
		add(texte, BorderLayout.CENTER);
	}

}
