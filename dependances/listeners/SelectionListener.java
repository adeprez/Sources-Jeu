package listeners;

import java.util.EventListener;

public interface SelectionListener extends EventListener {
	public boolean selection(int x, int y, boolean cliqueDroit);
}
