package reseau.ressources;

import interfaces.ContaineurImageOp;
import interfaces.ContaineurImagesOp;
import interfaces.Sauvegardable;
import io.IO;
import io.Out;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import perso.Perso;
import reseau.ClientServeurIdentifiable;
import reseau.paquets.Paquet;
import reseau.paquets.TypePaquet;
import reseau.ressources.listeners.AddRessourceListener;
import reseau.ressources.listeners.RemoveRessourceListener;
import ressources.Images;
import divers.Listenable;


@SuppressWarnings("unchecked")
public class RessourcesReseau extends Listenable implements Sauvegardable, ContaineurImagesOp, ClientServeurIdentifiable {
    private final Map<Integer, RessourceReseau<?>>[] ressources;


    public RessourcesReseau() {
	ressources = new Map[TypeRessource.values().length];
    }

    public Map<Integer, RessourceReseau<?>> get(TypeRessource typeRessource) {
	if(!aRessources(typeRessource))
	    ressources[typeRessource.ordinal()] = new HashMap<Integer, RessourceReseau<?>>();
	return ressources[typeRessource.ordinal()];
    }

    public boolean aRessources(TypeRessource type) {
	return ressources[type.ordinal()] != null;
    }

    public Collection<RessourceReseau<?>> getListe(TypeRessource typeRessource) {
	return get(typeRessource).values();
    }

    public RessourceReseau<?> getRessource(TypeRessource typeRessource, int id) {
	return get(typeRessource).get(id);
    }

    public boolean aRessource(TypeRessource typeRessource, int id) {
	return get(typeRessource).containsKey(id);
    }

    public <E> void putRessource(RessourceReseau<E> ressource) {
	Map<Integer, RessourceReseau<?>> map = get(ressource.getType());
	if(map.containsKey(ressource.getID()))
	    ((RessourceReseau<E>) map.get(ressource.getID())).setRessource(ressource.getRessource());
	else map.put(ressource.getID(), ressource);
	notifyAddRessourceListener(ressource);
    }

    public void removeRessource(TypeRessource type, int id) {
	removeRessource(get(type).get(id));
    }

    public void removeRessource(RessourceReseau<?> ressource) {
	if(get(ressource.getType()).remove(ressource.getID()) != null)
	    notifyRemoveRessourceListener(ressource);
    }

    public void removeAll() {
	for (int i = 0; i < ressources.length; i++)
	    if(ressources[i] != null) {
		ressources[i].clear();
		ressources[i] = null;
	    }
    }

    public int getNombreRessources() {
	int n = 0;
	for(final Map<Integer, RessourceReseau<?>> e : ressources)
	    n += e == null ? 0 : e.size();
	return n;
    }

    public void ecrire(Out out) {
	for(final Map<Integer, RessourceReseau<?>> e : ressources)
	    if(e != null)
		for(final RessourceReseau<?> r : e.values())
		    out.write(new Paquet(TypePaquet.ADD_RESSOURCE, r));
    }

    public int getNextID(TypeRessource typeRessource) {
	int id = 0;
	while(get(typeRessource).containsKey(id))
	    id++;
	return id;
    }

    public int getMaxID(TypeRessource typeRessource) {
	int max = 0;
	for(final RessourceReseau<?> r : get(typeRessource).values())
	    if(r.getID() > max)
		max = r.getID();
	return max;
    }

    public RessourceReseau<?> getRessource(TypeRessource type, Object o) {
	for(final RessourceReseau<?> e : get(type).values())
	    if(e.getRessource() == o)
		return e;
	return null;
    }

    public int getIDPerso(Object perso) {
	return getRessource(TypeRessource.PERSO, perso).getID();
    }

    public RessourceImage getRessourceImage(int id) {
	return (RessourceImage) get(TypeRessource.IMAGE).get(id);
    }

    public RessourceImage getRessourceImageObjet(int id) {
	return (RessourceImage) get(TypeRessource.IMAGE_OBJET).get(id);
    }

    public RessourceMap getMap(int id) {
	return (RessourceMap) get(TypeRessource.MAP).get(id);
    }

    public RessourcePerso getPerso(int id) {
	return (RessourcePerso) get(TypeRessource.PERSO).get(id);
    }

    public RessourceJeu getJeu(int id) {
	return (RessourceJeu) get(TypeRessource.CONFIG_JEU).get(id);
    }

    public Map<Integer, List<Perso>> getEquipes() {
	Map<Integer, List<Perso>> map = new HashMap<Integer, List<Perso>>();
	for(final RessourceReseau<?> r : get(TypeRessource.PERSO).values()) {
	    Perso p = ((RessourcePerso) r).getPerso();
	    if(!map.containsKey(p.getEquipe()))
		map.put(p.getEquipe(), new ArrayList<Perso>());
	    map.get(p.getEquipe()).add(p);
	}
	return map;
    }

    public Set<Integer> getIDEquipes() {
	Set<Integer> s = new HashSet<Integer>();
	for(final RessourceReseau<?> r : get(TypeRessource.PERSO).values())
	    s.add(((RessourcePerso) r).getPerso().getEquipe());
	return s;
    }

    public void addAddRessourceListener(AddRessourceListener l) {
	addListener(AddRessourceListener.class, l);
    }

    public void removeAddRessourceListener(AddRessourceListener l) {
	removeListener(AddRessourceListener.class, l);
    }

    public <E> void notifyAddRessourceListener(RessourceReseau<E> r) {
	for(final AddRessourceListener l : getListeners(AddRessourceListener.class))
	    l.add(r);
	notifyActualiseListener();
    }

    public void addRemoveRessourceListener(RemoveRessourceListener l) {
	addListener(RemoveRessourceListener.class, l);
    }

    public void removeRemoveRessourceListener(RemoveRessourceListener l) {
	removeListener(RemoveRessourceListener.class, l);
    }

    public <E> void notifyRemoveRessourceListener(RessourceReseau<E> r) {
	for(final RemoveRessourceListener l : getListeners(RemoveRessourceListener.class))
	    l.remove(r);
	notifyActualiseListener();
    }

    public map.Map getMap() {
	return getMap(0).getMap();
    }

    public RessourceReseau<?> lire(IO io) {
	switch(TypeRessource.get(io.nextPositif())) {
	case IMAGE: return new RessourceImage(io);
	case MAP: return new RessourceMap(this, io);
	case PERSO: return new RessourcePerso(io, getMap());
	case CONFIG_JEU: return new RessourceJeu(io);
	case IMAGE_OBJET: return new RessourceImageObjet(io);
	case SON:
	default: throw new IllegalAccessError("Type de ressource non reconnu");
	}
    }

    @Override
    public boolean estServeur() {
	return false;
    }

    @Override
    public BufferedImage getImage(int id) {
	try {
	    return getRessourceImageObjet(id).getImage();
	} catch(Exception e) {
	    return Images.get("Objet " + id, true);
	}
    }

    @Override
    public IO sauvegarder(IO io) {
	return io.addShort(getNombreRessources());
    }

    @Override
    public String toString() {
	return ressources + "";
    }

    @Override
    public ContaineurImageOp getImageOp(int id) {
	return getRessourceImageObjet(id);
    }

}
