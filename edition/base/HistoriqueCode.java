package base;

import io.IO;

import java.util.Date;

import javax.swing.JPanel;

import layouts.LayoutLignes;
import ressources.Fichiers;

import composants.panel.PanelMasquable;
import composants.styles.ScrollPaneTransparent;

import divers.Outil;

public class HistoriqueCode extends PanelMasquable {
	private static final long serialVersionUID = 1L;
	public static final int MINUTES = 600;
	public static final String PATH = "statistiques";


	public HistoriqueCode() {
		super("l'historique");
		JPanel contenu = new JPanel(new LayoutLignes());
		contenu.setOpaque(false);
		IO io = Fichiers.lire(PATH);
		while(io.aBytes(16))
			contenu.add(Outil.creerPanel(Outil.format(new Date(io.nextLong())), io.nextInt() + " classes, " + io.nextInt() + " lignes"));
		setCentre(new ScrollPaneTransparent(contenu));
	}


}
