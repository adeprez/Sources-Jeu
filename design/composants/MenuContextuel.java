package composants;

import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class MenuContextuel extends JPopupMenu {
	private static final long serialVersionUID = 1L;


	public MenuContextuel(Component... sources) {
		for(final Component source : sources)
			source.addMouseListener(new MenuContextuelListener());
	}

	public JMenuItem ajoutMenu(ActionListener l, String nom) {
		JMenuItem menu = new JMenuItem(nom);
		menu.setActionCommand(nom);
		menu.addActionListener(l);
		add(menu);
		return menu;
	}
	
	public JMenuItem ajoutMenu(ActionListener l, String nom, Image image) {
		JMenuItem item = ajoutMenu(l, nom);
		item.setIcon(new ImageIcon(image));
		return item;
	}
	
	public JMenuItem ajoutMenu(ActionListener l, String nom, String... sousMenus) {
		JMenuItem menu = ajoutMenu(l, nom);
		for(String s : sousMenus) {
			JMenuItem i = new JMenuItem(s);
			i.setActionCommand(s);
			i.addActionListener(l);
			menu.add(i);
		}
		return menu;
	}

	private class MenuContextuelListener extends MouseAdapter {

		@Override
		public void mousePressed(MouseEvent evt) {
			if(evt.isPopupTrigger())
				show(evt.getComponent(), evt.getX(), evt.getY());
		}

		@Override
		public void mouseReleased(MouseEvent evt) {
			if(evt.isPopupTrigger())
				show(evt.getComponent(), evt.getX(), evt.getY());
		}
	}

}
