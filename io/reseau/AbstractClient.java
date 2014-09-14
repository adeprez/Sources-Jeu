package reseau;

import interfaces.FiltreEnvoi;
import interfaces.IOable;
import interfaces.StyleListe;
import io.IO;

import java.awt.GridLayout;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JPanel;

import perso.Perso;
import perso.Vivant;
import physique.actions.AbstractAction;
import physique.actions.Action;
import physique.actions.ActionChangeArme;
import physique.actions.ActionMarche;
import physique.actions.ActionRoulade;
import physique.actions.ActionSaut;
import reseau.donnees.MessageServeur;
import reseau.listeners.DeconnexionListener;
import reseau.paquets.PaquetMessageServeur;
import reseau.paquets.TypePaquet;
import reseau.ressources.RessourcePerso;
import reseau.ressources.RessourceReseau;
import reseau.ressources.RessourcesReseau;
import reseau.ressources.listeners.ChangeRessourceListener;
import vision.Orientation;
import controles.TypeAction;
import divers.Outil;

public abstract class AbstractClient extends InOutReseau
implements StyleListe, ClientServeurIdentifiable, FiltreEnvoi, ChangeRessourceListener<Perso> {
    private final RessourcesReseau ressources;
    private RessourcePerso perso;
    private int ping;
    private int id;


    public AbstractClient(Socket socket, RessourcesReseau ressources) throws IOException {
	super(socket);
	this.ressources = ressources;
	id = -1;
    }

    protected abstract boolean traiter(TypePaquet type, IO in);

    public Perso getPerso(int id) {
	return ressources.getPerso(id).getPerso();
    }

    public Perso getPerso() {
	if(perso == null) {
	    perso = getRessourcePerso(id);
	    perso.addChangeRessourceListener(this);
	}
	return perso.getPerso();
    }

    public RessourcePerso getRessourcePerso(int id) {
	return ressources.getPerso(id);
    }

    public RessourcesReseau getRessources() {
	return ressources;
    }

    public int getID() {
	return id;
    }

    public void setID(int id) {
	this.id = id;
    }

    public int getPing() {
	return ping;
    }

    public void setPing(int ping) {
	this.ping = ping;
	notifyActualiseListener();
    }

    public void setPing(long t) {
	setPing((int) (System.currentTimeMillis() - t));
    }

    public void addDeconnexionListener(DeconnexionListener<AbstractClient> l) {
	addListener(DeconnexionListener.class, l);
    }

    public void removeDeconnexionListener(DeconnexionListener<AbstractClient> l) {
	removeListener(DeconnexionListener.class, l);
    }

    @SuppressWarnings("unchecked")
    private void notifyDeconnexionListener() {
	for(final DeconnexionListener<AbstractClient> l : getListeners(DeconnexionListener.class))
	    l.deconnexion(this);
    }

    public void fermer(MessageServeur message) {
	write(new PaquetMessageServeur(message));
	fermer();
    }

    public boolean faireAction(int id, TypeAction action, boolean debut, IO io) {
	Perso p = getPerso(id);
	p.setAngle(io.next());
	p.setDroite(io.nextBoolean());
	switch(action) {
	case DROITE:
	case GAUCHE:
	    if(debut) {
		if(faireAction(new ActionMarche(p, action.getOrientation() == Orientation.DROITE))) {
		    p.setOrientation(action.getOrientation());
		    return true;
		}
		return false;
	    }
	    if(p.enAction() && !(p.getAction() instanceof ActionMarche) && p.getAction().getSuivante() instanceof ActionMarche) {
		p.getAction().setSuivante(null);
		return true;
	    }
	    return p.stopAction(null);
	case SAUT:
	    return faireAction(new ActionSaut(p));
	case ROULADE:
	    return faireAction(new ActionRoulade(p));
	case CHANGER_ARME:
	    return faireAction(new ActionChangeArme(p, !estServeur() || io.aByte() ? io.nextPositif() : p.getSpecialitePrecedente()));
	case ACCROUPI:
	    return p.setAccroupi(debut, estServeur());
	case ATTAQUER:
	    return debut && faireAction(p.getSpecialite().getArme().getAction(p));
	default:
	    System.err.println(action + " non implementee (AbstractClient)");
	    return false;
	}
    }

    public <K extends Vivant> boolean faireAction(Action<K> action) {
	AbstractAction<?> a = action.getSource().getAction();
	if(a != null && !a.peutArret() && (a.estCompetence() || ((Action<?>) a).getType() != action.getType())) {
	    action.getSource().getAction().setSuivante(action);
	    return true;
	}
	if(estServeur())
	    return action.lancer();
	action.forceLancement();
	return true;
    }

    @Override
    public void change(Perso ancien, RessourceReseau<Perso> r) {
	perso.removeChangeRessourceListener(this);
	perso = (RessourcePerso) r;
	perso.addChangeRessourceListener(this);
    }

    @Override
    public boolean fermer() {
	if(super.fermer()) {
	    notifyDeconnexionListener();
	    return true;
	}
	return false;
    }

    @Override
    protected void notifyReceiveListener(TypePaquet type, IO in) {
	if(traiter(type, in))
	    super.notifyReceiveListener(type, in);
    }

    @Override
    public boolean doitEnvoyer(int id, IOable io) {
	return this.id == id;
    }

    @Override
    public JPanel creerVue() {
	JPanel p = new JPanel(new GridLayout());
	p.add(Outil.getTexte(getAdresse() + "", false));
	p.add(Outil.getTexte("ID=" + id, false));
	return p;
    }

    @Override
    public String toString() {
	return id + ", " + getPerso();
    }


}
