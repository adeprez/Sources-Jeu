package base;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

import ressources.Images;
import ressources.Proprietes;
import divers.Outil;

public class IconeTaches extends TrayIcon implements ActionListener {
	private static final String TITRE = "Mon programme";
	private static IconeTaches instance;
	private final MenuItem quitter, infos, admin, d3d, d2d;
	private final PopupMenu menu;

	
	public static IconeTaches getInstance() {
		synchronized(IconeTaches.class) {
			if(instance == null)
				instance = new IconeTaches();
			return instance;
		}
	}

	private IconeTaches() {
		super(Images.get("divers/icone.png", true), TITRE);
		setImageAutoSize(true);
		setPopupMenu(menu = new PopupMenu("Menu"));
		menu.add(quitter = new MenuItem("Quitter"));
		menu.add(infos = new MenuItem("A propos..."));
		menu.add(admin = new MenuItem("Administrer"));
		menu.addSeparator();
		menu.add(d3d = new MenuItem("Dessin 3D"));
		menu.add(d2d = new MenuItem("Dessin 2D"));
		quitter.addActionListener(this);
		infos.addActionListener(this);
		admin.addActionListener(this);
		d3d.addActionListener(this);
		d2d.addActionListener(this);
		if(SystemTray.isSupported()) try {
			SystemTray.getSystemTray().add(this);
		} catch(AWTException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == quitter) {
			if(Fenetre.aInstance())
				Fenetre.getInstance().fermer();
			System.exit(0);
		} else if(e.getSource() == infos)
			Outil.message("Programme concu par Alexis Deprez");
		else if(e.getSource() == admin)
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					EcranAdministration.ouvrir(true);
				}});
		else if(e.getSource() == d3d)
			Proprietes.getInstance().set3D(true);
		else if(e.getSource() == d2d)
			Proprietes.getInstance().set3D(false);
	}

}
