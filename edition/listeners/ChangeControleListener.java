package listeners;

import java.util.EventListener;

public interface ChangeControleListener extends EventListener {
	public boolean change(byte id, int controle);
}
