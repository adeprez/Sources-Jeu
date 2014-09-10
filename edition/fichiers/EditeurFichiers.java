package fichiers;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import listeners.RemoveListener;
import listeners.SelectionFichierListener;

import composants.panel.PanelImage;
import composants.panel.PanelOnglets;

import divers.Outil;

public class EditeurFichiers extends JPanel implements SelectionFichierListener, RemoveListener<String> {
	private static final long serialVersionUID = 1L;
	private final PanelOnglets tab;
	private final Map<String, Component> fichiers;


	public EditeurFichiers() {
		super(new BorderLayout());
		tab = new PanelOnglets();
		tab.addRemoveListener(this);
		add(tab, BorderLayout.CENTER);
		fichiers = new HashMap<String, Component>();
	}

	@Override
	public void selection(File fichier) {
		try {
			if(!fichiers.containsKey(fichier.toString())) {
				String nom = fichier.getName().toLowerCase();
				if(nom.endsWith(".png") || nom.endsWith(".jpg")) try {
					ajout(fichier.toString(), fichier.getName(), ImageIO.read(fichier));
				} catch(IOException e) {
					e.printStackTrace();
				}
				else ajout(fichier);
			} else {
				Component c = fichiers.get(fichier.toString());
				if(c != null) 
					tab.setSelectedComponent(c);
			}
		} catch(Exception e) {
			e.printStackTrace();
			Outil.erreur("Impossible d'ouvrir le fichier : " + e.getMessage());
		}
	}

	private void ajout(File fichier) {
		EditeurFichier e = new EditeurFichier(fichier);
		fichiers.put(fichier.toString(), e);
		tab.ajout(fichier.toString(), fichier.getName(), e);
	}

	private void ajout(String nom, String titre, BufferedImage image) {
		Component c = new PanelImage(image);
		tab.ajout(nom, titre, c);
		fichiers.put(nom, c);
	}

	@Override
	public void remove(String e) {
		fichiers.remove(e);
	}

}
