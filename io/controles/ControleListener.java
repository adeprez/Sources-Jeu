package controles;

import java.awt.event.KeyEvent;
import java.util.EventListener;

public interface ControleListener extends EventListener {
    public void appuie(KeyEvent e);
    public void relache(KeyEvent e);
}
