package territoire;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTabbedPane;

import ressources.Fichiers;
import ressources.compte.Compte;
import statique.Style;
import base.Ecran;
import carte.Territoire;
import carte.element.CaseTerritoire;
import carte.element.Lieu;
import divers.Outil;

public class GestionTerritoire extends Ecran implements ActionListener {
	private static final long serialVersionUID = 1L;
	private final ConstructionTerritoire construct;
	private final InformationCaseTerritoire infos;
	private final JTabbedPane t;
	private final Compte compte;
	private final GestionLieu lieu;
	private CaseTerritoire objet;


	public GestionTerritoire(Compte compte) {
		this.compte = compte;
		setName("Gestion territoire");
		setLayout(new BorderLayout());
		construct = new ConstructionTerritoire(compte.getRessources(), this);
		infos = new InformationCaseTerritoire();
		lieu = new GestionLieu(compte);
		t = new JTabbedPane();
		t.setFont(Style.POLICE);
		t.addTab("Construction", construct);
		t.addTab("Informations", infos);
		add(t);
	}

	public void setSelection(CaseTerritoire objet) {
		this.objet = objet;
		infos.setSelection(objet);
		construct.selection(objet);
		if(objet.getLieu() != null) {
			lieu.setLieu(objet.getLieu());
			t.addTab(objet.getNomContenu(), lieu);
			t.setSelectedIndex(2);
		} 
		else if(t.getTabCount() > 2)
			t.removeTabAt(2);
	}

	public void deselection() {
		objet = null;
		infos.deselection();
		construct.selection(null);
		if(t.getTabCount() > 2)
			t.removeTabAt(2);
	}

	public boolean testeExistance(String nom) {
		if(Fichiers.existe(compte.path() + Territoire.PATH + Lieu.PATH + nom)) {
			Outil.erreur("Un lieu porte deja le nom \"" + nom + "\"");
			return true;
		}
		return false;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(objet == null)
			Outil.erreur("Selectionnez un emplacement constructible sur la carte");
		else {
			Lieu l = construct.getLieu();
			if(Outil.confirmer("Construire " + l.getType().getNomArticle() + " sur " + objet + " ?\n(Prix : " + l.getPrix() + ")")) {
				try {
					String nom;
					do {
						nom = Outil.demander("Nom du lieu ?");
					} while(testeExistance(nom));
					l.construire(nom, objet, compte, true);
					compte.getTerritoire().getLieux().add(l);
					lieu.setLieu(objet.getLieu());
					t.addTab(objet.getNomContenu(), lieu);
					t.setSelectedIndex(2);
				} catch(Exception err) {
					if(err.getMessage() != null && !err.getMessage().isEmpty())
						Outil.erreur(err.getMessage());
				}
			}
		}
	}

}
