package temps;

import java.util.EventListener;

public interface HorlogeListener extends EventListener {
	public void action(Horloge horloge);
}
