package composants.diaporama;

import java.awt.Component;

import javax.swing.JPanel;

public abstract class ControleurDiaporama<E extends Component> extends JPanel {
	private static final long serialVersionUID = 1L;
	private final Diaporama<E> diaporama;
	
	
	public ControleurDiaporama(Diaporama<E> diaporama) {
		setOpaque(false);
		this.diaporama = diaporama;
	}

	public abstract void actualise();

	public ModeleDiaporama<E> getModele() {
		return diaporama.getModele();
	}
	
	public Diaporama<E> getDiaporama() {
		return diaporama;
	}
	
	public void suivant() {
		getModele().suivant();
	}
	
	public void precedent() {
		getModele().precedent();
	}

}
