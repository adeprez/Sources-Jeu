package sprites;

import interfaces.TacheRunnable;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import ressources.RessourcesLoader;
import statique.Style;

import composants.styles.EcranAttente;

import divers.Outil;

public class SelecteurFichiers extends JComponent implements ActionListener, TacheRunnable {
	private static final long serialVersionUID = 1L;
	private static final String[] EXTENSIONS = new String[] {"png","jpg"};
	private final JFileChooser fichier;
	private final JCheckBox recursif;
	private final PanelContainerImages cImages;
	private File[] fichiers;
	private int avancement;


	public SelecteurFichiers() {
		setLayout(new BorderLayout());
		setOpaque(false);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		recursif = new JCheckBox("Recursif", true);
		cImages = new PanelContainerImages(new Dimension(50, 50));
		recursif.setFont(Style.POLICE);
		fichier = new JFileChooser();
		fichier.setMultiSelectionEnabled(true);
		fichier.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fichier.addActionListener(this);
		add(recursif, BorderLayout.NORTH);
		add(new JSplitPane(JSplitPane.VERTICAL_SPLIT, fichier, new JScrollPane(cImages)), BorderLayout.CENTER);
	}

	public void setFichiers(File... fichiers) {
		this.fichiers = fichiers;
		avancement = 0;
		new EcranAttente("Chargement des images...", this);
	}
	
	public BufferedImage[] getImages() {
		return cImages.getImages();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		setFichiers(fichier.getSelectedFiles());
	}

	@Override
	public void executer() {
		cImages.vider();
		List<File> fichiers = new ArrayList<File>();
		if(recursif.isSelected())
			for(final File f : this.fichiers)
				RessourcesLoader.getFichiersRecurrence(fichiers, f, EXTENSIONS);
		else for(final File f : this.fichiers)
			if(RessourcesLoader.aExtension(f, EXTENSIONS))
				fichiers.add(f);
		BufferedImage[] images = new BufferedImage[fichiers.size()];
		int max = images.length - 1;
		avancement = Outil.getPourcentage(0, max);
		for(int i=0 ; i<images.length ; i++) try {
			avancement = Outil.getPourcentage(i, max);
			images[i] = ImageIO.read(fichiers.get(i));
		} catch (IOException e) {
			e.printStackTrace();
			images[i] = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_BINARY);
		}
		cImages.setImages(images);
		cImages.setToolTipText(images.length + " images");
		avancement = 100;
	}

	@Override
	public int getAvancement() {
		return avancement;
	}


}
