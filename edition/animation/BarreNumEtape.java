package animation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import listeners.ChangeEtapeListener;
import ressources.sprites.animation.sequence.Sequence;
import divers.Outil;

public class BarreNumEtape extends JPanel implements ChangeListener, KeyListener {
	private static final long serialVersionUID = 1L;
	private final ChangeEtapeListener l;
	private final Sequence sequence;
	private final JSpinner nombre;
	private final JSlider slide;
	private final JLabel texte;

	
	public BarreNumEtape(int numEtape, ChangeEtapeListener l, Sequence sequence) {
		this.l = l;
		this.sequence = sequence;
		setBorder(BorderFactory.createRaisedBevelBorder());
		setLayout(new BorderLayout());
		if(sequence.getModele().getNombreEtapes() == 0)
			sequence.getModele().addEtape();
		nombre = new JSpinner(new SpinnerNumberModel(numEtape, 0, 1000, 1));
		slide = new JSlider(SwingConstants.VERTICAL, 0, sequence.getModele().getNombreEtapes() - 1, numEtape);
		texte = Outil.getTexte("", false);
		add(texte, BorderLayout.SOUTH);
		add(slide, BorderLayout.CENTER);
		add(nombre, BorderLayout.NORTH);
		nombre.setPreferredSize(new Dimension(50, 30));
		changeTexte();
		slide.addChangeListener(this);
		nombre.addChangeListener(this);
	}
	
	public void change() {
		int val = slide.getValue();
		while(val >= sequence.getModele().getNombreEtapes())
			sequence.getModele().addEtape();
		changeTexte();
		l.changeEtape(val);
	}
	
	public void changeTexte() {
		texte.setText(nombre.getValue() + "/" + (sequence.getModele().getNombreEtapes() - 1));
	}
	
	public void setEtape(int etape) {
		nombre.setValue(etape);
		slide.setValue(etape);
		slide.setMaximum(sequence.getModele().getNombreEtapes() - 1);
		changeTexte();
	}
	
	public void incrEtape(int incr) {
		int val = Math.max(0, (int) nombre.getValue() + incr);
		if(val >= sequence.getModele().getNombreEtapes())
			slide.setMaximum(sequence.getModele().getNombreEtapes());
		setEtape(val);
		change();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if(e.getSource() == nombre) {
			int val = (int) nombre.getValue();
			if(val >= sequence.getModele().getNombreEtapes())
				slide.setMaximum(sequence.getModele().getNombreEtapes());
			slide.setValue(val);
		} else nombre.setValue(slide.getValue());
		change();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_S:
		case KeyEvent.VK_Q:
			if(slide.getValue() > 0)
				incrEtape(-1);
			break;
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_UP:
		case KeyEvent.VK_Z:
		case KeyEvent.VK_D:
			if(slide.getValue() < sequence.getModele().getNombreEtapes() - 1)
				incrEtape(1);
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}
	
}
