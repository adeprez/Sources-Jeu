package reseau.ressources;

import interfaces.Sauvegardable;
import io.IO;
import reseau.ressources.listeners.ChangeRessourceListener;
import divers.Listenable;

public abstract class RessourceReseau<E> extends Listenable implements Sauvegardable {
    private final int id;
    private IO io;


    public RessourceReseau(int id) {
	this.id = id;
    }

    public RessourceReseau(IO io) {
	this(io.nextPositif());
    }

    public abstract TypeRessource getType();
    protected abstract void ecrireDonnees(IO io);
    public abstract E getRessource();
    public abstract void affecteRessource(E ressource);

    public void change() {
	io = null;
    }

    public IO sauvegarder() {
	if(io == null)
	    io = sauvegarder(new IO());
	return io;
    }

    public void setRessource(E ressource) {
	E tmp = getRessource();
	affecteRessource(ressource);
	change();
	notifyChangeRessourceListener(tmp);
    }

    public void addChangeRessourceListener(ChangeRessourceListener<E> l) {
	addListener(ChangeRessourceListener.class, l);
    }

    public void removeChangeRessourceListener(ChangeRessourceListener<E> l) {
	removeListener(ChangeRessourceListener.class, l);
    }

    @SuppressWarnings("unchecked")
    public void notifyChangeRessourceListener(E ancien) {
	for(final ChangeRessourceListener<E> l : getListeners(ChangeRessourceListener.class))
	    l.change(ancien, this);
    }

    public int getID() {
	return id;
    }

    @Override
    public IO sauvegarder(IO io) {
	io.addBytePositif(getType().getID()).addBytePositif(id);
	ecrireDonnees(io);
	return io;
    }


}
