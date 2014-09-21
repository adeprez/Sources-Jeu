package partie.modeJeu;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import layouts.LayoutLignes;
import perso.Perso;
import reseau.ressources.RessourcePerso;
import reseau.ressources.RessourceReseau;
import reseau.ressources.RessourcesReseau;
import reseau.ressources.RessourcesServeur;
import reseau.ressources.TypeRessource;
import reseau.serveur.Serveur;

public abstract class JeuSolo extends Jeu {


    public JeuSolo(Serveur serveur) {
	super(serveur);
    }

    @Override
    public int nextIDEquipe(RessourcePerso r) {
	return r.getID() + 1;
    }

    @Override
    public List<Component> getComposants(RessourcesServeur r) {
	return creerComposants(r);
    }

    public static List<Component> creerComposants(RessourcesReseau r) {
	List<Component> l = new ArrayList<Component>();
	JPanel p = new JPanel(new LayoutLignes());
	p.setOpaque(false);
	for(final RessourceReseau<?> rr : r.get(TypeRessource.PERSO).values())
	    p.add(getInterface((Perso) rr.getRessource()));
	l.add(p);
	return l;
    }

}
