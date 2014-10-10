package listeners;

import java.util.EventListener;

import physique.actions.AbstractAction;


public interface FinActionListener extends EventListener {
    public void finAction(AbstractAction<?> terminee, AbstractAction<?> nouvelle);
}
