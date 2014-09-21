package listeners;

import java.util.EventListener;

import perso.Vivant;
import physique.PhysiqueDestructible;

public interface DestructibleListener extends EventListener {
    public void meurt(PhysiqueDestructible p, Vivant tueur);
    public void vieChange(PhysiqueDestructible p);
}
