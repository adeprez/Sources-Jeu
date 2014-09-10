package composants.diaporama;

import interfaces.Actualisable;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

import listeners.AjoutListener;
import listeners.ChangeDiapoListener;

public class DefilementComposants<E extends Component> extends JComponent 
implements ChangeDiapoListener, AjoutListener<E>, Actualisable, LayoutManager {
	private static final int ESPACEMENT = 15;
	private static final long serialVersionUID = 1L;
	private final ModeleDiaporama<E> modele;
	private Component centre;


	public DefilementComposants(ModeleDiaporama<E> modele) {
		setLayout(this);
		this.modele = modele;
		modele.addChangeDiapoListener(this);
		modele.addAjoutListener(this);
		setOpaque(false);
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		doLayout();
	}

	@Override
	public void changeDiapo(Object diapo) {
		centre = (Component) diapo;
		doLayout();
	}

	@Override
	public void ajout(final E e) {
		actualise();
		e.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				modele.setIndex(modele.getElements().indexOf(e));
			}
		});
	}

	@Override
	public void actualise() {
		removeAll();
		for(final E e : modele.getElements())
			add(e);
		validate();
		repaint();
	}

	@Override
	public void addLayoutComponent(String name, Component comp) {}

	@Override
	public void layoutContainer(Container parent) {
		if(centre != null) {
			final int X = (parent.getWidth() - centre.getWidth())/2;
			int milieu = modele.getIndex();
			int x = X;
			for(int i=milieu ; i<parent.getComponentCount() ; i++) {
				Component c = parent.getComponent(i);
				parent.getComponent(i).setBounds(x, 0, parent.getWidth()/parent.getComponentCount(), parent.getHeight());
				x += c.getWidth() + ESPACEMENT;
			}
			x = X;
			for(int i=milieu-1 ; i>=0 ; i--) {
				Component c = parent.getComponent(i);
				x -= c.getWidth() + ESPACEMENT;
				parent.getComponent(i).setBounds(x, 0, parent.getWidth()/parent.getComponentCount(), parent.getHeight());
			}
		}
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		int w = 0, h = 0;
		for(final Component c : parent.getComponents()) {
			w += c.getMinimumSize().width;
			if(c.getMinimumSize().height > h)
				h = c.getMinimumSize().height;
		}
		return new Dimension(w, h);
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		int w = 0, h = 0;
		for(final Component c : parent.getComponents()) {
			w += c.getPreferredSize().width;
			if(c.getPreferredSize().height > h)
				h = c.getPreferredSize().height;
		}
		return new Dimension(w, h);
	}

	@Override
	public void removeLayoutComponent(Component comp) {}


}
