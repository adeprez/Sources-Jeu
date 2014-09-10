package listeners;

import physique.actions.AbstractAction;


public interface FinActionListener {
	public void finAction(AbstractAction<?> terminee, AbstractAction<?> nouvelle);
}
