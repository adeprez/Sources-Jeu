package util;

import java.util.ArrayList;
import java.util.List;

import physique.actions.Actionable;
import temps.AbstractPeriodique;

public class EnsembleActionables extends AbstractPeriodique {
	private final List<Actionable> actions;


	public EnsembleActionables(int delai) {
		this(delai, new ArrayList<Actionable>());
	}

	public EnsembleActionables(int delai, ArrayList<Actionable> actions) {
		super(delai);
		this.actions = actions;
	}

	public void addAction(Actionable action) {
		synchronized(actions) {
			actions.add(action);
			action.lancer();
		}
	}

	public void removeAction(Actionable action) {
		synchronized(actions) {
			actions.remove(action);
			action.fermer();
		}
	}

	public void terminerActions() {
		synchronized(actions) {
			while(!actions.isEmpty())
				actions.remove(0).fermer();
		}
	}

	public void faireActions() {
		synchronized(actions) {
			int i = 0;
			while(i < actions.size()) try {
				if(actions.get(i).faireAction())
					i++;
				else actions.remove(i).fermer();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public String toString() {
		return actions.toString();
	}

	@Override
	public boolean fermer() {
		if(super.fermer()) {
			terminerActions();
			return true;
		}
		return false;
	}

	@Override
	public void iteration() {
		faireActions();
	}

}
