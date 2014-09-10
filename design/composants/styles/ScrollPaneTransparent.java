package composants.styles;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;

import listeners.MouseScrollListener;

public class ScrollPaneTransparent extends JScrollPane {
	private static final long serialVersionUID = 1L;

	
	public ScrollPaneTransparent(JComponent c) {
		super(c);
		c.setOpaque(false);
		getViewport().setOpaque(false);
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		new MouseScrollListener(this).setListener();
	}
	
}
