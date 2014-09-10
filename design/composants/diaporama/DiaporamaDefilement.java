package composants.diaporama;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JPanel;

import composants.styles.Bouton;

public class DiaporamaDefilement<E extends Component> extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private AbstractButton gauche, droite;
	private ModeleDiaporama<E> modele;
	
	
	public DiaporamaDefilement() {
		this(new ModeleDiaporama<E>());
	}

	public DiaporamaDefilement(ModeleDiaporama<E> modele) {
		super(new BorderLayout());
		setOpaque(false);
		gauche = new Bouton("<", true);
		droite = new Bouton(">", true);
		add(gauche, BorderLayout.WEST);
		add(droite, BorderLayout.EAST);
		gauche.addActionListener(this);
		droite.addActionListener(this);
		add(new DefilementComposants<E>(modele), BorderLayout.CENTER);
		this.modele = modele;
	}

	public ModeleDiaporama<E> getModele() {
		return modele;
	}

	public void setModele(ModeleDiaporama<E> modele) {
		this.modele = modele;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == gauche)
			modele.precedent();
		else if(e.getSource() == droite)
			modele.suivant();
	}
	
}
