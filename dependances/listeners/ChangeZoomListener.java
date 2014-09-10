package listeners;

import java.awt.Dimension;
import java.util.EventListener;

public interface ChangeZoomListener extends EventListener {
	public void zoom(Dimension d);
}
