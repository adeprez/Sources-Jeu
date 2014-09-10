package listeners;

import java.util.EventListener;

import physique.PhysiqueDestructible;

public interface DestructibleListener extends EventListener {
	public void meurt(PhysiqueDestructible p);
	public void vieChange(PhysiqueDestructible p);
}
