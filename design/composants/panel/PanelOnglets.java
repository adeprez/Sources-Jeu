package composants.panel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import listeners.RemoveListener;
import ressources.Images;
import statique.Style;

public class PanelOnglets extends JTabbedPane {
	private static final long serialVersionUID = 1L;

	
	public void ajout(final String nom, String titre, final Component c) {
		add(c);
		int index = getTabCount() - 1;
		JPanel p = new JPanel();
		p.setOpaque(false);
		p.add(new JLabel(titre));
		JButton b = new JButton(new ImageIcon(Images.get("icones/fermer.png", true)));
		b.setPreferredSize(new Dimension(18, 18));
		b.setBorderPainted(false);
		b.setContentAreaFilled(false);
		b.setToolTipText("Fermer");
		b.setCursor(Style.curseur.main());
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				remove(c);
				notifyRemoveListener(nom);
			}
		});
		p.add(b);
		setTabComponentAt(index, p);
		setSelectedComponent(c);
	}
	
	public void addRemoveListener(RemoveListener<String> l) {
		listenerList.add(RemoveListener.class, l);
	}
	
	public void removeRemoveListener(RemoveListener<String> l) {
		listenerList.remove(RemoveListener.class, l);
	}
	
	@SuppressWarnings("unchecked")
	private void notifyRemoveListener(String nom) {
		for(final RemoveListener<String> l : listenerList.getListeners(RemoveListener.class))
			l.remove(nom);
	}
	
}
