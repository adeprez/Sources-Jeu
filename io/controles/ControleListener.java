package controles;

import java.util.EventListener;

public interface ControleListener extends EventListener {
	public void appuie(TypeAction action);
	public void relache(TypeAction action);
}
