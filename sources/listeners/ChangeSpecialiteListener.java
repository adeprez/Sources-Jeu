package listeners;

import java.util.EventListener;

import perso.AbstractPerso;
import specialite.Specialite;

public interface ChangeSpecialiteListener extends EventListener {
	public void changeSpecialite(AbstractPerso perso, Specialite ancienne, Specialite nouvelle);
}
