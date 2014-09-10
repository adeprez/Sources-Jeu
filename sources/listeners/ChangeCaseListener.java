package listeners;

import java.util.EventListener;

public interface ChangeCaseListener extends EventListener {
	public void changeCase(int x, int y, int nX, int nY);
}
