package physique;

import listeners.ContactListener;
import listeners.FinActionListener;
import map.Map;
import physique.actions.AbstractAction;
import physique.actions.Actionable;
import physique.forme.Forme;
import exceptions.ExceptionJeu;
import exceptions.HorsLimiteException;

public abstract class MobileActionable extends Mobile implements Actionable {
    private AbstractAction<?> action, actionContact;
    private FinActionListener finAction;
    private ContactListener contact;


    public MobileActionable(Forme forme) {
	super(forme);
    }

    public abstract Map getMap();

    public void setContactSolListener(ContactListener contact) {
	this.contact = contact;
    }

    public void removeContactSolListener(ContactListener contact) {
	if(this.contact == contact)
	    this.contact = null;
    }

    public void setFinActionListener(FinActionListener finAction) {
	this.finAction = finAction;
    }

    public synchronized void forceAction(AbstractAction<?> a) {
	forceStopAction();
	action = a;
	action.demarre();
    }

    public synchronized boolean setAction(AbstractAction<?> a) {
	if(stopAction(a)) {
	    action = a;
	    action.demarre();
	    return true;
	}
	return false;
    }

    public void forceStopAction() {
	if(action != null) {
	    action.seTermine();
	    notifyFinAction(action, action.getSuivante());
	    actionSuivante();
	}
    }

    public void notifyFinAction(AbstractAction<?> action, AbstractAction<?> nouvelle) {
	if(finAction != null)
	    finAction.finAction(action, nouvelle);
    }

    public void actionSuivante() {
	AbstractAction<?> a = action.getSuivante();
	action = null;
	if(a != null && !a.lancer())
	    setActionContactSol(a);
    }

    public boolean stopAction(AbstractAction<?> nextAction) {
	if(action == null)
	    return true;
	if(action.fermer()) {
	    notifyFinAction(action, nextAction);
	    actionSuivante();
	    return true;
	}
	return false;
    }

    public void setActionContactSol(AbstractAction<?> action) {
	this.actionContact = action;
    }

    public AbstractAction<?> getAction() {
	return action;
    }

    public boolean enAction() {
	return action != null;
    }

    public void contactHaut(int vitesse) {
	if(contact != null)
	    contact.contactHaut(vitesse);
    }

    @Override
    public double getCoefReductionForceX() {
	return 1.5;
    }

    @Override
    public double getCoefReductionForceY() {
	return 1.5;
    }

    @Override
    public void collisionSol(Collision c) {
	int vol = getTempsVol();
	if(vol > 0 && contact != null)
	    contact.contactSol(vol);
	if(actionContact != null) {
	    AbstractAction<?> a = actionContact;
	    actionContact = null;
	    setAction(a);
	}
    }

    @Override
    public Collision forceY() throws HorsLimiteException {
	float y = getForceY();
	Collision c = super.forceY();
	if(c != null)
	    if(y > 0)
		contactHaut((int) y);
	    else collisionSol(c);
	return c;
    }

    @Override
    public boolean lancer() {
	return true;
    }

    @Override
    public boolean fermer()  {
	return true;
    }

    @Override
    public boolean faireAction() {
	if(action != null) try {
	    action.faireAction();
	} catch(Exception err) {
	    err.printStackTrace();
	} try {
	    forces();
	} catch(Exception err) {}
	if(enMouvement()) try {
	    deplace();
	} catch(ExceptionJeu e) {
	} try {
	    gravite();
	} catch(ExceptionJeu e) {}
	return true;
    }


}
