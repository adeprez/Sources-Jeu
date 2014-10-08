package composants.styles;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import listeners.MouseScrollListener;

public class ScrollPaneTransparent extends JScrollPane {
    private static final long serialVersionUID = 1L;


    public ScrollPaneTransparent(JComponent c) {
	this(c, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    public ScrollPaneTransparent(JComponent c, int vertical, int horizontal) {
	super(c, vertical, horizontal);
	c.setOpaque(false);
	getViewport().setOpaque(false);
	setBorder(null);
	getVerticalScrollBar().setUnitIncrement(5);
	new MouseScrollListener(this).setListener();
    }

}
