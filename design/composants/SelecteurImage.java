package composants;

import interfaces.ImageListener;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import composants.panel.PanelImage;
import composants.styles.Bouton;

import divers.Outil;
import exceptions.AnnulationException;
import filtres.FiltreImage;

public class SelecteurImage extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private final PanelImage image;
	private final AbstractButton ouvrir;
	private final int id;


	public SelecteurImage(int id) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.id = id;
		setPreferredSize(new Dimension(125, 125));
		ouvrir = new Bouton("Ouvrir...");
		ouvrir.addActionListener(this);
		image = new PanelImage();
		add(image);
		add(ouvrir);
	}

	public BufferedImage getImage() throws AnnulationException {
		if(image.getImage() == null)
			throw new AnnulationException("Choisir une image");
		return image.getImage();
	}

	public void setImage(BufferedImage img) {
		image.setImage(img);
		validate();
		repaint();
	}

	public void addImageListener(ImageListener l) {
		listenerList.add(ImageListener.class, l);
	}

	public void removeImageListener(ImageListener l) {
		listenerList.remove(ImageListener.class, l);
	}

	private void notifyImageListener(BufferedImage img) {
		for(final ImageListener l : listenerList.getListeners(ImageListener.class))
			l.change(id, img);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == ouvrir) try {
			BufferedImage i = chargeImage();
			image.setImage(i);
			notifyImageListener(i);
			validate();
			repaint();
		} catch(AnnulationException err) {
		} catch(Exception e1) {
			Outil.erreur("Impossible d'utiliser cette image");
			e1.printStackTrace();
		}
	}

	public static BufferedImage chargeImage() throws AnnulationException {
		JFileChooser f = new JFileChooser();
		f.setFileFilter(new FiltreImage());
		if(f.showOpenDialog(null) == JFileChooser.CANCEL_OPTION)
			throw new AnnulationException();
		try {
			return ImageIO.read(f.getSelectedFile());
		} catch(Exception err) {
			throw new AnnulationException(err.getMessage());
		}
	}


}
