package listeners;

import java.io.File;
import java.util.EventListener;

public interface SelectionFichierListener extends EventListener {
	public void selection(File fichier);
}
