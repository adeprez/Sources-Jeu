package map;

import interfaces.ContaineurImagesOp;
import interfaces.Localise3D;
import interfaces.Sauvegardable;
import io.IO;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JComboBox;

import physique.Mobile;
import physique.MondePhysique;
import reseau.serveur.Serveur;
import ressources.compte.Compte;
import carte.element.Lieu;

import composants.styles.RenduListeStyle;
import composants.styles.fenetres.FenetrePopup;

import divers.Liste;
import divers.Outil;
import exceptions.AnnulationException;

public abstract class MapIO<E extends Localise3D & Sauvegardable> extends MondePhysique<E> implements Sauvegardable {
    public static final int CODE_COLONNE = 255;
    public static final String PATH = "/map";
    private final ContaineurImagesOp images;
    private final Serveur serveur;
    private boolean chargee;


    public MapIO(ContaineurImagesOp images, Serveur serveur) {
	this.images = images;
	this.serveur = serveur;
	setLargeurExtensible(true);
    }

    public MapIO(ContaineurImagesOp images, Serveur serveur, IO io) {
	this(images, serveur);
	int taille = io.nextShortInt();
	agrandir(taille);
	int id = CODE_COLONNE, x = 0;
	while(x < taille) try {
	    id = io.nextPositif();
	    if(id == CODE_COLONNE)
		x++;
	    else try {
		lire(id, io).setPos(convertX(x), convertY(getAltitude(x)));
	    } catch(Exception err) {
		err.printStackTrace();
	    }
	} catch(Exception err) {
	    err.printStackTrace();
	    break;
	}
	if(id != CODE_COLONNE)
	    System.err.println("Une erreur est survenue lors de la lecture de la carte");
	chargee = true;
    }

    public abstract E lire(int id, IO io);

    public Serveur getServeur() {
	return serveur;
    }

    public void setServeur(Mobile m) {
	m.setServeur(serveur);
    }

    public void enregistrer(String path) {
	Outil.save(this, path + PATH);
    }

    public ContaineurImagesOp getImages() {
	return images;
    }

    @Override
    public int getDistance() {
	return chargee ? super.getDistance() : Integer.MAX_VALUE;
    }

    @Override
    public IO sauvegarder(IO io) {
	io.addShort(getLargeur());
	for(final List<E> le : getObjets()) {
	    for(final E e : le)
		e.sauvegarder(io);
	    io.addBytePositif(CODE_COLONNE);
	}
	return io;
    }

    public static Liste<Map> getAllMaps() {
	Liste<Map> maps = new Liste<Map>();
	for(final Compte c : Compte.getComptes())
	    for(final Lieu l : c.getTerritoire().getLieux())
		if(l.aMap(c.path()))
		    maps.add(l.chargeMap(c.path()));
	return maps;
    }

    public static Map demanderMap() throws AnnulationException {
	JComboBox<Map> b = new JComboBox<Map>(getAllMaps());
	if(b.getItemCount() > 0) {
	    b.setRenderer(new RenduListeStyle());
	    final FenetrePopup p = new FenetrePopup("Choisir une map", b);
	    b.addActionListener(e -> p.dispose());
	    p.afficher(new Dimension(500, 300));
	    if(b.getSelectedIndex() != -1)
		return (Map) b.getSelectedItem();
	}
	throw new AnnulationException();
    }


}
