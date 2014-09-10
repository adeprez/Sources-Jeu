package composants;

import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

public class FenetreInterne extends JInternalFrame implements InternalFrameListener {
	private static final long serialVersionUID = 1L;
	private Dimension taille;

	
	public FenetreInterne(String titre, Container content, boolean redimensionnable, boolean fermable, boolean maximisable, boolean iconifiable) {
		super(titre, redimensionnable, fermable, maximisable, iconifiable);
		setContentPane(content);
		addInternalFrameListener(this);
	}
	
	public FenetreInterne(String titre, Container content) {
		this(titre, content, true, true, true, true);
	}
	
	public FenetreInterne(Container content) {
		this(null, content);
	}
	
	public FenetreInterne() {
		this(null, new Container());
	}
	
	@Override
	public void internalFrameDeiconified(InternalFrameEvent e) {
		setSize(taille);
	}
	
	@Override
	public void internalFrameIconified(InternalFrameEvent e) {
		taille = getSize();
		setSize(taille.width, 28);
	}

	@Override
	public void internalFrameActivated(InternalFrameEvent e) {
		
	}

	@Override
	public void internalFrameClosed(InternalFrameEvent e) {
		
	}

	@Override
	public void internalFrameClosing(InternalFrameEvent e) {
		
	}

	@Override
	public void internalFrameDeactivated(InternalFrameEvent e) {
		
	}

	@Override
	public void internalFrameOpened(InternalFrameEvent e) {
		
	}
	
}
