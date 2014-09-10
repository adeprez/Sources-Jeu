package composants.diaporama;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.AbstractButton;
import javax.swing.JLabel;

public class ControleurGaucheDroite<E extends Component> extends ControleDiapoBoutons<E> {
	private static final long serialVersionUID = 1L;

	
	public ControleurGaucheDroite(Diaporama<E> diaporama) {
		super(diaporama);
	}
	
	@Override
	protected void placer(AbstractButton plus, AbstractButton moins, JLabel texte) {
		setLayout(new BorderLayout());
		moins.setText("{-");
		plus.setText("-}");
		getDiaporama().add(moins, BorderLayout.WEST);
		getDiaporama().add(plus, BorderLayout.EAST);
	}

}
