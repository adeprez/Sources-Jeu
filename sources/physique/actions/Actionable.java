package physique.actions;

import interfaces.Fermable;
import interfaces.Lancable;


public interface Actionable extends Lancable, Fermable {
    public boolean faireAction();
}
