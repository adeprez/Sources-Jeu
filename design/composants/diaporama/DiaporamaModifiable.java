package composants.diaporama;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;

import base.Ecran;

import composants.styles.Bouton;

public abstract class DiaporamaModifiable<E extends Component> extends Ecran {
	private static final long serialVersionUID = 1L;
	protected final Diaporama<E> diapo;

	
	public DiaporamaModifiable() {
		super("fond/parchemin.jpg");
		setLayout(new BorderLayout());
		diapo = new Diaporama<E>();
		AbstractButton creer = new Bouton("Creer").large();
		AbstractButton enregistrer = new Bouton("Enregistrer").large();
		creer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				creer();
			}
		});
		enregistrer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enregistrer();
			}
		});
		add(creer, BorderLayout.NORTH);
		add(diapo, BorderLayout.CENTER);
		add(enregistrer, BorderLayout.SOUTH);
	}
	
	public abstract void creer();
	
	public abstract void enregistrer();
	
	public Diaporama<E> getDiapo() {
		return diapo;
	}
	
}
