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
import physique.actions.AbstractAction;
import physique.actions.vehicule.ActionRoule;
import physique.actions.vivant.ActionAccroupi;
import physique.actions.vivant.ActionChangeArme;
import physique.actions.vivant.ActionGrimpeCorde;
import physique.actions.vivant.ActionMarche;
import physique.actions.vivant.ActionRoulade;
import physique.actions.vivant.ActionSaut;
import physique.vehicule.Vehicule;
import reseau.listeners.DeconnexionListener;
import reseau.paquets.Paquet;
import reseau.paquets.PaquetMessage;
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

    public void fermer(String message) {
	write(new PaquetMessage(PaquetMessage.SERVEUR, message));
	fermer();
    }

    public boolean faireAction(int id, TypeAction action, boolean debut, IO io) {
	Perso p;
	try {
	    p = getPerso(id);
	} catch(Exception err) {
	    return false;
	}
	if(!p.estVivant())
	    if(estServeur())
		return false;
	    else write(new Paquet(TypePaquet.VIE));
	p.setAngle(io.next());
	p.setDroite(io.nextBoolean());
	if(p.dansVehicule())
	    return faireActionVehicule(action, p, p.getVehicule(), debut, io);
	switch(action) {
	case DROITE:
	case GAUCHE:
	    if(debut) {
		if(p.enAction() && p.getAction() instanceof ActionAccroupi)
		    return ((ActionAccroupi) p.getAction()).commenceMarche(action.getOrientation() == Orientation.DROITE);
		if(faireAction(new ActionMarche(p))) {
		    p.setOrientation(action.getOrientation());
		    return true;
		}
		return false;
	    }
	    if(p.enAction())
		if(p.getAction() instanceof ActionAccroupi)
		    return ((ActionAccroupi) p.getAction()).stopMarche(action.getOrientation());
		else if(!(p.getAction() instanceof ActionMarche) && p.getAction().getSuivante() instanceof ActionMarche) {
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
	    if(debut) {
		if(p.enAction() && p.getAction() instanceof ActionAccroupi)
		    return false;
		ActionAccroupi a = new ActionAccroupi(p);
		if(p.enAction() && p.getAction() instanceof ActionMarche)
		    a.setMarche(p.estDroite());
		return faireAction(a);
	    }
	    if(p.enAction() && p.getAction() instanceof ActionAccroupi)
		return p.stopAction(null);
	case ATTAQUER:
	    return faireAction(p.getSpecialite().getArme().getAction(p));
	case ESCALADER:
	    return faireAction(new ActionGrimpeCorde(p));
	default:
	    return autreAction(id, p, action, debut, io);
	}
    }

    public boolean autreAction(int id, Perso p, TypeAction action, boolean debut, IO io) {
	System.err.println(action + " non implementee (AbstractClient)");
	return false;
    }

    public boolean faireActionVehicule(TypeAction action, Perso p, Vehicule v, boolean debut, IO io) {
	switch(action) {
	case ENTRER_VEHICULE:
	    return v.retire(p);
	case GAUCHE:
	case DROITE:
	    if(debut) {
		if(faireAction(new ActionRoule(v))) {
		    v.setOrientation(action.getOrientation());
		    return true;
		}
		return false;
	    }
	    if(v.enAction())
		if(!(v.getAction() instanceof ActionRoule) && v.getAction().getSuivante() instanceof ActionRoule) {
		    v.getAction().setSuivante(null);
		    return true;
		}
	    return v.stopAction(null);
	default:
	    return false;
	}
    }

    public boolean faireAction(AbstractAction<?> action) {
	AbstractAction<?> a = action.getSource().getAction();
	if(a != null && !a.peutArret() && a.getClass() != action.getClass()) {
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
