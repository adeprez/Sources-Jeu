package composants.panel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import composants.styles.Bouton;

public class PanelMasquable extends JPanel {
	private static final long serialVersionUID = 1L;
	private final AbstractButton change;
	private final String nom;
	private JComponent centre;


	public PanelMasquable(String nom) {
		super(new BorderLayout());
		this.nom = nom;
		setOpaque(false);
		change = new Bouton().large();
		change.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				change();
			}
		});
		add(change, BorderLayout.NORTH);
	}

	public void setCentre(JComponent centre) {
		this.centre = centre;
		add(centre, BorderLayout.CENTER);
		change();
	}

	private void change() {
		if(centre != null) {
			boolean visible = !centre.isVisible();
			centre.setVisible(visible);
			change.setText((visible ? "Masquer " : "Afficher ") + nom);
			validate();
			repaint();
		}
	}


}
